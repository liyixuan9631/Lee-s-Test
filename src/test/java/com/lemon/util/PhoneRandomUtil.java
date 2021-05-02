package com.lemon.util;

import java.util.Random;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-26 00:16
 * @Desc：
 **/

public class PhoneRandomUtil {
//    public static void main(String[] args){
//        System.out.println(getUnregisterPhone());
//    }

    /**
    * 实现功能描述：随机生成一个手机号
    * @param
    * @return 返回一个随机生成的手机号
    */
    public static String getRandomPhone(){
        //思路一、先去查询手机号码字段，按照倒序排列，取得最大的手机号+1
        //思路二、先去生成一个随机的手机号码，再通过改手机号进入到数据库查询，如果查询有记录，再生成一个，否则说明该号码没有被注册（循环）
        Random random = new Random();
        //nextInt随机生成一个整数，范围是从0-参数的范围之内
        String phonePrefix = "152";
        //生成8位随机整数-循环拼接
        for(int i=0;i<8;i++){
            //生成一个0-9的随机整数
            int num = random.nextInt(9);
            phonePrefix = phonePrefix + num;
        }
        return phonePrefix;
    }

    /**
    * 实现功能描述：生成一个未注册的手机号
    * @param
    * @return 返回一个未注册的手机号
    */
    public static String getUnregisterPhone(){
        String phone = "";
        while (true) {
            phone = getRandomPhone();
            //查询数据库
            Object result = JDBCUtils.querySingleData("select count(*) from member where mobile_phone="+phone);
            if((Long)result==0){
                //表示手机号没有被注册，符合需求
                break;
            }else{
                //表示手机号已经被注册了，还需要继续执行上述过程
                continue;
            }
        }
        return phone;
    }
}
