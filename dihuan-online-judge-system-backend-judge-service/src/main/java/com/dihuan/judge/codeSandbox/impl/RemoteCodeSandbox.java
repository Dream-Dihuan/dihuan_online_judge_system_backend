package com.dihuan.judge.codeSandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.dihuan.common.exception.DihuanException;
import com.dihuan.common.result.ResultCodeEnum;
import com.dihuan.common.utils.jsonWebTokenUtils.JwtUtils;
import com.dihuan.judge.codeSandbox.CodeSandbox;
import com.dihuan.model.codeSandbox.ExecuteCodeRequest;
import com.dihuan.model.codeSandbox.ExecuteCodeResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

public class RemoteCodeSandbox implements CodeSandbox {

    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String url = "http://localhost:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header("access_token",null)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new DihuanException(ResultCodeEnum.FAIL, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
