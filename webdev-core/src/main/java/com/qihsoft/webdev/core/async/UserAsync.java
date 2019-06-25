package com.qihsoft.webdev.core.async;

import com.qihsoft.webdev.core.service.impl.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Qihsoft on 2017/11/21
 */
@Component
public class UserAsync {
    @Autowired
    private SysUserService userService;


}
