package com.datingapp.service;

import java.util.List;
import com.datingapp.dto.AdminDto;

public interface AdminService {
	List<AdminDto> getUsersWithRoles();
	void editRoles(String username, String roles);
}
