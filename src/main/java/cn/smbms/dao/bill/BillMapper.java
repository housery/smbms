package cn.smbms.dao.bill;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.Bill;
import org.apache.ibatis.annotations.Param;

public interface BillMapper {
    /**
     * 增加订单
     *
     * @param bill
     * @return
     * @throws Exception
     */
    public int add(Bill bill);


    /**
     * 通过查询条件获取供应商列表-模糊查询-getBillList
     *
     * @return
     * @throws Exception
     */
    public List<Bill> getBillList(@Param("providerId") String providerId,
                                  @Param("productName") String productName,
                                  @Param("isPayment") String isPayment,
                                  @Param("from") int from, @Param("pageSize") int pageSize);

    /**
     * 订单表查询，mybatis spring
     * @param bill
     * @return
     */
    public List<Bill> getBillListByProId(Bill bill);

    /**
     * 通过delId删除Bill
     *
     * @param delId
     * @return
     * @throws Exception
     */
    public int deleteBillById(String delId);


    /**
     * 通过billId获取Bill
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Bill getBillById(String id);

    /**
     * 修改订单信息
     *
     * @param bill
     * @return
     * @throws Exception
     */
    public int modify(Bill bill);

    /**
     * 查询订单数量
     *
     * @return
     * @throws Exception
     */
    public int getBillCount(@Param("providerId") String providerId,
                            @Param("productName") String productName,
                            @Param("isPayment") String isPayment);

    public int getBillCountByProviderId(@Param("providerId") String providerId);
}
