package cn.smbms.dao.user;

import cn.smbms.pojo.User;
import cn.smbms.service.user.UserService;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther xiaohoo
 * @Date 2018/8/30 15:36
 * @Email 1126457667@qq.com
 */
public class UserMapperImplTest {
    private Logger logger = Logger.getLogger(UserMapperImplTest.class);
    @Test
    public void add() {
    }

    @Test
    public void getLoginUser() {
    }

    @Test
    public void getUserList() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
        for (String name: ctx.getBeanDefinitionNames()) {
            logger.info(name);
        }
        UserService userService = (UserService) ctx.getBean("userService");
        List<User> userList = userService.getUserList("赵", 3, 1, 5);
        for(User user: userList){
            logger.debug("getUserList userCode: " + user.getUserCode() +
                    " and userName: " + user.getUserName() +
                    " and userRole: " + user.getUserRole() +
                    " and userRoleName: " + user.getUserRoleName() +
                    " and age: " + user.getAge() +
                    " and address: " + user.getAddress());
        }
    }

    @Test
    public void deleteUserById() {
    }

    @Test
    public void getUserById() {
    }

    @Test
    public void modify() {
    }

    @Test
    public void updatePwd() {
    }

    @Test
    public void getUserCount() {
    }
}