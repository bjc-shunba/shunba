package com.baidu.shunba.common.utils;

import java.util.Map;

import com.baidu.shunba.core.vo.Pager;
/**
 *类描述：分页工具类
 *@date： 日期：2012-12-7 时间：上午10:19:14
 *@version 1.0
 */
public class PagerUtil {

	public static int getOffset(int rowCounts, int curPageNO, int pageSize) {
		int offset = 0;
		try {
			if (curPageNO > (int) Math.ceil((double) rowCounts / pageSize))
				curPageNO = (int) Math.ceil((double) rowCounts / pageSize);
			// 得到第几页
			if (curPageNO <= 1)
				curPageNO = 1;
			// 得到offset
			offset = (curPageNO - 1) * pageSize;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offset;
	}

	public static int getcurPageNo(int rowCounts, int curPageNO, int pageSize) {
		try {
			// 得到第几页
			if (curPageNO > (int) Math.ceil((double) rowCounts / pageSize))
				curPageNO = (int) Math.ceil((double) rowCounts / pageSize);
			if (curPageNO <= 1)
				curPageNO = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return curPageNO;
	}
}