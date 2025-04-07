package com.dihuan.common.localThread;


import com.dihuan.common.utils.jsonWebTokenUtils.TokenInfo;

public class TokenInfoHolder {
    public static ThreadLocal<TokenInfo> threadLocal = new ThreadLocal<>();

    public static void setLoginAdminUser(TokenInfo tokenInfo){
        threadLocal.set(tokenInfo);
    }

    public static TokenInfo getTokenInfo(){
        return threadLocal.get();
    }

    public static void clearTokenInfo(){
        threadLocal.remove();
    }
}
