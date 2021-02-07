package com.baidu.shunba.core.vo;

import java.util.Map;
/**
 * 标签生成类(不使用分页标签)
 * @author jeecg
 * @version1.0
 */
public class Pager {
	private int curPageNO = 1; // 当前页
	private int pageSize; // 每页显示的记录数
	private int rowsCount; // 记录行数
	private int pageCount; // 页数
	private Map<String, Object> map;// 封装查询条件

	/**
	 * @param allCount
	 *            记录行数
	 * @param offset
	 *            记录开始数目
	 * @param pageSize
	 *            每页显示的记录数
	 */
	public Pager(int allCount,int curPagerNo, int pageSize, Map<String, Object> map) {
		this.curPageNO = curPagerNo;
		this.pageSize = pageSize;
		this.rowsCount = allCount;
		this.map = map;
		this.pageCount = (int) Math.ceil((double) allCount / pageSize);
	}

	public Pager() {
	}
	// getPageSize：返回分页大小
	public int getPageSize() {
		return pageSize;
	}

	// getRowsCount：返回总记录行数
	public int getRowsCount() {
		return rowsCount;
	}

	// getPageCount：返回总页数
	public int getPageCount() {
		return pageCount;
	}

	// 第一页
	public int first() {
		return 1;
	}

	// 最后一页
	public int last() {
		return pageCount;
	}

	// 上一页
	public int previous() {
		return (curPageNO - 1 < 1) ? 1 : curPageNO - 1;
	}

	// 下一页
	public int next() {
		return (curPageNO + 1 > pageCount) ? pageCount : curPageNO + 1;
	}

	// 第一页
	public boolean isFirst() {
		return (curPageNO == 1) ? true : false;
	}

	// 最后一页
	public boolean isLast() {
		return (curPageNO == pageCount) ? true : false;
	}
	public String toString() {
		return "Pager的值为 " + " curPageNO = " + curPageNO + " limit = " + pageSize + " rowsCount = " + rowsCount + " pageCount = " + pageCount;
	}

}
