package com.lemon.testcase;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-18 00:10
 * @Desc：
 **/

public class InvestFlowTest extends BaseTest {
    @BeforeClass
    public void setUp(){
        //生成三个角色的随机手机号码（投资人+借款人+管理员）
        String borrowerPhone = PhoneRandomUtil.getUnregisterPhone();
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        String investPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowerPhone);
        Environment.envData.put("admin_phone",adminPhone);
        Environment.envData.put("invest_phone",investPhone);
        //读取用例数据（1-9）
        List<ExcelPojo> list = readSpecifyExcelData(3,0,9);
        for(int i=0;i< list.size();i++){
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            //发送请求
            Response res = request(excelPojo,"investFlow");
            //提取响应
            extractToEnvironment(excelPojo,res);
        }
    }

    @Test
    public void testInvest(){
        List<ExcelPojo> list = readSpecifyExcelData(3,9);
        ExcelPojo excelPojo = list.get(0);
        //替换
        excelPojo = casesReplace(excelPojo);
        Response res = request(excelPojo,"investFlow");
        //响应断言
        assertResponse(excelPojo, res);
        //数据库断言
        assertSQL(excelPojo);
    }

    @AfterTest
    public void teardown(){

    }
}
