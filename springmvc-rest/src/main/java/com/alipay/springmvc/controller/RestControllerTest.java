package com.alipay.springmvc.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.springmvc.model.AdvertiserParam;
import com.alipay.springmvc.model.ResponseObj;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by tangkun.tk on 2016/8/30.
 * 验证aop 和 rest 接口
 */
@RestController
public class RestControllerTest {

    @RequestMapping(value = "/adv", method = RequestMethod.GET)
    public ResponseObj create(@RequestBody @Valid  AdvertiserParam advertiserParam){
        System.out.println(JSON.toJSONString(advertiserParam));
        return new ResponseObj();
    }

    @RequestMapping(value = "/adv/{id}", method = RequestMethod.GET)
    public ResponseObj getAdvertiser(@PathVariable("id") String advId){
        System.out.println("id="+advId);
        throw new javax.validation.ValidationException();
        // return new ResponseObj().success(advId);
    }

    @RequestMapping(value = "/bbc/{id}", method = RequestMethod.GET)
    public ResponseObj getBbcAdvertiser(@PathVariable("id") String advId) throws Exception {
        System.out.println("id="+advId);
        throw new Exception();
        // return new ResponseObj().success(advId);
    }

}
