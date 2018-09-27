package cn.smbms.service.bill;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.pojo.Bill;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("billService")
public class BillServiceImpl implements BillService {

    private BillMapper billDao;
    @Autowired
    @Qualifier("billMapper")
    private BillMapper billMapper;

    public BillServiceImpl() {
    }

    public BillMapper getBillMapper() {
        return billMapper;
    }

    public void setBillMapper(BillMapper billMapper) {
        this.billMapper = billMapper;
    }

    @Override
    public int add(Bill bill) {
		/*// TODO Auto-generated method stub
		boolean flag = false;
		Connection connection = null;
		try {
			connection = BaseDao.getConnection();
			connection.setAutoCommit(false);//开启JDBC事务管理
			if(billDao.add(connection,bill) > 0)
				flag = true;
			connection.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				System.out.println("rollback==================");
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			//在service层进行connection连接的关闭
			BaseDao.closeResource(connection, null, null);
		}*/
        return billMapper.add(bill);
    }

    @Override
    public List<Bill> getBillList(String providerId, String productName, String isPayment, int currentPageNo, int pageSize) {
        int from = (currentPageNo - 1) * pageSize;
        return billMapper.getBillList(providerId, productName, isPayment, from, pageSize);
    }

    @Override
    public int deleteBillById(String delId) {
       /* // TODO Auto-generated method stub
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (billDao.deleteBillById(connection, delId) > 0)
                flag = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }*/
        return billMapper.deleteBillById(delId);
    }

    @Override
    public Bill getBillById(String id) {
 /*       // TODO Auto-generated method stub
        Bill bill = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            bill = billDao.getBillById(connection, id);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            bill = null;
        } finally {
            BaseDao.closeResource(connection, null, null);
        }*/
        return billMapper.getBillById(id);
    }

    @Override
    public int modify(Bill bill) {
        /*// TODO Auto-generated method stub
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (billDao.modify(connection, bill) > 0)
                flag = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }*/
        return billMapper.modify(bill);
    }

    @Override
    public List<Bill> getBillListByProId(Bill bill) {
        return billMapper.getBillListByProId(bill);
    }

    @Override
    public int getBullCount(String providerId, String productName, String isPayment) {
        return billMapper.getBillCount(providerId, productName, isPayment);
    }

}
