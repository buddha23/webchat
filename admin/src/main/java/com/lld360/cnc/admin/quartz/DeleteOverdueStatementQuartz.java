package com.lld360.cnc.admin.quartz;

import org.springframework.beans.factory.annotation.Autowired;

import com.lld360.cnc.repository.UserStatementTempDao;

public class DeleteOverdueStatementQuartz {
	@Autowired
	private UserStatementTempDao userStatementTempDao;
	
	public void doJob(){
		userStatementTempDao.deleteOverdue();
	}
}
