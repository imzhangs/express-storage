package com.mine.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mine.domain.UserAddress;
import com.mine.mapper.UserAddressMapper;
import com.mine.service.UserAddressService;

@Service
public class UserAddressServiceImp implements UserAddressService {

	@Autowired
	UserAddressMapper mapper;
	
	@Override
	public int insertUserAddress(UserAddress address) {
		// TODO Auto-generated method stub
		return mapper.insertUserAddress(address);
	}

	@Override
	public List<UserAddress> queryUserAddress(int offset, int limit) {
		// TODO Auto-generated method stub
		return mapper.queryUserAddress(offset, limit);
	}

}
