package com.settings;

import com.crm.utils.DateTimeUtil;
import com.crm.utils.MD5Util;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        //验证失效时间
        String expireTime = "2020-11-02 10:10:10";
        //当前时间
        String currentTime = DateTimeUtil.getSysTime();

//        int count = expireTime.compareTo(currentTime);
//        System.out.println(count);

        /*String lockState = "0";
        if("0".equals(lockState)){
            System.out.println("账号已锁定");
        }*/

     /*   //浏览器端的IP地址
        String ip = "192.168.1.1";
        //允许访问的IP地址群
        String allowIps = "192.168.1.1,192.168.1.2";

        if(allowIps.contains(ip)){
            System.out.println("有效的IP地址，允许访问系统");
        }else{
            System.out.println("ip地址受限");
        }*/

        String pwd = "123";
        String pwd1 = MD5Util.getMD5(pwd);
        System.out.println(pwd1);

    }
}
