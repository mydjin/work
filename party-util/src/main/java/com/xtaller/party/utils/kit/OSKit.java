package com.xtaller.party.utils.kit;

import java.util.Properties;

/**
 * Created by Taller on 2017/11/11
 */
public class OSKit {
    public static Boolean isLinux(){
        Properties props = System.getProperties();
        String os = props.getProperty("os.name").toLowerCase();
        if(os.indexOf("windows") < 0){
            return true;
        }else {
            return false;
        }
    }
}
