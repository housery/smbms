package cn.smbms.dao.user;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    /**
     * 增加用户信息
     *
     * @param user
     * @return
     * @throws Exception
     */
    public int add(User user);

    /**
     * 通过userCode获取User
     *
     * @param userCode 用户代码
     * @return
     */
    public User getLoginUser(String userCode);

    /**
     * 获取用户列表
     * @return 用户列表
     */
    public List<User> getUserList(@Param("userName") String userName, @Param("roleId")Integer roleId,
                                  @Param("from")Integer from, @Param("pageSize")Integer pageSize);

    /**
     * 通过条件查询-用户表记录数
     *
     * @param userName
     * @param userRole
     * @return
     * @throws Exception
     */
    public int getUserCount(@Param("userName") String userName, @Param("userRole") int userRole);

    /**
     * 通过userId删除user
     *
     * @param delId
     * @return
     * @throws Exception
     */
    public int deleteUserById(String delId);


    /**
     * 通过userId获取user
     *
     * @param id
     * @return
     * @throws Exception
     */
    public User getUserById(String id);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     * @throws Exception
     */
    public int modify(User user);


    /**
     * 修改当前用户密码
     *
     * @param id
     * @param pwd
     * @return
     * @throws Exception
     */
    public int updatePwd(@Param("id") int id, @Param("userPassword") String pwd);


}
