package com.dihuan.judge.codeSandbox;

import com.dihuan.judge.codeSandbox.impl.LocalCodeSandbox;
import com.dihuan.judge.codeSandbox.impl.RemoteCodeSandbox;

public class CodeSandboxFactory {

    public static CodeSandbox newInstance(String type) {
        switch (type){
            case "remote":
                return new RemoteCodeSandbox();
            case "local":
                return new LocalCodeSandbox();
            default:
                return new RemoteCodeSandbox();
        }
    }
}
