package com.baidu.shunba.core.web.bean;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baidu.shunba.common.utils.StringUtils;
import com.google.gson.Gson;

public class MessageJson implements Serializable {

	private static final long serialVersionUID = -5755057103197751458L;
	
	public static final int RESULT_CODE_OK = 1;
	public static final int RESULT_CODE_SYSTEM_ERROR = 0;
	public static final int RESULT_CODE_UNSUPPORTED_METHOD = -1;
	public static final int RESULT_CODE_REQUESTED_TIME_IS_EMPTY = -2;
	public static final int RESULT_CODE_INVALID_TIME = -3;
	public static final int RESULT_CODE_REQUESTED_AUTUTOKEN_IS_EMPTY = -4;
	public static final int RESULT_CODE_INVALID_AUTHTOKEN = -5;
	public static final int RESULT_CODE_INVALID_PARAMETERS = -6;

	public static final Map<Integer, String> RESULT_MAP = new LinkedHashMap<Integer, String>() {
		private static final long serialVersionUID = -6825577953107746289L;
		{
			put(RESULT_CODE_OK, "request successed.");
			put(RESULT_CODE_SYSTEM_ERROR, "System error.");
			put(RESULT_CODE_UNSUPPORTED_METHOD, "Unsupported method.");
			put(RESULT_CODE_REQUESTED_TIME_IS_EMPTY, "Requested time is empty.");
			put(RESULT_CODE_INVALID_TIME, "invalid time.");
			put(RESULT_CODE_REQUESTED_AUTUTOKEN_IS_EMPTY, "Requested data is Illegal.");
			put(RESULT_CODE_INVALID_AUTHTOKEN, "Invalid auth token.");
			put(RESULT_CODE_INVALID_PARAMETERS, "Invalid parameters.");
		}
	};
	
	public static MessageJson newInstanceWithError(Throwable error) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = -1;
		if (StringUtils.isNotEmpty(error.getLocalizedMessage())) {
			msgJson.message = error.getLocalizedMessage();
		} else  {
			msgJson.message = error.toString();
		}
		return msgJson;
	}
	
	public static MessageJson newInstanceWithError(String errMsg) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = -1;
		msgJson.message = errMsg;
		return msgJson;
	}
	
	public static MessageJson newInstanceWithError(int code, String errMsg, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = - Math.abs(code);
		msgJson.message = errMsg;
		msgJson.setResults(result);
		return msgJson;
	}
	
	public static MessageJson newInstanceWithError(String errMsg, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = -1;
		msgJson.message = errMsg;
		msgJson.setResults(result);
		return msgJson;
	}
	
	public static MessageJson newInstanceWithCode(int code) {
		MessageJson msgJson = new MessageJson();
		msgJson.setCode(code);
		return msgJson;
	}
	
	public static MessageJson newInstance(Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.setResults(result);
		return msgJson;
	}
	
	public static MessageJson newInstanceWithAddition(Object result, Object addition) {
		MessageJson msgJson = new MessageJson();
		msgJson.setResults(result);
		msgJson.addition = addition;
		return msgJson;
	}
	
	public static MessageJson newInstance(int code, String message, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.code = code;
		msgJson.message = message;
		msgJson.setResults(result);
		return msgJson;
	}
	
	public static MessageJson newInstance(String message, Object result) {
		MessageJson msgJson = new MessageJson();
		msgJson.message = message;
		msgJson.setResults(result);
		return msgJson;
	}
	
	public static MessageJson newInstanceWithMessage(String message) {
		MessageJson msgJson = new MessageJson();
		msgJson.message = message;
		return msgJson;
	}
	
	
	public static MessageJson newPageInstance(List<?> result) {
		MessageJson msgJson = new MessageJson();
		msgJson.results = result;
		msgJson.curPage = 1;
		msgJson.pageSize = (result != null ? result.size() : 0);
		msgJson.totalSize = msgJson.pageSize;
		return msgJson;
	}
	
	public static MessageJson newPageInstance(Object result, int curPage, int pageSize, int totalSize) {
		MessageJson msgJson = new MessageJson();
		msgJson.results = result;
		msgJson.curPage = curPage;
		msgJson.pageSize = pageSize;
		msgJson.totalSize = totalSize;
		return msgJson;
	}

	private int code;

	private String message;

	private Object results;
	
	public Object addition;
	
	
	public int curPage = 1;
	public int pageSize = 0;
	public int totalSize = 1;

	public MessageJson() {
		setCode(RESULT_CODE_OK);
	}

	public int getCode() {
		return code;
	}

	public boolean isSuccess() {
		return code > 0;
	}
	
	public void appendMessage(String appendMessage) {
		if (this.message == null) {
			this.message = "";
		}
		this.message += ": " + appendMessage;
	}
	
	public void appendMessage(Throwable e) {
		if (this.message == null) {
			this.message = "";
		}
		if (e != null) {
			if (e.getLocalizedMessage() != null && e.getLocalizedMessage().length() > 0) {
				this.message += ": " + e.getLocalizedMessage();
			} else {
				this.message += ": " + e.getMessage();
			}
		}
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
		if (RESULT_MAP.containsKey(code)) {
			message = RESULT_MAP.get(code);
		}
	}

	public <T extends Object> T getResults() {
		return (T) results;
	}

	public void setResults(Object results) {
		if (results instanceof Collection) {
			Collection c = (Collection) results;
			this.pageSize = c.size();
			this.totalSize = c.size();
		}
		this.results = results;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
