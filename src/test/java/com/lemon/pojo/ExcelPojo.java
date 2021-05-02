package com.lemon.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Project: PortAutomation
 * @Site: http://www.lemonban.com
 * @Forum: http://testingpai.com
 * @Copyright: ©2020 版权所有 湖南省零檬信息技术有限公司
 * @Author: Administrator
 * @Create: 2021-04-05 11:18
 * @Desc：
 **/

public class ExcelPojo {
    @Excel(name="序号(caseId)")
    private int caseId;

    @Excel(name="接口模块(module)")
    private String module;

    @Excel(name="用例标题(title)")
    private String title;

    @Excel(name="请求头(requestHeader)")
    private String requestHeader;

    @Excel(name="请求方式(method)")
    private String method;

    @Excel(name="接口地址(url)")
    private String url;

    @Excel(name="参数输入(inputParams)")
    private String inputParams;

    @Excel(name="期望响应结果(expected)")
    private String expected;

    @Excel(name="提取响应数据（extract）")
    private String extract;

    @Excel(name="数据库校验（dbAssert）")
    private String dbAssert;

    @Override
    public String toString() {
        return "ExcelPojo{" +
                "caseId=" + caseId +
                ", module='" + module + '\'' +
                ", title='" + title + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expected='" + expected + '\'' +
                ", extract='" + extract + '\'' +
                ", dbAssert='" + dbAssert + '\'' +
                '}';
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    public String getExtract() {
        return extract;
    }

    public void setExtract(String extract) {
        this.extract = extract;
    }

    public String getDbAssert() {
        return dbAssert;
    }

    public void setDbAssert(String dbAssert) {
        this.dbAssert = dbAssert;
    }
}
