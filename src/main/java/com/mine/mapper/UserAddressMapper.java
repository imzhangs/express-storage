package com.mine.mapper;

import java.util.List;

import com.mine.domain.UserAddress;

public interface UserAddressMapper {

	public int insertUserAddress(UserAddress address);
	
	public List<UserAddress> queryUserAddress(int offset,int limit);
	
}
