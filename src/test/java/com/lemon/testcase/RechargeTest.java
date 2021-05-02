package com.lemon.testcase;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-11 16:12
 * @Desc：
 **/

public class RechargeTest extends BaseTest {
    int memberId;
    String token;

    @BeforeClass
    public void setUp(){
        //生成一个没有被注册过的手机号,放到环境变量中去
        String phone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone",phone);
        //前置条件
        //读取Excel里面的前两条数据
        List<com.lemon.pojo.ExcelPojo> listData = readSpecifyExcelData(2,0,2);
        //替换
        ExcelPojo excelPojo = listData.get(0);
        excelPojo = casesReplace(excelPojo);
        //注册接口请求
        Response resRegister = request(excelPojo,"recharge");
        //提取接口返回的对应字段，保存到环境变量中
        extractToEnvironment(excelPojo,resRegister);

        //{{mobile_phone}}参数替换
        casesReplace(listData.get(1));

        //登录接口请求
        Response resLogin = request(listData.get(1),"recharge");
        //提取接口返回的对应字段，保存到环境变量中
        extractToEnvironment(listData.get(1),resLogin);
    }

    @Test(dataProvider = "getLoginData")
    public void testRecharge(com.lemon.pojo.ExcelPojo excelPojo){
        //参数替换
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"recharge");
        //断言
        assertResponse(excelPojo,res);
    }

    @DataProvider
    public Object[] getLoginData(){
        List<ExcelPojo> listData = readSpecifyExcelData(2,2);
        //把集合转换为一维数组
        return listData.toArray();
    }

    @AfterTest
    public void teardown(){

    }
}
