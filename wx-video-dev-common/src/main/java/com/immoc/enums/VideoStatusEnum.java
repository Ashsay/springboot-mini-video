package com.immoc.enums;

public enum VideoStatusEnum {
	
	SUCCESS(1),    //成功
	FORBID(2);	   //禁止播放
	
	public final int value;
	
	VideoStatusEnum(int value){
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
}
