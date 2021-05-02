package com.lemon.testcase;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
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

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-11 14:03
 * @Desc：
 **/

public class LoginTest extends BaseTest {

    @BeforeClass
    public void setup(){
        //生成一个没有被注册过的手机号,放到环境变量中去
        String phone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone",phone);
        //前置条件
        //读取Excel里面的第一条数据——>执行——>生成一条注册过了的手机号码
        List<com.lemon.pojo.ExcelPojo> listData = readSpecifyExcelData(1,0,1);
        ExcelPojo excelPojo = listData.get(0);
        //替换
        excelPojo = casesReplace(excelPojo);
        //执行注册接口请求
        Response resRegister = request(excelPojo,"login");
        //提取注册返回的手机号保存到环境变量中
        extractToEnvironment(excelPojo,resRegister);
    }

    @Test(dataProvider = "getLoginData")
    public void testLogin(com.lemon.pojo.ExcelPojo excelPojo){
        //替换用例数据
        excelPojo = casesReplace(excelPojo);
        //发起登录请求
        Response res = request(excelPojo,"login");
        //断言
        assertResponse(excelPojo,res);
    }

    @DataProvider
    public Object[] getLoginData(){
        List<ExcelPojo> listData = readSpecifyExcelData(1,1);
        //把集合转换为一维数组
        return listData.toArray();
    }

    @AfterTest
    public void teardown(){

    }

}
