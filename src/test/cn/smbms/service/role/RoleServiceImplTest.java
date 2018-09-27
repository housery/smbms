package cn.smbms.service.role;

import cn.smbms.dao.role.RoleMapper;
import cn.smbms.pojo.Role;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther xiaohoo
 * @Date 2018/9/3 17:33
 * @Email 1126457667@qq.com
 */
public class RoleServiceImplTest {
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
    RoleMapper roleMapper = (RoleMapper) ctx.getBean("roleMapper");
    Logger logger = Logger.getLogger(RoleServiceImplTest.class);

    @Test
    public void getRoleList() {
        List<Role> roleList = roleMapper.getRoleList();
        logger.info(roleList.size());
        for (Role role: roleList) {
            logger.info(role.getId() + "\t" + role.getRoleName() + "\t" + role.getCreatedBy());
        }
    }
}