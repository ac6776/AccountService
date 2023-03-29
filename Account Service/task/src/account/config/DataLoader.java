package account.config;

import account.domain.Role;
import account.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final RoleService roleService;

    @Autowired
    public DataLoader(RoleService roleService) {
        this.roleService = roleService;
        createRoles();
    }

    private void createRoles() {
        try {
            roleService.save(new Role("ROLE_ADMINISTRATOR"));
            roleService.save(new Role("ROLE_USER"));
            roleService.save(new Role("ROLE_ACCOUNTANT"));
            roleService.save(new Role("ROLE_AUDITOR"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
