package com.example.util;


/**
 * 返回数据
 *
 */
public class Result{
    private static final long serialVersionUID = 1L;
    private String code = Constants.RTN_CODE_SUCCESS;
    private String message = "success";
    private Object result;
    private String status = Constants.RTN_STATUS_SUCCESS;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static Result ok(){
        Result rtnData = new Result();
        rtnData.setCode(Constants.RTN_CODE_SUCCESS);
        rtnData.setStatus(Constants.RTN_STATUS_SUCCESS);
        rtnData.setResult("");
        return rtnData;
    }


    public static Result ok(Object result){
        Result rtnData = new Result();
        rtnData.setCode(Constants.RTN_CODE_SUCCESS);
        rtnData.setStatus(Constants.RTN_STATUS_SUCCESS);
        rtnData.setResult(result);
        return rtnData;
    }

    public static Result fail(Object result){
        return fail(result, null);
    }

    public static Result fail(Object result, String message){
        return fail(result, Constants.RTN_CODE_FAIL, message);
    }

    public static Result fail(Object result, String code, String message){
        Result rtnData = new Result();
        rtnData.setCode(code);
        rtnData.setMessage(message);
        rtnData.setStatus(Constants.RTN_STATUS_ERROR);
        rtnData.setResult(result);
        return rtnData;
    }

}
