package com.sohu.smc.common.local.leveldb;


import com.sohu.smc.common.ipc.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kuntang
 * Date: 14-4-8
 * Time: 上午11:08
 * To change this template use File | Settings | File Templates.
 */
public class UserInfo implements Writable {
    private long pid;
    private String nickName;
    private String headUrl;
    private int gender;
    private String userId;
    private String signList;        // 徽章
    private int role;               //用户角色

    public long getPid() {
        return pid;
    }

    public String getNickName() {
        return nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public int getGender() {
        return gender;
    }

    public String getUserId() {
        return userId;
    }


    public void setPid(long pid) {
        this.pid = pid;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSignList() {
        return signList;
    }

    public void setSignList(String signList) {
        this.signList = signList;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
        out.writeLong(pid);
        out.writeUTF(nickName);
        out.writeUTF(headUrl);
        out.writeInt(gender);
        out.writeUTF(userId);
        out.writeUTF(signList);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.pid = in.readLong();
        this.nickName = in.readUTF();
        this.headUrl = in.readUTF();
        this.gender = in.readInt();
        this.userId = in.readUTF();
        this.signList = in.readUTF();
    }

    public static UserInfo read(DataInput in) throws IOException {
        UserInfo info = new UserInfo();
        info.readFields(in);
        return info;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "pid=" + pid +
                ", nickName='" + nickName + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", gender=" + gender +
                ", userId='" + userId + '\'' +
                ", signList='" + signList + '\'' +
                ", role=" + role +
                '}';
    }

    public static void main(String args[]) throws IOException {

        UserInfo info = new UserInfo();
        info.setGender(1);
        info.setHeadUrl("http://sucimg.itc.cn/avatarimg/wapUpload_1386366049886_c175");
        info.setNickName("玉良言AAA");
        info.setPid(5763088046270451715L);
        info.setUserId("47855ba5be3fe647a395cadfe54a2116@t.qq.sohu.com");
        String levelDbPath =null;
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            levelDbPath = "D:\\sohu_code\\level_db";
        }else{
            levelDbPath = "/opt/scm/leveldb/live_comment.db";
        }




    }


}
