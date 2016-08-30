package com.alipay.springmvc.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.springmvc.model.AdvertiserParam;
import com.alipay.springmvc.model.ResponseObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by tangkun.tk on 2016/8/30.
 */
@Controller
public class TestController {






    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(){
        System.out.println("index------------");
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index2(){
        System.out.println("index 2.....................");
        return "index";
    }

    @RequestMapping(value = "/indextest", method = RequestMethod.GET)
    public String index4(){
        System.out.println("index test.....................");
        return "index";
    }

}
