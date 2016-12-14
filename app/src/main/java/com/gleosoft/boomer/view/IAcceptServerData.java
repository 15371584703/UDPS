package com.gleosoft.boomer.view;

/**
 * Created by hohn on 2016/12/13.
 */

public interface IAcceptServerData {
    public static final String SERVERIP = "255.255.255.255"; // 广播地址
    public static final int SERVERPORT = 9000; // 端口号

    public void acceptUdpData(String data);
}

