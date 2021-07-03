package com.push.sdk.consts;

/**
 * 常量
 */
public interface KimConstant {

	byte DATA_TYPE_PONG  = 0;
	byte DATA_TYPE_PING  = 1;
	byte DATA_TYPE_MESSAGE = 2;
	byte DATA_TYPE_SENT = 3;
	byte DATA_TYPE_REPLY = 4;
	int PONG_TIME_OUT_COUNT = 3;
	String CLIENT_CONNECT_CLOSED = "client_closed";

}
