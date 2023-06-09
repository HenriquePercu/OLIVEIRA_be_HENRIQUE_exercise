package com.ecore.roles.service;

import com.ecore.roles.model.Role;

import java.util.List;
import java.util.UUID;

public interface RolesService {

    Role createRole(Role role);

    Role getRoleById(UUID id);

    List<Role> getRoles();

    Role getRoleByUserIdAndTeamId(UUID userId, UUID teamId);

}
