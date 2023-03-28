package account.service;

import account.domain.Role;
import account.repository.RolesRepository;
import account.security.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService {
    private RolesRepository rolesRepository;

    @Autowired
    public void setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Role findRole(String role) {
        return rolesRepository.findByRole(RoleType.valueOf(role))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    public Role save(Role role) {
        return rolesRepository.save(role);
    }

    public Map<String, Role> findAll() {
        List<Role> roles = rolesRepository.findAll();
        Map<String, Role> mappedRoles = new HashMap<>();
        for (Role role: roles) {
            mappedRoles.put(role.toString().substring("ROLE_".length()), role);
        }
        return mappedRoles;
    }
}
