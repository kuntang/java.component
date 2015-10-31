package com.sohu.smc;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;


//@Service
public class HttpUtils {

	public  String getContentByUrl(String url){
		StringBuilder sb = new StringBuilder();

		 String line = null;  
		 BufferedReader  inReader = null;
		 HttpClient client = getHttpClient();
		 HttpGet request = new HttpGet(); 
		 request.setHeader("Connection", "close");
		try {  
            request.setURI(new URI(url));  
            HttpResponse response = client.execute(request);  
            inReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"gbk"));
            while ((line = inReader.readLine())  != null) {  
                sb.append(line);  
            }                     
        } catch (URISyntaxException e) {
			// TODO Auto-generated catch block
//        	LOG.error("HttpUtils getContentByUrl URISyntaxException: url="+url,e);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
//			LOG.error("HttpUtils getContentByUrl ClientProtocolException",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
//			LOG.error("HttpUtils getContentByUrl IOException ",e);
		} finally {  
            if (inReader != null) {  
                try {  
                	inReader.close();// 关闭reader  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }            
        }
		return sb.toString();
	}
	
	public String getContentByUrl(String url,String enconding){
		StringBuilder sb = new StringBuilder();
		HttpClient client = getHttpClient();  
        InputStream in = null;  
        BufferedReader inReader = null;  
        HttpGet get = new HttpGet();  
        get.setHeader("Connection", "close");
        try  
        {  
            get.setURI(new URI(url));  
            HttpResponse response = client.execute(get);  
            if (response.getStatusLine ().getStatusCode () != 200) {  
                // close_wait 异常,被动关闭时,释放连接
            	get.abort();  
                return sb.toString();  
            }  
            HttpEntity entity =response.getEntity();         
            if( entity != null ){  
                in = entity.getContent();  
                String line = null;
                inReader = new BufferedReader(new InputStreamReader(in,enconding));                          
                while ((line = inReader.readLine())  != null) {                      
                    sb.append(line + "\r\n");
                }           
            }        
        }  catch (Exception e)  {  
        	// close_wait 异常,被动关闭时,释放连接
            get.abort();  
            e.printStackTrace ();  

            return sb.toString();  
        }finally{  
            if (inReader != null){  
                try  
                {  
                	inReader.close ();  
                }  
                catch (IOException e)  
                {  
                    e.printStackTrace ();  
                }  
            }  
            if (in != null){  
                try  
                {  
                    in.close ();  
                }  
                catch (IOException e)  
                {  
                    e.printStackTrace ();  
                }  
            }  
        }  
		return sb.toString();
	}
	
	public String executeUrl(String url){
		StringBuilder sb = new StringBuilder();
		HttpClient client = getHttpClient();  
        HttpPost post = new HttpPost();
       // get.setHeader("http.protocol.cookie-policy",CookiePolicy.IGNORE_COOKIES);
        post.setHeader("Connection", "close");
        post.setHeader("contentType","jpg");
        InputStream in = null;  
        BufferedReader inReader = null;  
        try  
        {  
            post.setURI(new URI(url));
//            reqEntity.addPart("file", new FileBody(new File("D:\\tk.jpg")) );
//            post.setEntity(reqEntity);

            File file = new File("D:\\tk.jpg");
//            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
//            builder.addBinaryBody
//                    ("upfile", file, ContentType.DEFAULT_BINARY, "tk.jpg");
//            HttpEntity entityOut = builder.build();

            FileEntity fileEntity = new FileEntity(file);
            fileEntity.setContentType("jpg");
            post.setEntity(fileEntity);
            HttpResponse response = client.execute(post);
            if (response.getStatusLine ().getStatusCode () != 200) {  
                // close_wait 异常,被动关闭时,释放连接
            	post.abort();
            } 
            HttpEntity entity =response.getEntity();         
            if( entity != null ){  
                in = entity.getContent();  
                String line = null;
                inReader = new BufferedReader(new InputStreamReader(in,"utf-8"));                          
                while ((line = inReader.readLine())  != null) {                      
                    sb.append(line + "\r\n");
                }           
            }        
        }  catch (Exception e)  {  
        	// close_wait 异常,被动关闭时,释放连接
            post.abort();
            e.printStackTrace ();  

        } finally{ 
        		post.abort();
        }  
         return sb.toString();
	}
	
	public  HttpClient getHttpClient(){
		  HttpClient client = new DefaultHttpClient();  
		  //关闭设置代理
		  // HttpHost proxy = new HttpHost(ConstantUtils.PROXY_HOST, ConstantUtils.PROXY_PORT);   
          // client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);  
          // 解决 Cookie rejected:
          client.getParams().setParameter("http.protocol.cookie-policy",CookiePolicy.IGNORE_COOKIES);
          return client;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HttpUtils http = new HttpUtils();
		String content = http.executeUrl("http://localhost:2012/comment/uploadImage/123.jgp");
		System.out.println(content);
	}

}
