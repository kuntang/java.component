package com.alipay.springmvc.model;

import com.alipay.springmvc.udf.annotation.NotEmpty;

/**
 * Created by tangkun.tk on 2016/8/30.
 */
public class AdvertiserParam {

    @NotEmpty
    private String advertiserName;
    @NotEmpty
    private String desc;

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
