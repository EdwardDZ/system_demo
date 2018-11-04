package com.systemDemo.service;
import java.util.Date;

import com.systemDemo.pojo.TbLog;
import com.systemDemo.pojo.UserSearch;
import com.systemDemo.util.ResultUtil;

public interface LogService {
	//添加日志
	public void insLog(TbLog log);
	
	//获取日志列表
	ResultUtil selLogList(Integer page, Integer limit,UserSearch search);

	//删除指定日期以前的日志
	public int delLogsByDate(Date date);
}
