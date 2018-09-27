package cn.smbms.controller;

import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther xiaohoo
 * @Date 2018/9/11 10:27
 * @Email 1126457667@qq.com
 */
@Controller
@RequestMapping("/sys/provider")
public class ProviderController {
    private Logger logger = Logger.getLogger(UserController.class);
    @Resource
    private ProviderService providerService;

    // 查询获取用户列表
    @RequestMapping("/providerlist.html")
    public String getProviderList(Model model,
                                  @RequestParam(value = "queryProName", required = false) String queryProName,
                                  @RequestParam(value = "queryProCode", required = false) String queryProCode,
                                  @RequestParam(value = "pageIndex", required = false) String pageIndex){

        logger.info("----> queryProName:" + queryProName);
        logger.info("----> queryProCode:" + queryProCode);
        logger.info("----> pageIndex:" + pageIndex);
        if (StringUtils.isNullOrEmpty(queryProName)) {
            queryProName = "";
        }
        if (StringUtils.isNullOrEmpty(queryProCode)) {
            queryProCode = "";
        }
        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;

        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                return "redirect:/sys/user/syserror.html";
            }
        }
        //总数量（表）
        int totalCount	= providerService.getProCount(queryProName, queryProCode);

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

        List<Provider> providerList = providerService.getProviderList(queryProName, queryProCode, currentPageNo, pageSize);
        model.addAttribute("providerList", providerList);
        model.addAttribute("queryProName", queryProName);
        model.addAttribute("queryProCode", queryProCode);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "providerlist";
    }

    @RequestMapping(value="/syserror.html")
    public String sysError(){
        return "syserror";
    }

    // 添加供应商
    @RequestMapping(value = "/provideradd.html")
    public String addProvider(@ModelAttribute("provider") Provider provider){
        return "provideradd";
    }

    // 添加供应商保存操作
    @RequestMapping(value = "/addprovidersave.html")
    public String addProviderSave(Provider provider, HttpSession session,
                                  @RequestParam(value ="a_companyLicPicPath", required = false) MultipartFile attach){
        String companyLicPicPath = null;
        //判断文件是否为空
        if(!attach.isEmpty()){
            // File.separator 兼容平台的分隔符
            String path = session.getServletContext().getRealPath("statics"+File.separator+"uploadfiles");
            logger.info("uploadFile path ============== > "+path);
            String oldFileName = attach.getOriginalFilename();//原文件名
            logger.info("uploadFile oldFileName ============== > "+oldFileName);
            String prefix=FilenameUtils.getExtension(oldFileName);//原文件后缀
            logger.debug("uploadFile prefix============> " + prefix);
            int filesize = 500000;
            logger.debug("uploadFile size============> " + attach.getSize());
            if(attach.getSize() >  filesize){//上传大小不得超过 500k
                session.setAttribute("uploadFileError", " * 上传大小不得超过 500k");
                return "provideradd";
            }else if(prefix.equalsIgnoreCase("jpg") || prefix.equalsIgnoreCase("png")
                    || prefix.equalsIgnoreCase("jpeg") || prefix.equalsIgnoreCase("pneg")){//上传图片格式不正确
                String fileName = System.currentTimeMillis()+RandomUtils.nextInt(1000000)+"_Provider.jpg";
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
                    session.setAttribute("uploadFileError", " * 上传失败！");
                    return "provideradd";
                }
                companyLicPicPath = path+File.separator+fileName;
            }else{
                session.setAttribute("uploadFileError", " * 上传图片格式不正确");
                return "provideradd";
            }
        }
        provider.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setCreationDate(new Date());
        provider.setCompanyLicPicPath(companyLicPicPath);
        if(providerService.add(provider)){
            return "redirect:/sys/provider/providerlist.html";
        }
        return "provideradd";
    }

    @RequestMapping(value = "/providermodify.html")
    public String getUserById(@RequestParam String pid, Model model){
        logger.info("----->pid:" + pid);
        Provider provider = providerService.getProviderById(pid);
        model.addAttribute(provider);
        return "providermodify";
    }

    @RequestMapping(value = "/providermodifysave.html")
    public String modifyUserSave(Provider provider, HttpSession session){
        logger.debug("----------->modifyProvider pid:" + provider.getId());
        provider.setModifyBy(((User) session.getAttribute(Constants.USER_SESSION)).getId());
        provider.setModifyDate(new Date());
        if (providerService.modify(provider) == 1){
            return "redirect:/sys/provider/providerlist.html";
        }
        return "providermodify";
    }

    // 用户详情
    @RequestMapping(value = "/view/{pid}")
    public String view(@PathVariable String pid, Model model){
        logger.debug("provider view pid ---->" + pid);
        Provider provider = providerService.getProviderById(pid);
        model.addAttribute(provider);
        return "providerview";
    }

    @RequestMapping(value = "/deleteprovider")
    @ResponseBody
    public Object delProvider(@RequestParam String proid) {
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(proid)) {
            int flag = providerService.deleteProviderById(proid);
            if (flag == 0) {//删除成功
                resultMap.put("delResult", "true");
            } else if (flag == -1) {//删除失败
                resultMap.put("delResult", "false");
            } else if (flag > 0) {//该供应商下有订单，不能删除，返回订单数
                resultMap.put("delResult", String.valueOf(flag));
            }
        } else {
            resultMap.put("delResult", "notexit");
        }
        //把resultMap转换成json对象输出
        return JSONArray.toJSONString(resultMap);
    }
}
