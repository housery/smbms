package cn.smbms.controller;

import cn.smbms.pojo.Bill;
import cn.smbms.pojo.Provider;
import cn.smbms.pojo.User;
import cn.smbms.service.bill.BillService;
import cn.smbms.service.bill.BillServiceImpl;
import cn.smbms.service.provider.ProviderService;
import cn.smbms.service.provider.ProviderServiceImpl;
import cn.smbms.tools.Constants;
import cn.smbms.tools.PageSupport;
import com.alibaba.fastjson.JSONArray;
import com.mysql.jdbc.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther xiaohoo
 * @Date 2018/9/18 10:22
 * @Email 1126457667@qq.com
 */
@Controller
@RequestMapping(value = "/sys/bill")
public class BillController {
    private Logger logger = Logger.getLogger(UserController.class);
    @Resource
    private BillService billService;
    @Resource
    private ProviderService providerService;

    /*
    * 获取供应商列表*/
    @RequestMapping(value = "/billlist.html")
    public String billList(Model model,
                           @RequestParam(value = "queryProductName", required = false) String queryProductName,
                           @RequestParam(value = "queryProviderId", required = false) String queryProviderId,
                           @RequestParam(value = "queryIsPayment", required = false) String queryIsPayment,
                           @RequestParam(value = "pageIndex", required = false) String pageIndex){
        logger.info("----> queryProviderId:" + queryProductName);
        logger.info("----> queryProductName:" + queryProviderId);
        logger.info("----> queryIsPayment:" + queryIsPayment);
        logger.info("----> pageIndex:" + pageIndex);

        List<Provider> providerList = providerService.getProviderList(null,null,0,0);
        model.addAttribute("providerList", providerList);

        if(StringUtils.isNullOrEmpty(queryProductName)){
            queryProductName = "";
        }
        List<Bill> billList = new ArrayList<Bill>();
        if(StringUtils.isNullOrEmpty(queryIsPayment)){
            queryIsPayment = "";
        }
        if(StringUtils.isNullOrEmpty(queryProviderId)){
            queryProviderId = "";
        }

        //设置页面容量
        int pageSize = Constants.pageSize;
        //当前页码
        int currentPageNo = 1;
        // 将前段页面索引设为当前页
        if(pageIndex != null){
            try{
                currentPageNo = Integer.valueOf(pageIndex);
            }catch(NumberFormatException e){
                return "redirect:/bill/syserror.html";
            }
        }
        //总数量（表）
        int totalCount	= billService.getBullCount(queryProviderId, queryProductName, queryIsPayment);
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

        billList = billService.getBillList(queryProviderId, queryProductName, queryIsPayment, currentPageNo, pageSize);
        model.addAttribute("billList", billList);
        model.addAttribute("queryProductName", queryProductName);
        model.addAttribute("queryProviderId", queryProviderId);
        model.addAttribute("queryIsPayment", queryIsPayment);
        model.addAttribute("totalPageCount", totalPageCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPageNo", currentPageNo);
        return "billlist";
    }

    @RequestMapping(value="/syserror.html")
    public String sysError(){
        return "syserror";
    }

    // 添加订单
    @RequestMapping(value = "/billAdd.html")
    public String billAdd(){
        return "billadd";
    }

    // 保存订单
    @RequestMapping(value = "/billaddSave.html")
    public String billaddSave(HttpSession session,
                              @RequestParam(value = "billCode", required = false) String billCode,
                              @RequestParam(value = "productName", required = false) String productName,
                              @RequestParam(value = "productDesc", required = false) String productDesc,
                              @RequestParam(value = "productUnit", required = false) String productUnit,
                              @RequestParam(value = "productCount", required = false) String productCount,
                              @RequestParam(value = "totalPrice", required = false) String totalPrice,
                              @RequestParam(value = "providerId", required = false) String providerId,
                              @RequestParam(value = "isPayment", required = false) String isPayment){

        Bill bill = new Bill();
        bill.setBillCode(billCode);
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));
        bill.setCreatedBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setCreationDate(new Date());
        boolean flag = false;
        flag = billService.add(bill) != 0;
        System.out.println("add flag -- > " + flag);
        if(flag){
            return "redirect:/sys/bill/billlist.html";
        }
        return "/sys/bill/billAdd.html";
    }

    // 异步请求供应商列表
    @RequestMapping(value = "/getproviderlist")
    @ResponseBody
    public Object getproviderlist(){
        List<Provider> providerList = providerService.getProviderList(null,null,0,0);
        return JSONArray.toJSONString(providerList);
    }

    // 查看订单
    @RequestMapping(value = "/billview/{billid}")
    public String billView(@PathVariable String billid, Model model){
        logger.info("------> into billview");
        if(!StringUtils.isNullOrEmpty(billid)){
            Bill bill = billService.getBillById(billid);
            model.addAttribute("bill", bill);
            return "billview";
        }
        //没有id值，跳转到订单列表
        return "billlist";
    }

    // 跳转至订单修改页面
    @RequestMapping(value = "/billModify.html")
    public String getbillId(@RequestParam String billid, Model model){
        logger.info("------> into billModify");
        if(!StringUtils.isNullOrEmpty(billid)){
            Bill bill = billService.getBillById(billid);
            model.addAttribute("bill", bill);
            return "billmodify";
        }
        //没有id值，跳转到订单列表
        return "billlist";
    }

    // 订单页面修改
    @RequestMapping(value = "/billModifysave.html")
    public String billModifySave(Model model, HttpSession session,
                                 @RequestParam(value = "id", required = false) String id,
                                 @RequestParam(value = "productName", required = false) String productName,
                                 @RequestParam(value = "productDesc", required = false) String productDesc,
                                 @RequestParam(value = "productUnit", required = false) String productUnit,
                                 @RequestParam(value = "productCount", required = false) String productCount,
                                 @RequestParam(value = "totalPrice", required = false) String totalPrice,
                                 @RequestParam(value = "providerId", required = false) String providerId,
                                 @RequestParam(value = "isPayment", required = false) String isPayment){
        logger.debug("---------->into billModifySave");
        Bill bill = new Bill();
        bill.setId(Integer.valueOf(id));
        bill.setProductName(productName);
        bill.setProductDesc(productDesc);
        bill.setProductUnit(productUnit);
        bill.setProductCount(new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setIsPayment(Integer.parseInt(isPayment));
        bill.setTotalPrice(new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        bill.setProviderId(Integer.parseInt(providerId));

        bill.setModifyBy(((User)session.getAttribute(Constants.USER_SESSION)).getId());
        bill.setModifyDate(new Date());
        boolean flag = false;
        flag = billService.modify(bill) != 0;
        if(flag){
            return "redirect:/sys/bill/billlist.html";
        }
        return "/sys/bill/billModify.html";
    }

    // 删除订单
    @RequestMapping(value = "/deleteBill")
    @ResponseBody
    public Object deleteBill(@RequestParam String billid){
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if(!StringUtils.isNullOrEmpty(billid)){
            boolean flag = billService.deleteBillById(billid) != 0;
            if(flag){//删除成功
                resultMap.put("delResult", "true");
            }else{//删除失败
                resultMap.put("delResult", "false");
            }
        }else{
            resultMap.put("delResult", "notexit");
        }
        return JSONArray.toJSONString(resultMap);
    }
}
