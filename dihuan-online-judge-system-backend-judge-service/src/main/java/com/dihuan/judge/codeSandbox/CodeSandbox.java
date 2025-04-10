package com.dihuan.judge.codeSandbox;

import com.dihuan.model.codeSandbox.ExecuteCodeRequest;
import com.dihuan.model.codeSandbox.ExecuteCodeResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface CodeSandbox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
