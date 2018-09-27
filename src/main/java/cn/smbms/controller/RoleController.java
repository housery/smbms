package cn.smbms.controller;

import cn.smbms.pojo.Role;
import cn.smbms.service.role.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther xiaohoo
 * @Date 2018/9/11 17:12
 * @Email 1126457667@qq.com
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @RequestMapping("/rolelist.html")
    public String getRoleList(Model model){
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute(roleList);
        return "rolelist";
    }
}
