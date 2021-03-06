package com.systemDemo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.systemDemo.mapper.TbUsersMapper;
import com.systemDemo.pojo.TbUsers;
import com.systemDemo.pojo.TbUsersExample;
import com.systemDemo.pojo.TbUsersExample.Criteria;
import com.systemDemo.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private TbUsersMapper tbUsersMapper;

	@Override
	public TbUsers selUserByCodeAndStatus(String eCode,String status) {
		TbUsersExample example=new TbUsersExample();
		Criteria criteria = example.createCriteria();
		criteria.andECodeEqualTo(eCode);
		criteria.andStatusEqualTo(status);
		List<TbUsers> users = tbUsersMapper.selectByExample(example);
		if(users!=null&&users.size()>0){
			return users.get(0);
		}
		return null;
	}

	@Override
	public void updUserStatus(TbUsers user) {
		tbUsersMapper.updateByPrimaryKey(user);
	}

}
