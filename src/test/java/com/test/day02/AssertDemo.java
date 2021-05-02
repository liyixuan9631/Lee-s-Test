package com.test.day02;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-05 09:14
 * @Desc：
 **/

public class AssertDemo {
    int memberId;
    String token;

    @Test
    public void testLogin(){
        //RestAssured的全局化配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";

        String jsonDataOfLogin ="{\"mobile_phone\":\"13323231111\",\"pwd\":\"12345678\"}";
        Response res =
                given().
                        //config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))).
                        body(jsonDataOfLogin).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                when().
                        post("/member/login").
                then().
                        log().all().
                        extract().response();
        //1、响应结果断言
        //整数类型
        int code = res.jsonPath().get("code");
        Assert.assertEquals(code,0);
        //字符串类型
        String msg = res.jsonPath().get("msg");
        Assert.assertEquals(msg,"OK");
        //小数类型
        //注意：restassured里面如果返回json小数，那么其类型时float
        //丢失精度问题解决方案：声明restassured返回json小数类型是BigDecimal
        BigDecimal actual =res.jsonPath().get("data.leave_amount");
        BigDecimal expected = BigDecimal.valueOf(1000.01);
        Assert.assertEquals(actual,expected);
        memberId =res.jsonPath().get("data.id");
        token =res.jsonPath().get("data.token_info.token");
    }

    @Test(dependsOnMethods = "testLogin")
    public void testRecharge(){
        //发起充值接口请求
        String jsonDataOfRecharge = "{\"member_id\":"+memberId+",\"amount\":1000.01}";
        Response res2 =
                given().
                        body(jsonDataOfRecharge).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Authorization","Bearer "+token).
                when().
                        post("/member/recharge").
                then().
                        log().all().extract().response();
        BigDecimal actual2 = res2.jsonPath().get("data.leave_amount");
        BigDecimal expected2 = BigDecimal.valueOf(2000.02);
        Assert.assertEquals(actual2,expected2);
    }
}
