package com.alipay.springmvc.model;

/**
 * Created by tangkun.tk on 2016/8/30.
 */
public class ResponseObj {

    private final static String OK = "ok";

    private final static String ERROR = "error";

    private Meta meta;

    private Object data;

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseObj success(){
        this.meta = new Meta(true,OK);
        return this;
    }

    public ResponseObj success(Object obj){
        this.meta = new Meta(true,OK);
        this.data = obj;
        return this;
    }

    public ResponseObj failure(){
        this.meta = new Meta(true,ERROR);
        return this;
    }

    public ResponseObj failure(Object obj){
        this.meta = new Meta(true,ERROR);
        this.data = obj;
        return this;
    }


    public class Meta{
        private boolean success;
        private String msg;

        public Meta(boolean success,String msg){
            this.success = success;
            this.msg = msg;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
