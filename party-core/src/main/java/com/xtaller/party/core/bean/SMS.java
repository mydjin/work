package com.xtaller.party.core.bean;

/**
 * Created by Taller on 2017/12/8
 */
public enum SMS {
    Reg(0), Login(63396), Forget(63397), YELLO(0);
    private Integer templId;
    private SMS(Integer templId){
        this.templId = templId;
    }

    @Override
    public String toString() {
        return this.templId+"";
    }
}
