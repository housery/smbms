package cn.smbms.service.role;

import java.util.List;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    @Qualifier("roleMapper")
    private RoleMapper roleMapper;

    @Override
    public List<Role> getRoleList() {

        return roleMapper.getRoleList();
    }

}
