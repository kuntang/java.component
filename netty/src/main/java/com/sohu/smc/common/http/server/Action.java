package com.sohu.smc.common.http.server;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import org.jboss.netty.util.CharsetUtil;


import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Action {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




    protected abstract byte[] action(HttpRequest req, HttpResponse resp,String[] uriSplits)
            throws Exception;

    /**
     * parse the query string parameters.
     *
     * @param request
     *            http request
     * @return a json object contains the parameters
     * @author georgecao
     */
    private HashMap parseQueryString(HttpRequest request) {
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri(),
                Charset.forName("UTF-8"));
        return traversalDecoder(decoder);
    }

    /**
     * traversal the decoder
     *
     * @param decoder
     *            the decoder
     * @author georgecao
     */
    private HashMap traversalDecoder(QueryStringDecoder decoder) {
        HashMap json = new HashMap();
        Iterator<Map.Entry<String, List<String>>> iterator = decoder
                .getParameters().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> entry = iterator.next();
            String value = entry.getValue().get(0);
            json.put(entry.getKey(),value);
        }
        return json;
    }

    /**
     * pack request, get the parameters
     *
     * @param request
     *            the http request
     * @return a json object contains all the parameters
     * @throws Exception
     *             exception
     * @since modified by george cao at 2011-01-20
     */
    public HashMap packRequest(HttpRequest request) {
        String content = request.getContent().toString(Charset.forName("UTF-8"));
        if (!request.getMethod().equals(HttpMethod.GET)) {  // post����
            // ���ݰ�׿��ios�����������ʽ.
            String preStr = "";
            if(content!=null && content.length()>5){
                String conditionPreStr = content.substring(0,4);
                if(!"data".equals(conditionPreStr)) {
                    preStr = "data=";
                }
            }
            QueryStringDecoder decoder = new QueryStringDecoder("/?"
                    +preStr+content
                    + "&");
            return traversalDecoder(decoder);
        } else {
            return parseQueryString(request);
        }
    }

    /**
     * �򵥴ֱ��İ�contentֻ���ɺ���dataһ���ֶβ��������ݸ�ʽ
     * @param request
     * @return
     */
    public HashMap packRequestOnlyDataParam(HttpRequest request) {
        String content = request.getContent().toString(Charset.forName("UTF-8"));
        if(content!=null && content.length()>5){
            String conditionPreStr = content.substring(0,4);
            if("data".equals(conditionPreStr)) {
                content = content.substring(5);
            }
        }
        HashMap json = new HashMap();
        if (!request.getMethod().equals(HttpMethod.GET)) {
            json.put("data",content);
            return json;
        } else {
            return parseQueryString(request);
        }
    }


    public String toString(HashMap map, String key) {
        try {
            return (String) map.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    public int toInt(HashMap map, String key) {
        try {
            return Integer.parseInt((String) map.get(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public long toLong(HashMap map, String key) {
        try {
            return Long.parseLong((String) map.get(key));
        } catch (Exception e) {
            return -1;
        }
    }

    public final void render(HttpResponse response, Object object,
                             com.sohu.smc.common.http.server.annotation.ContentType type) {
        String contentType = TEXT_PLAIN;
        switch (type) {
            case HTML:
                contentType = HTML;
                break;
            case JSON:
                contentType = JSON;
                break;
            case TEXT_PLAIN:
                contentType = TEXT_PLAIN;
                break;
            case XML:
                contentType = XML;
                break;
            case JAVASCRIPT:
                contentType = JAVASCRIPT;
                break;
        }

        response.setContent(ChannelBuffers.copiedBuffer(object.toString(),
                CharsetUtil.getEncoder(Charset.forName("UTF-8")).charset()));
        response.setHeader(CONTENT_TYPE, contentType);

    }

    public static final String ENCODING = "UTF-8";
    public static final Charset CHARSET = Charset.forName(ENCODING);

    public static final String HTML = "text/html; charset=" + ENCODING;
    public static final String JAVASCRIPT = "application/javascript; charset="
            + ENCODING;
    public static final String JSON = "application/json; charset=" + ENCODING;
    public static final String TEXT_PLAIN = "text/plain; charset=" + ENCODING;
    public static final String XML = "application/xml; charset=" + ENCODING;
}
