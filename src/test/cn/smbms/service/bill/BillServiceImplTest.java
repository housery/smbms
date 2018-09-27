package cn.smbms.service.bill;

import cn.smbms.pojo.Bill;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther xiaohoo
 * @Date 2018/8/31 13:07
 * @Email 1126457667@qq.com
 */
public class BillServiceImplTest {
    private Logger logger = Logger.getLogger(BillServiceImplTest.class);
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
    BillService billService = (BillService) ctx.getBean("billService");

    @Test
    public void add() {
        Bill bill = new Bill();
        bill.setBillCode("Test_001");
        bill.setProductName("test");
        bill.setIsPayment(1);
        bill.setProductCount(new BigDecimal("2000.00"));
        bill.setProductDesc("带劲Test");
        int count = billService.add(bill);
        logger.info(count);
    }

    @Test
    public void getBillList() {
        Bill bill = new Bill();
        bill.setProductName("洗");
        // List<Bill> billList = billService.getBillList(bill);
        // logger.info(billList.size());
    }

    @Test
    public void deleteBillById() {
        int count = billService.deleteBillById("19");
        logger.info(count);
    }

    @Test
    public void getBillById() {
        Bill bill = billService.getBillById("21");
        logger.info(bill.getId() + "\t" + bill.getProductName());
    }

    @Test
    public void modify() {
        Bill bill = new Bill();
        bill.setId(22);
        bill.setProductName("雷碧可乐");
        int count = billService.modify(bill);
        logger.info(count);
    }

    @Test
    public void getBillListByProId() {
        List<Bill> billList = new ArrayList<Bill>();
        Bill bill = new Bill();
        bill.setProviderId(13);
        bill.setProductName("洗发水");
        bill.setIsPayment(2);
        billList = billService.getBillListByProId(bill);
        for (Bill bill1: billList) {
            logger.debug("billCode " + bill1.getBillCode() + " productName " + bill1.getProductName()
                    + " providerName " + bill1.getProviderName() + " totalPrice " + bill1.getTotalPrice()
                    + " isPayment " + bill1.getIsPayment() + " creationDate " + bill1.getCreationDate());
        }
    }
}