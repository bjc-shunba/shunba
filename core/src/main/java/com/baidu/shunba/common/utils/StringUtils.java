package com.baidu.shunba.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.CollectionUtils;

public class StringUtils extends StringUtil {

	private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StringUtils.class);
	
	/**
	 * 手机号码正则表达式
	 * 
	 *  内地手机号：11位，前缀可能是086-
	 *  香港手机号：8位，前缀可能是+852-
	 *  台湾手机号：09开头+8位，前缀可能是+886-
	 *  澳门手机号:6开头+6位，前缀可能是+853-
	 */
	public static final String REG_MOBILE_PHONE = "((\\+?0?86\\-?)?1[345789]\\d{9})|((\\+?852\\-?)?[569]\\d{3}\\-?\\d{4})|((\\+?886\\-?)?[0]?9\\d{8})|((\\+?853\\-?)?([6]\\d{6}))";

	public static List<Integer> getIdsIntegers(String[] ids) {
		List<Integer> intIds = new ArrayList<Integer>();
		if (ids != null) {
			for (String id : ids) {
				try {
					int intId = ObjectUtils.getIntValue(id.trim());
					if (!intIds.contains(intId)) {
						intIds.add(intId);
					}
				} catch (Exception e) {
				}
			}
		}
		return intIds;
	}
	
	public static List<Double> getValuesDoubles(String[] values) {
		List<Double> dValues = new ArrayList<Double>();
		if (values != null) {
			for (String value : values) {
				try {
					double dValue = ObjectUtils.getDoubleValue(value.trim());
					dValues.add(dValue);
				} catch (Exception e) {
				}
			}
		}
		return dValues;
	}
	
	public static String getIdsString(String ids) {
		try {
			if (ids != null) {
				ids = ids.trim();
				if (ids.length() > 0) {
					if (ids.indexOf(",") != 0) {
						ids = "," + ids;
					}
					if (ids.lastIndexOf(",") != ids.length() - 1) {
						ids = ids + ",";
					}
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static String getIdsString(Collection<? extends Object> ids){
		if(ids == null || ids.size() == 0) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
		int i = 0;
		for (Object id : ids) {
			if (id == null) {
				continue;
			}
			if (i == 0) {
				sb.append(",");
			}
			
			sb.append(id + ",");
			i ++;
		}
		
		if (sb.length() <= 0) {
			return null;
		}
		
		return sb.toString();
	}
	
	public static String getNormalIdsString(Collection<String> ids){
		if(ids == null || ids.size() == 0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		
		for (String id : ids) {
			if  (isEmpty(id)) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		return sb.toString();
	}
	
	public static String getNormalIdsString(String[] ids){
		if(ids == null || ids.length == 0){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		
		for (String id : ids) {
			if  (isEmpty(id)) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append(",");
			}
			sb.append(id);
		}
		return sb.toString();
	}
	
	public static String getIdsString(String[] ids) {
		StringBuilder sb = new StringBuilder();
		try {
			if (ids != null) {
				for (String id : ids) {
					try {
						sb.append("," + id.trim());
					} catch (Exception ex) {
					}
				}
				if (sb.length() > 0) {
					sb.append(",");
				}
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}
	
	public static String getPermissionIdsString(HashSet<String> ids){
		if(ids == null){
			return null;
		}
		StringBuffer sb = new StringBuffer();
		
		String[] result = new String[ids.size()];
		ids.toArray(result);
		if (result != null && result.length > 0) {
			sb.append("'" + result[0] + "'");
			for (int i = 1; i < result.length; i++) {
				String id = result[i];
				sb.append(", '" + id + "'");
			}
		}
		return sb.toString();
	}
	
	public static List<String> getIdsFromIdsString(String idsString) {
		List<String> ids = new ArrayList<String>();
		try {
			String[] idsStrs = idsString.trim().split(",");
			for (String id : idsStrs) {
				if (isNotEmpty(id)) {
					ids.add(id);
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static List<Integer> getIntegerIdsFromIdsString(String idsString) {
		List<Integer> ids = new ArrayList<Integer>();
		try {
			String[] idsStrs = idsString.trim().split(",");
			for (String id : idsStrs) {
				if (isNotEmpty(id)) {
					try {
						ids.add(Integer.valueOf(id));
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static List<Double> getDoubleIdsFromIdsString(String idsString) {
		List<Double> ids = new ArrayList<Double>();
		try {
			String[] idsStrs = idsString.trim().split(",");
			for (String id : idsStrs) {
				if (isNotEmpty(id)) {
					try {
						ids.add(Double.valueOf(id));
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {
		}
		return ids;
	}
	
	public static boolean isStringInvalid(final String str) {
		if (str == null || str.length() == 0 || str.trim().length() == 0) {
			return true;
		}
		String strTrm = str.trim();
		if (strTrm.equalsIgnoreCase("null") || strTrm.equalsIgnoreCase("undefined") || strTrm.equalsIgnoreCase("invalid") ) {
			return true;
		}
		return false;
	}
	
	public static boolean hasEmojiChar(final String str) {
		if (str == null || str.length() == 0) {
			return false;
		}
		
		try {
			int len = str.length();
	        boolean isEmoji = false;
	        for (int i = 0; i < len; i++) {
	            char hs = str.charAt(i);
	            if (0xd800 <= hs && hs <= 0xdbff) {
	                if (str.length() > 1) {
	                    char ls = str.charAt(i + 1);
	                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
	                    if (0x1d000 <= uc && uc <= 0x1f77f) {
	                        return true;
	                    }
	                }
	            } else {
	                // non surrogate
	                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
	                    return true;
	                } else if (0x2B05 <= hs && hs <= 0x2b07) {
	                    return true;
	                } else if (0x2934 <= hs && hs <= 0x2935) {
	                    return true;
	                } else if (0x3297 <= hs && hs <= 0x3299) {
	                    return true;
	                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
	                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
	                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
	                    return true;
	                }
	                if (!isEmoji && str.length() > 1 && i < str.length() - 1) {
	                    char ls = str.charAt(i + 1);
	                    if (ls == 0x20e3) {
	                        return true;
	                    }
	                }
	            }
	        }
		} catch (Exception e) {
		}
		return false;
	}
	
	public static String getNumPart(String s) {
		if(s == null){
			return "";
		}
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(s);
		return matcher.replaceAll("");
	}
	
	public static List<String> createStringsFromInteger(int start, int end) {
		List<String> list = new ArrayList<>(end - start);
		
		for (int i = start; i < end; i++) {
			list.add(String.valueOf(i));
		}
		
		return list;
	}
	
	public static Map<String, Object> createMapFromList(List<String> keyList, List<Object> valueList) {
		Map<String, Object> map = new LinkedHashMap<>();
		
		for (int i = 0; i < keyList.size(); i++) {
			map.put(keyList.get(i), valueList.get(i));
		}
		
		return map;
	}
	
	public static Map<Integer, String> createIndexMapFromList(List<String> keyList) {
		Map<Integer, String> map = new LinkedHashMap<>();
		
		for (int i = 0; i < keyList.size(); i++) {
			map.put(i, keyList.get(i));
		}
		
		return map;
	}

	public static String connectObjectStr(String connectFlag, Object ... objects) {
		StringBuilder sb = new StringBuilder();
		for(Object obj : objects) {
			sb.append(obj.toString()).append(connectFlag);
		}
		return sb.substring(0, sb.length() - 1);
	}
	
	
	public static Map<String, String> simpleParseJsonToMap(String json) {
		Map<String, String> resMap = new HashMap<String, String>();
		try {
			String[] results = json.trim().replaceAll("\\{", "").replaceAll("\\}", "").split(",");
			for (String pRes : results) {
				try {
					String[] kv = pRes.split(":");
					String key = kv[0].replaceAll("\"", "").replaceAll("'", "").trim();
					String value = kv[1].replaceAll("\"", "").replaceAll("'", "").trim();
					resMap.put(key, value);
				} catch (Exception exp) {
				}
			}
		} catch (Exception exp) {

		}
		return resMap;
	}

	/**
	 * [1,2,3] -> (1,2,3)
	 * @param list
	 * @return
	 */
	public static String connectTupleFromListForSql(Collection list) {
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		int len = list.size();
		sb.append('(');
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next());
			sb.append(',');
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(')');
		return sb.toString();
	}

	public static Object[] getNoNullObjArr(Object ... objects) {
		List<Object> list = new ArrayList<>();
		for(Object obj : objects) {
			if(obj != null) {
				list.add(obj);
			}
		}
		return list.toArray();
	}
}
