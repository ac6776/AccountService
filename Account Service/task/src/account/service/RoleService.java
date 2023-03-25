package account.service;

import account.domain.Role;
import account.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RoleService {
    private RolesRepository rolesRepository;

    @Autowired
    public void setRolesRepository(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Role findRole(String role) {
        return rolesRepository.findByRole(role)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));
    }

    public Role save(Role role) {
        return rolesRepository.save(role);
    }
}
