package com.lemon.data;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-17 11:53
 * @Desc：
 **/

public class Constants {
    //日志输出配置：控制台(false) or 日志文件(true)中
    public static final boolean LOG_TO_FILE = true;
    //Excel文件的路径
    public static final String EXCEL_FILE_PATH = "src/test/resources/api_testcases_futureloan_practice.xls";
    //接口BaseUrl地址
    public static  final String BaseURI = "http://api.lemonban.com/futureloan";
    //数据库baseURI
    public  static final String DB_BASE_URI = "api.lemonban.com";
    //数据库名
    public static final String DB_NAME = "/futureloan";
    //数据库用户名
    public static final String DB_USERNAME = "future";
    //数据库密码
    public static final String DB_PWD = "123456";
}
