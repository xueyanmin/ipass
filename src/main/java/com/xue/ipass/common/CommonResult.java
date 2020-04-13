package com.xue.ipass.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {

    private String status;
    private String message;
    private Object data;

    /**
     * 短信验证码发送成功
     */
    public CommonResult success(String status, String message, Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(status);
        commonResult.setMessage(message);
        commonResult.setData(data);
        return commonResult;
    }

    /**
     * 短息验证码发送失败
     */
    public CommonResult failed(String status, String message, Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus("104");
        commonResult.setMessage(message);
        commonResult.setData(data);
        return commonResult;
    }

    public CommonResult success( Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus("100");
        commonResult.setMessage("查询成功");
        commonResult.setData(data);
        return commonResult;
    }
    public CommonResult success(String message, Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus("104");
        commonResult.setMessage(message);
        commonResult.setData(data);
        return commonResult;
    }


    public CommonResult failed(Object data) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus("104");
        commonResult.setMessage("查询失败");
        commonResult.setData(data);
        return commonResult;
    }

    public CommonResult failed() {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus("104");
        commonResult.setMessage("查询失败");
        commonResult.setData(null);
        return commonResult;
    }
}