package cn.smbms.service.bill;

import java.util.List;

import cn.smbms.pojo.Bill;

public interface BillService {
    /**
     * 增加订单
     *
     * @param bill
     * @return
     */
    public int add(Bill bill);


    /**
     * 通过条件获取订单列表-模糊查询-billList
     *
     * @return
     */
    public List<Bill> getBillList(String providerId, String productName, String isPayment,
                                  int currentPageNo, int pageSize);

    /**
     * 通过billId删除Bill
     *
     * @param delId
     * @return
     */
    public int deleteBillById(String delId);


    /**
     * 通过billId获取Bill
     *
     * @param id
     * @return
     */
    public Bill getBillById(String id);

    /**
     * 修改订单信息
     *
     * @param bill
     * @return
     */
    public int modify(Bill bill);

    /**
     * 订单表查询，mybatis spring
     * @param bill
     * @return
     */
    public List<Bill> getBillListByProId(Bill bill);

    /**
     * 获取订单数量，用于分页
     * @return
     */
    public int getBullCount(String providerId, String productName, String isPayment);

}
