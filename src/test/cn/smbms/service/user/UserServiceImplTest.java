package cn.smbms.service.user;

import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillServiceImplTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther xiaohoo
 * @Date 2018/8/31 15:57
 * @Email 1126457667@qq.com
 */
public class UserServiceImplTest {
    private Logger logger = Logger.getLogger(BillServiceImplTest.class);
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
    UserService userService= (UserService) ctx.getBean("userService");
    /**
     * 事务管理测试
     */
    @Test
    public void addNewUser() throws ParseException {
        User user = new User();
        user.setUserCode("test001");
        user.setUserName("测试用户001");
        user.setUserPassword("1234567");
        Date birthday =new SimpleDateFormat("yyyy-MM-dd").parse("1984-12-12");
        user.setBirthday(birthday);
        user.setCreationDate(new Date());
        user.setAddress("地址测试");
        user.setGender(1);
        user.setPhone("13688783697");
        user.setUserRole(1);
        user.setCreatedBy(1);
        user.setCreationDate(new Date());

        boolean result = userService.addNewUser(user);
        logger.debug(result);
    }

    @Test
    public void selectUserCodeExistTest (){
        User user = userService.selectUserCodeExist("yangguo");
        logger.info(user.getUserPassword() + "\t" + user.getUserCode());
    }
    @Test
    public void getUserCount(){
        int count = userService.getUserCount("赵", 3);
        logger.info("==>" + count);
    }

    @Test
    public void deleteUserByIdTest(){
        int count = userService.deleteUserById("18");
        logger.info("delete success " + count);
    }

    @Test
    public void getUserByIdTest(){
        User user = userService.getUserById("1");
        logger.info(user.getId() + "\t" + user.getUserName());
    }

    @Test
    public void modifyTest(){
        User user = new User();
        // 修改id 17信息
        user.setId(17);
        user.setUserName("Jack");
        user.setPhone("88888888");
        int count = userService.modify(user);
        logger.info(count);
    }

    @Test
    public void updatePwdTest(){
        int count = userService.updatePwd(17, "666666");
        logger.info(count);
    }

    @Test
    public void loginTest(){
        User user = userService.login("admin", "1234567");
        logger.info(user.getUserName());
    }

    @Test
    public void getUserListTest(){
        List<User> userList =  userService.getUserList("", 0, 1, 5);
        logger.info(userList.size());
    }
}