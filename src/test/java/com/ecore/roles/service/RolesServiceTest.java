package com.ecore.roles.service;

import com.ecore.roles.exception.ResourceNotFoundException;
import com.ecore.roles.model.Role;
import com.ecore.roles.repository.MembershipRepository;
import com.ecore.roles.repository.RoleRepository;
import com.ecore.roles.service.impl.RolesServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.ecore.roles.utils.TestData.*;
import static java.lang.String.format;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolesServiceTest {

    @InjectMocks
    private RolesServiceImpl rolesService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private MembershipsService membershipsService;

    @Test
    public void shouldCreateRole() {
        Role developerRole = DEVELOPER_ROLE();
        when(roleRepository.save(developerRole)).thenReturn(developerRole);

        Role role = rolesService.createRole(developerRole);

        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToCreateRoleWhenRoleIsNull() {
        assertThrows(NullPointerException.class,
                () -> rolesService.createRole(null));
    }

    @Test
    public void shouldReturnRoleWhenRoleIdExists() {
        Role developerRole = DEVELOPER_ROLE();
        when(roleRepository.findById(developerRole.getId())).thenReturn(of(developerRole));

        Role role = rolesService.getRoleById(developerRole.getId());

        assertNotNull(role);
        assertEquals(developerRole, role);
    }

    @Test
    public void shouldFailToGetRoleWhenRoleIdDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoleById(UUID_1));

        assertEquals(format("Role %s not found", UUID_1), exception.getMessage());
    }

    @Test
    public void shouldReturnRoleWhenSearchByRoleIdAndUserId() {
        when(membershipRepository.findByUserIdAndTeamId(GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID))
                .thenReturn(of(DEFAULT_MEMBERSHIP()));

        Role role = rolesService.getRoleByUserIdAndTeamId(GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID);

        assertNotNull(role);
        assertEquals(role.getId(), DEVELOPER_ROLE().getId());
        assertEquals(role.getName(), DEVELOPER_ROLE().getName());
    }

    @Test
    public void shouldFailToReturnRoleWhenTeamDoesNotExist() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> rolesService.getRoleByUserIdAndTeamId(GIANNI_USER_UUID, ORDINARY_CORAL_LYNX_TEAM_UUID));

        assertEquals(format("Role not found for %s, %s", ORDINARY_CORAL_LYNX_TEAM_UUID, GIANNI_USER_UUID), exception.getMessage());
    }
}
