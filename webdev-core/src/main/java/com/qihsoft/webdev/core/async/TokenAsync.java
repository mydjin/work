package com.qihsoft.webdev.core.async;

import com.qihsoft.webdev.core.service.impl.SysUserTokenService;
import com.qihsoft.webdev.core.model.SysUserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Qihsoft on 2017/10/3
 */
@Component
public class TokenAsync {
    @Autowired
    private SysUserTokenService userTokenService;

    @Async
    public void updateToken(SysUserToken ut){
        userTokenService.createUserToken(ut);
    }
}
