package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-05 10:39
 * @Desc：
 **/

public class DataDrivenDemo {

    @Test(dataProvider = "getLoginData02")
    public void testLogin(ExcelPojo excelPojo){
        //RestAssured的全局化配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //接口入参
        String inputParams = excelPojo.getInputParams();
        //接口地址
        String url = excelPojo.getUrl();
        //请求头
        String requestHeader = excelPojo.getRequestHeader();
        //把请求头转换为map
        //Map requestHeaderMap = (Map) JSON.parse(requestHeader);
        Map requestHeaderMap = JSON.parseObject(requestHeader);
        Response res =
                given().
                        body(inputParams).
                        headers(requestHeaderMap).
                when().
                        post(url).
                then().
                        log().all().
                        extract().response();
        String excepted = excelPojo.getExpected();
        Map<String,Object> exceptedMap = JSON.parseObject(excepted);

        for(String key : exceptedMap.keySet()){
            //获取期望的结果
            Object exceptedValue = exceptedMap.get(key);
            //获取实际的结果
            Object actualValue = res.jsonPath().get(key);
            //比较实际结果与期望结果
            Assert.assertEquals(actualValue,exceptedValue);
        }
    }

    @DataProvider
    public Object[][] getLoginData01(){
        Object[][] data = {{"13323230000","123456"},
                {"13323231111","123456"},
                {"12323230000","12345678"}};
        return data;
    }

    @DataProvider
    public Object[] getLoginData02(){
        File file = new File("E:\\Java Automation\\homework\\Port\\20210324\\api_testcases_futureloan_practice.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        //读取excel
        List<ExcelPojo> listData = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        //把集合转换为一维数组
        return listData.toArray();
    }

    public static void main(String[] args){
        File file = new File("E:\\Java Automation\\homework\\Port\\20210324\\api_testcases_futureloan_practice.xls");
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        importParams.setStartSheetIndex(1);
        //读取excel
        List<ExcelPojo> listData = ExcelImportUtil.importExcel(file,ExcelPojo.class,importParams);
        for(Object object : listData){
            System.out.println(object);
        }
    }
}
