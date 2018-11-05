package com.platform.jd.entity;

import java.util.Date;

public class JdResponseOrderTrackInfoOrderTrackEntity {
	private String content; //配送内容
	private Date msgTime; //操作时间
	private String operator; //操作人
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(Date msgTime) {
		this.msgTime = msgTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
