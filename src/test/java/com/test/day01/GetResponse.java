package com.test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-04 18:27
 * @Desc：
 **/

public class GetResponse {
    @Test
    public void getResponseHeader(){
        Response response =
            given().
            when().
                post("http://www.httpbin.org/post").
            then().
                log().all().extract().response();
        System.out.println("接口的响应时间"+response.time());
        System.out.println(response.getHeader("Content-Type"));
    }

    @Test
    public void getResponseJson01(){
        String jsonData="{\"mobile_phone\":\"13323231111\",\"pwd\":\"12345678\"}";
        Response res =
                given().
                        body(jsonData).
                        header("Content-Type","application/json").
                        header("X-Lemonban-Media-Type","lemonban.v1").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();
        System.out.println(res.jsonPath().<Character>get("data.id"));
    }

    @Test
    public void getResponseJson02(){
        Response res =
                given().
                when().
                        get("http://www.httpbin.org/json").
                then().
                        log().all().extract().response();
        System.out.println(res.jsonPath().<Character>get("slideshow.slides.title"));
        List<String> list = res.jsonPath().getList("slideshow.slides.title");
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }

    @Test
    public void getResponseJson03(){
        Response res =
                given().
                when().
                        get("http://www.baidu.com").
                then().
                        log().all().extract().response();
        //获取文本
        System.out.println(res.htmlPath().<Character>get("html.head.title"));
        //获取属性
        System.out.println(res.htmlPath().<Character>get("html.head.meta[0].@content"));
    }

    @Test
    public void getResponseJson04(){
        Response res =
                given().
                when().
                        get("http://www.httpbin.org/xml").
                then().
                        log().all().extract().response();
        System.out.println(res.xmlPath().<Character>get("slideshow.slide[1].title"));
        System.out.println(res.xmlPath().<Character>get("slideshow.slide[1].@type"));
    }
}
