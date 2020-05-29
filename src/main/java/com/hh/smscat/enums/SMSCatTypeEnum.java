package com.hh.smscat.enums;

/**
 * 2019/7/9 20:59
 *
 * @author owen pan
 */
public enum SMSCatTypeEnum {
    MOTUOTING("MoTuoTing"),
    HANGBIAO("HangBiao"),
    WEIZHI("WeiZhi")
    ;
    private String type;

    SMSCatTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }}
