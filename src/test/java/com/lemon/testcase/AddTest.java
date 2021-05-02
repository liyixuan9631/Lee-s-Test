package com.lemon.testcase;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.PhoneRandomUtil;
import io.restassured.response.Response;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-28 23:24
 * @Desc：
 **/

public class AddTest extends BaseTest {

    @BeforeClass
    public void setUp(){
        //随机生成借款人和管理员的手机号码
        String borrowerPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("borrower_phone",borrowerPhone);
        String adminPhone = PhoneRandomUtil.getUnregisterPhone();
        Environment.envData.put("admin_phone",adminPhone);
        //读取1-4条的测试用例数据
        List<ExcelPojo> list = readSpecifyExcelData(4,0,4);
        for(int i=0;i<list.size();i++){
            ExcelPojo excelPojo = list.get(i);
            excelPojo = casesReplace(excelPojo);
            Response res = request(excelPojo,"addLoan");
            extractToEnvironment(excelPojo,res);
        }
    }

    @Test(dataProvider = "getLoginData")
    public void testAdd(ExcelPojo excelPojo){
        //替换环境变量
        excelPojo = casesReplace(excelPojo);
        //发送请求
        Response res = request(excelPojo,"addLoan");
        //响应断言
        assertResponse(excelPojo,res);
        //数据库断言
        assertSQL(excelPojo);
    }

    @DataProvider
    public Object[] getLoginData(){
        List<ExcelPojo> list = readSpecifyExcelData(4,4);
        //把集合转换为一维数组
        return list.toArray();
    }

    @AfterTest
    public void tearDown(){

    }
}
