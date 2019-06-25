package com.xtaller.party.core.async;

import com.xtaller.party.core.model.SysUserToken;
import com.xtaller.party.core.service.impl.SysUserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Taller on 2017/10/3
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
