package com.lemon.testcase;

import com.alibaba.fastjson.JSONObject;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.lemon.common.BaseTest;


import java.util.List;
import java.util.Map;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-26 22:24
 * @Desc：
 **/

public class RegisterTest extends BaseTest{

    @BeforeClass
    public void setUp(){
        //随机生成没有被注册的手机号码
        String phone1 = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone1",phone1);
        String phone2 = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("phone2",phone2);
    }

    @Test(dataProvider = "getRegisterData")
    public void testRegister(ExcelPojo excelPojo){
        //替换用例数据
        excelPojo = casesReplace(excelPojo);
        //发送注册请求
        Response res = request(excelPojo,"register");
        //响应断言
        assertResponse(excelPojo, res);
        //数据库断言
        assertSQL(excelPojo);
    }

    @DataProvider
    public Object[] getRegisterData(){
        List<ExcelPojo> listData = readSpecifyExcelData(0,0);
        //把集合转换为一个一维数组
        return listData.toArray();
    }

    @AfterTest
    public void teardown(){

    }
}
