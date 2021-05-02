package com.test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-06 07:11
 * @Desc：
 **/

public class homework01 {
    int memberIdOfAdministrator;
    String tokenOfAdministrator;

    int memberIdOfInvestor;
    String tokenOfInvestor;

    int memberIdOfBorrower;
    String tokenOfBorrower;

    @Test
    public void registerOfAdministrator(){
        given().
                body("{\"mobile_phone\":\"15211119631\",\"pwd\":\"admin6666\",\"type\":0,\"reg_name\":\"admin\"}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
        when().
                post("http://api.lemonban.com/futureloan/member/register").
        then().
                log().all();
    }

    @Test
    public void registerOfInvestor(){
        given().
                body("{\"mobile_phone\":\"15200001111\",\"pwd\":\"investor6666\",\"type\":1,\"reg_name\":\"investor\"}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
        when().
                post("http://api.lemonban.com/futureloan/member/register").
        then().
                log().all();
    }

    @Test
    public void registerOfBorrower(){
        given().
                body("{\"mobile_phone\":\"15200002222\",\"pwd\":\"borrower6666\",\"type\":1,\"reg_name\":\"borrower\"}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
        when().
                post("http://api.lemonban.com/futureloan/member/register").
        then().
                log().all();
    }

    @Test
    public void loginOfAdministrator(){
        Response resOfAdministrator =
        given().
                body("{\"mobile_phone\":\"15211119631\",\"pwd\":\"admin6666\"}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
        when().
                post("http://api.lemonban.com/futureloan/member/login").
        then().
                log().all().extract().response();

        memberIdOfAdministrator = resOfAdministrator.jsonPath().get("data.id");
        System.out.println(memberIdOfAdministrator);

        tokenOfAdministrator = resOfAdministrator.jsonPath().get("data.token_info.token");
        System.out.println(tokenOfAdministrator);
    }

    @Test
    public void loginOfInvestor(){
        Response resOfInvestor =
                given().
                        body("{\"mobile_phone\":\"15200001111\",\"pwd\":\"investor6666\"}").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Content-Type","application/json").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();

        memberIdOfInvestor = resOfInvestor.jsonPath().get("data.id");
        System.out.println(memberIdOfInvestor);

        tokenOfInvestor = resOfInvestor.jsonPath().get("data.token_info.token");
        System.out.println(tokenOfInvestor);
    }

    @Test
    public void loginOfBorrower(){
        Response resOfBorrower =
                given().
                        body("{\"mobile_phone\":\"15200002222\",\"pwd\":\"borrower6666\"}").
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Content-Type","application/json").
                when().
                        post("http://api.lemonban.com/futureloan/member/login").
                then().
                        log().all().extract().response();

        memberIdOfBorrower = resOfBorrower.jsonPath().get("data.id");
        System.out.println(memberIdOfBorrower);

        tokenOfBorrower = resOfBorrower.jsonPath().get("data.token_info.token");
        System.out.println(tokenOfBorrower);
    }

    @Test(dependsOnMethods = "loginOfInvestor")
    public void recharge(){
        given().
                body("{\"member_id\":"+memberIdOfInvestor+",\"amount\":200000}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
                header("Authorization","Bearer "+tokenOfInvestor).
        when().
                post("http://api.lemonban.com/futureloan/member/recharge").
        then().
                log().all();
    }

    @Test(dependsOnMethods = "loginOfAdministrator")
    public void addProject(){
        given().
                body("{\"member_id\":"+memberIdOfAdministrator+",\"title\":\"借款50万\",\"amount\":500000.0,\"loan_rate\":18.0,\"loan_term\":30,\"loan_date_type\":2,\"bidding_days\":5}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
                header("Authorization","Bearer "+tokenOfAdministrator).
        when().
                post("http://api.lemonban.com/futureloan/loan/add").
        then().
                log().all();
    }

    @Test(dependsOnMethods = "loginOfAdministrator")
    public void auditProject(){
        given().
                body("{\"loan_id\":2046846,\"approved_or_not\":true}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
                header("Authorization","Bearer "+tokenOfAdministrator).
        when().
                patch("http://api.lemonban.com/futureloan/loan/audit").
        then().
                log().all();
    }

    @Test(dependsOnMethods = "loginOfInvestor")
    public void invest(){
        given().
                body("{\"loan_id\":2046846,\"member_id\":"+memberIdOfInvestor+",\"amount\":100000}").
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json").
                header("Authorization","Bearer "+tokenOfInvestor).
        when().
                post("http://api.lemonban.com/futureloan/member/invest").
        then().
                log().all();
    }
}
