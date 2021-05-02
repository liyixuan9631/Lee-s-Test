package com.lemon.common;

import cn.afterturn.easypoi.cache.ExcelCache;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.excel.entity.ExcelBaseParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lemon.data.Constants;
import com.lemon.data.Environment;
import com.lemon.pojo.ExcelPojo;
import com.lemon.util.JDBCUtils;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
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
 * @Create: 2021-04-17 10:45
 * @Desc：
 **/

public class BaseTest {

    @BeforeTest
    public void GlobalSetup() throws FileNotFoundException {
        //RestAssured的全局化配置
        //json小数返回类型是BigDecimal
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //BaseUrl全局配置
        RestAssured.baseURI = Constants.BaseURI;
//        //日志全局重定向到本地文件中
//        File file = new File(System.getProperty("user.dir")+"\\log");
//        if(!file.exists()){
//            file.mkdir();
//        }
//        PrintStream fileOutPutStream = new PrintStream(new File("log/test_all.log"));
//        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new
//                ResponseLoggingFilter(fileOutPutStream));
    }


    /**
     * 实现功能描述：对get、post、patch、put请求做二次封装
     * @param excelPojo  excel每行数据对应的对象
     * @return 接口响应的结果
     */
    public Response request(ExcelPojo excelPojo, String InterfaceModuleName){
        //如果指定输出到文件，则设置重定向输出到文件
        String logFilePath;
        if(Constants.LOG_TO_FILE) {
            //为每一个请求做单独的日志保存
            File dirPath = new File(System.getProperty("user.dir") + "\\log\\" + InterfaceModuleName);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }
            logFilePath = dirPath + "\\test" + excelPojo.getCaseId() + ".log";
            PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        //请求地址
        String url = excelPojo.getUrl();
        //请求方法
        String method = excelPojo.getMethod();
        //请求头
        String headers = excelPojo.getRequestHeader();
        //请求头转成Map
        Map<String,Object> headersMap = JSONObject.parseObject(headers,Map.class);
        //请求参数
        String params = excelPojo.getInputParams();
        //对get、post、patch、put做封装
        Response res = null;
        if("get".equalsIgnoreCase(method)){
            res = given().log().all().headers(headersMap).when().get(url).then().log().all().extract().response();
        }else if("post".equalsIgnoreCase(method)){
            res = given().log().all().headers(headersMap).body(params).when().post(url).then().log().all().extract().response();
        }else if ("patch".equalsIgnoreCase(method)){
            res = given().log().all().headers(headersMap).body(params).when().patch(url).then().log().all().extract().response();
        }
        //向Allure报表中添加日志
        if(Constants.LOG_TO_FILE) {
            try {
                Allure.addAttachment("接口请求响应信息", new FileInputStream(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 实现功能描述：读取sheet里面的所有数据
     * @param sheetNum sheet编号
     * @return void
     */
    public List<com.lemon.pojo.ExcelPojo> readAllExcelData(int sheetNum){
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取的sheet
        importParams.setStartSheetIndex(sheetNum-1);
        //读取Excel
        List<com.lemon.pojo.ExcelPojo> listData = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData;
    }

    /**
     * 实现功能描述：读取sheet里面的指定数据
     * @param sheetNum sheet编号
     * @param startRow 读取的起始行
     * @param readRows 读取的行数
     * @return void
     */
    public List<com.lemon.pojo.ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow, int readRows){
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取的sheet
        importParams.setStartSheetIndex(sheetNum);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        //设置读取的行数
        importParams.setReadRows(readRows);
        //读取Excel
        List<com.lemon.pojo.ExcelPojo> listData = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData;
    }

    /**
     * 实现功能描述：读取sheet里面从指定行开始的所有的数据
     * @param sheetNum sheet编号
     * @param startRow 读取的起始行
     * @return void
     */
    public List<com.lemon.pojo.ExcelPojo> readSpecifyExcelData(int sheetNum, int startRow){
        File file = new File(Constants.EXCEL_FILE_PATH);
        //导入的参数对象
        ImportParams importParams = new ImportParams();
        //读取的sheet
        importParams.setStartSheetIndex(sheetNum);
        //设置读取的起始行
        importParams.setStartRows(startRow);
        //读取Excel
        List<com.lemon.pojo.ExcelPojo> listData = ExcelImportUtil.importExcel(file, ExcelPojo.class, importParams);
        return listData;
    }

    /**
    * 实现功能描述：将对应的接口返回字段提取到环境变量中
    * @param excelPojo 用例数据对象
    * @param res 接口返回Response对象
    * @return void
    */
    public void extractToEnvironment(ExcelPojo excelPojo, Response res){
        if(excelPojo.getExtract()!=null) {
            Map<String, Object> extractMap = JSON.parseObject(excelPojo.getExtract(), Map.class);
            //循环遍历extractMap
            for (String key : extractMap.keySet()) {
                Object path = extractMap.get(key);
                //根据【提取返回数据】列里面的路径表达式去提取实际接口对应的返回字段的值
                Object value = res.jsonPath().get(path.toString());
                //存到环境变量中
                Environment.envData.put(key, value);
            }
        }
    }

    /**
    * 实现功能描述：从环境变量中取得对应的值，进行正则替换
    * @param orgStr 原始字符串
    * @return java.lang.String 替换后的字符串
    */
    public static String regexReplace(String orgStr){
        if(orgStr != null) {
            //pattern：正则表达式的匹配器
            Pattern pattern = Pattern.compile("\\{\\{(.*?)}}");
            //match：去匹配哪一个原始字符串，得到匹配对象
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            while (matcher.find()) {
                //group(0)表示获取到整个匹配到的内容
                //System.out.println(matcher.group(0));
                String outerStr = matcher.group(0);//{{member_id}}
                //group(1)表示获取到{{}}包裹着的内容
                //System.out.println(matcher.group(1));
                String innerStr = matcher.group(1);//member_id
                //从环境变量中取到要替换的字段
                Object replaceStr = Environment.envData.get(innerStr);
                //replace
                result = result.replace(outerStr, replaceStr + "");
            }
            return result;
        }
        return null;
    }

    /**
     * 实现功能描述：对用例数据进行替换（入参+请求头+接口地址+期望结果）
     * @param excelPojo
     * @return com.lemon.pojo.ExcelPojo
     */
    public ExcelPojo casesReplace(ExcelPojo excelPojo){
        //正则替换--->参数输入
        String inputParams = regexReplace(excelPojo.getInputParams());
        excelPojo.setInputParams(inputParams);
        //正则替换--->请求头
        String requestHeader = regexReplace(excelPojo.getRequestHeader());
        excelPojo.setRequestHeader(requestHeader);
        //正则替换--->接口地址
        String url = regexReplace(excelPojo.getUrl());
        excelPojo.setUrl(url);
        //正则替换--->期望的返回结果
        String expected = regexReplace(excelPojo.getExpected());
        excelPojo.setExpected(expected);
        //正则替换--->数据库校验
        String dbAssert = regexReplace(excelPojo.getDbAssert());
        excelPojo.setDbAssert(dbAssert);
        return excelPojo;
    }

    /**
    * 实现功能描述：对响应结果断言
    * @param excelPojo 用例数据实体类对象
    * @param response   接口响应
    * @return void
    */
    public void assertResponse(ExcelPojo excelPojo,Response response){
        if(excelPojo.getExpected()!=null) {
            Map<String, Object> exceptedMap = JSON.parseObject(excelPojo.getExpected());
            for (String key : exceptedMap.keySet()) {
                //获取期望的结果
                Object exceptedValue = exceptedMap.get(key);
                //获取实际的结果
                Object actualValue = response.jsonPath().get(key);
                //比较实际结果与期望结果
                Assert.assertEquals(actualValue, exceptedValue);
            }
        }
    }

    /**
    * 实现功能描述：数据库断言
    * @param excelPojo 用例数据实体类对象
    * @return void
    */
    public void assertSQL(ExcelPojo excelPojo){
        String dbAssert = excelPojo.getDbAssert();
        if(dbAssert!=null){
            Map<String,Object> map = JSONObject.parseObject(dbAssert);
            for(String key : map.keySet()){
                Object expectedValue = map.get(key);
                //System.out.println("expectedValue的类型："+expectedValue.getClass());
                Object actualValue = JDBCUtils.querySingleData(key);
                //System.out.println("actualValue的类型："+actualValue.getClass());
                if(expectedValue instanceof BigDecimal) {
                    Assert.assertEquals(actualValue,expectedValue);
                }else if(expectedValue instanceof Integer){
                    Long expectedValue2 = ((Integer) expectedValue).longValue();
                    Assert.assertEquals(actualValue,expectedValue2);

                }
            }
        }
    }
}
