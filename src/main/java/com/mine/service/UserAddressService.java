package com.mine.service;

import java.util.List;

import com.mine.domain.UserAddress;

public interface UserAddressService {

	public int insertUserAddress(UserAddress address);
	
	public List<UserAddress> queryUserAddress(int offset,int limit);
	
}
