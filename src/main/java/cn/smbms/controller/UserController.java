package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.pojo.User;
import cn.smbms.service.role.RoleService;
import cn.smbms.service.role.RoleServiceImpl;
import cn.smbms.service.user.UserService;
import cn.smbms.service.user.UserServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther xiaohoo
 * @Date 2018/9/7 9:39
 * @Email 1126457667@qq.com
 */
@Controller
@RequestMapping("/sys/user")
public class UserController {
    private Logger logger = Logger.getLogger(UserController.class);
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    @RequestMapping(value = "/userlist.html")
    public String getUserList(Model model,
                              @RequestParam(value = "queryname", required = false) String queryUserName,
                              @RequestParam(value = "queryUserRole", required = false) String queryUserRole,
                              @RequestParam(value = "pageIndex", required = false) String pageIndex){
        logger.info("getUserList ---- > queryUserName: " + queryUserName);
        logger.info("getUserList ---- > queryUserRole: " + queryUserRole);
        logger.info("getUserList ---- > pageIndex: " + pageIndex);
        //查询用户列表
        int _queryUserRole = 0;
        List<User> userList = null;
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;
        if(queryUserName == null){
            queryUserName = "";
        }
        if(queryUserRole != null && !queryUserRole.equals("")){
            _queryUserRole = Integer.parseInt(queryUserRole);
        }

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                return "redirect:/sys/user/syserror.html";
            }
        }
        //总数量（表）
        int totalCount	= userService.getUserCount(queryUserName,_queryUserRole);
        //总页数
        PageSupport pages=new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);

        int totalPageCount = pages.getTotalPageCount();

        //控制首页和尾页
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }

        userList = userService.getUserList(queryUserName, _queryUserRole, currentPageNo, pageSize);
        model.addAttribute("userList", userList);
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        model.addAttribute("roleList", roleList);
        model.addAttribute("queryUserName", queryUserName);
        model.addAttribute("queryUserRole", queryUserRole);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "userlist";
    }

    @RequestMapping(value="/useradd.html",method=RequestMethod.GET)
    public String addUser(@ModelAttribute("user") User user){
        return "useradd";
    }

    @RequestMapping(value="/useraddsave.html",method=RequestMethod.POST)
    public String addUserSave(User user,HttpSession session,HttpServletRequest request,
                              @RequestParam(value ="a_idPicPath", required = false) MultipartFile attach){
        String idPicPath = null;
        //判断文件是否为空
        if(!attach.isEmpty()){
            String path = request.getSession().getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            logger.info("uploadFile path ============== > "+path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            logger.info("uploadFile oldFileName ============== > "+oldFileName);
            String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
            logger.debug("uploadFile prefix============> " + prefix);
            int filesize = 500000;
            logger.debug("uploadFile size============> " + attach.getSize());
            if(attach.getSize() >  filesize){//上传大小不得超过 500k
                request.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                return "useradd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Personal.jpg";
                logger.debug("new fileName======== " + attach.getName());
                File targetFile = new File(path, fileName);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }
                //保存
                try {
                    attach.transferTo(targetFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("uploadFileError", " * 上传失败！");
                    return "useradd";
                }
                idPicPath = path+File.separator+fileName;
            }else{
                request.setAttribute("uploadFileError", " * 上传图片格式不正确");
                return "useradd";
            }
        }
        user.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        user.setCreationDate(new Date());
        user.setIdPicPath(idPicPath);
        if(userService.add(user)){
            return "redirect:/sys/user/userlist.html";
        }
        return "useradd";
    }

    // 异步请求用户角色列表
    @RequestMapping(value = "/getRoleList")
    @ResponseBody
    public Object getRoleList(){
        List<Role> roleList = roleService.getRoleList();
        return JSONArray.toJSONString(roleList);
    }

    // 跳转到修改用户页面
    @RequestMapping(value = "/usermodify.html")
    public String getUserById(@RequestParam String uid, Model model){
        logger.info("----->uid:" + uid);
        User user = userService.getUserById(uid);
        model.addAttribute(user);
        return "usermodify";
    }

    // 修改用户
    @RequestMapping(value = "/usermodifysave.html")
    public String modifyUserSave(User user, HttpSession session){
        logger.debug("----------->modifyUser uid:" + user.getId());
        user.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        user.setModifyDate(new Date());
        if (userService.modify(user) == 1){
            return "redirect:/sys/user/userlist.html";
        }
        return "usermodify";
    }

    // 用户详情
    @RequestMapping(value = "/view/{id}")
    public String view(@PathVariable String id, Model model){
        logger.debug("user view id ---->" + id);
        User user = userService.getUserById(id);
        model.addAttribute(user);
        return "userview";
    }

    // 异步请求不要加 .html 因为加上后springMVC会以html格式响应信息
    @RequestMapping(value="/view"/*, produces = {"application/json;charset=UTF-8"}*/)
    @ResponseBody
    public User view(@RequestParam String id){
        logger.debug("view id===================== "+id);
        User user = new User();
        try {
            user = userService.getUserById(id);
        } catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

    // 删除用户
    @RequestMapping(value = "/deleteuser")
    @ResponseBody
    public Object delUser(@RequestParam String userid) {
        User user = userService.getUserById(userid);
        Integer delId = 0;
        try{
            delId = Integer.parseInt(userid);
        }catch (Exception e) {
            delId = 0;
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(delId <= 0){
            resultMap.put("delResult", "notexist");
        }else if(userService.deleteUserById(delId.toString()) != 0){
            resultMap.put("delResult", "true");
        }else{
            resultMap.put("delResult", "false");
        }
        //把resultMap转换成json对象输出
        return JSONArray.toJSONString(resultMap);
    }

    // 判断用户编号是否存在
    @RequestMapping(value="/ucexist")
    @ResponseBody
    public Object userCodeIsExit(@RequestParam String userCode){
        logger.debug("userCodeIsExit userCode===================== "+userCode);
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode", "exist");
        }else{
            User user = userService.selectUserCodeExist(userCode);
            if(null != user)
                resultMap.put("userCode", "exist");
            else
                resultMap.put("userCode", "noexist");
        }
        return JSONArray.toJSONString(resultMap);
    }

    //跳转至页面修改页面
    @RequestMapping(value = "/pwdmodify.html")
    public String pwdModify(){
        return "pwdmodify";
    }

    //异步验证密码
    @RequestMapping(value = "/getPwdByUserId")
    @ResponseBody
    public String getPwdByUserId(HttpSession session, @RequestParam("oldpassword") String oldpassword){
        String pwd = ((User) session.getAttribute(Constants.USER_SESSION)).getUserPassword();
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (pwd.equals(oldpassword)){
            resultMap.put("result","true");
        } else {
            resultMap.put("result", "false");
        }
        return JSONArray.toJSONString(resultMap);
    }

    // 保存密码
    @RequestMapping("/pwdsave.html")
    public String pwdSave(@RequestParam(value = "rnewpassword") String newpassword, HttpSession session){
        Integer id = ((User) session.getAttribute(Constants.USER_SESSION)).getId();
        logger.debug("pwdSave id ---------> " + id);
        if (userService.updatePwd(id, newpassword) == 1){
            return "redirect:/login.html";
        }
        return "pwdmodify";
    }

    // 错误处理页面
    @RequestMapping(value="/syserror.html")
    public String sysError(){
        return "syserror";
    }

}
