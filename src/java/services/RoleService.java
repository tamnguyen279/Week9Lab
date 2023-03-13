package services;

import dataaccess.RoleDB;
import java.util.List;
import models.Role;

/**
 *
 * @author nguye
 */
public class RoleService {

    public List<Role> getAll() throws Exception {
        RoleDB roleDB = new RoleDB();
        List<Role> roles = roleDB.getAll();
        return roles;
    }

    public Role get(int id) throws Exception {
        RoleDB roleDB = new RoleDB();
        Role role = roleDB.get(id);
        return role;
    }
}
