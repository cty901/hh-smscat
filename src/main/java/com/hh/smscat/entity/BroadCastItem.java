package com.hh.smscat.entity;

/**
 * 2019/7/11 14:16
 *
 * @author owen pan
 */
public class BroadCastItem {
    private Boolean enable;
    private String url;

    public BroadCastItem(Boolean enable, String url) {
        this.enable = enable;
        this.url = url;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "BroadCastItem{" +
                "enable=" + enable +
                ", url='" + url + '\'' +
                '}';
    }
}
