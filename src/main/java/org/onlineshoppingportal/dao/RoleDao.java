package org.onlineshoppingportal.dao;

import org.onlineshoppingportal.entity.Role;

public interface RoleDao {

	public Role findRoleByRoleName(String role);
}
