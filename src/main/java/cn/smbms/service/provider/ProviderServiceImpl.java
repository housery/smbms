package cn.smbms.service.provider;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import cn.smbms.dao.BaseDao;
import cn.smbms.dao.bill.BillMapper;
import cn.smbms.dao.provider.ProviderMapper;
import cn.smbms.pojo.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Transactional
@Service("providerService")
public class ProviderServiceImpl implements ProviderService {

    private ProviderMapper providerDao;
    @Autowired
    @Qualifier("providerMapper")
    private ProviderMapper providerMapper;
    @Resource
    private BillMapper billMapper;

    private BillMapper billDao;

    public ProviderServiceImpl() {
        /*providerDao = new ProviderMapperImpl();
        billDao = new BillMapperImpl();*/
    }
    public ProviderMapper getProviderMapper() {
        return providerMapper;
    }

    public void setProviderMapper(ProviderMapper providerMapper) {
        this.providerMapper = providerMapper;
    }

    @Override
    public boolean add(Provider provider) {
       /* // TODO Auto-generated method stub
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务管理
            if (providerDao.add(connection, provider) > 0)
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
        } finally {
            //在service层进行connection连接的关闭
            BaseDao.closeResource(connection, null, null);
        }
        return flag;*/
        int result = providerMapper.add(provider);
        boolean flag = false;
        if (result == 1){
            flag = true;
        }
       return flag;
    }

    @Override
    public List<Provider> getProviderList(String proName, String proCode, int currentPageNo, int pageSize) {
        // 起始偏移量
        int from = (currentPageNo - 1) * pageSize;
        return providerMapper.getProviderList(proName, proCode, from, pageSize);
    }

    /**
     * 业务：根据ID删除供应商表的数据之前，需要先去订单表里进行查询操作
     * 若订单表中无该供应商的订单数据，则可以删除
     * 若有该供应商的订单数据，则不可以删除
     * 返回值billCount
     * 1> billCount == 0  删除---1 成功 （0） 2 不成功 （-1）
     * 2> billCount > 0    不能删除 查询成功（0）查询不成功（-1）
     * <p>
     * ---判断
     * 如果billCount = -1 失败
     * 若billCount >= 0 成功
     */
    @Override
    public int deleteProviderById(String delId) {
/*        // TODO Auto-generated method stub
        Connection connection = null;
        int billCount = -1;
        try {
            connection = BaseDao.getConnection();
            connection.setAutoCommit(false);
            billCount = billDao.getBillCountByProviderId(connection, delId);
            if (billCount == 0) {
                providerDao.deleteProviderById(connection, delId);
            }
            connection.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            billCount = -1;
            try {
                connection.rollback();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        } finally {
            BaseDao.closeResource(connection, null, null);
        }*/
        int billCount = -1; // 发生异常情况
        billCount = billMapper.getBillCountByProviderId(delId); // 可能为0和大于0的值
        if (billCount == 0) {
            providerMapper.deleteProviderById(delId);
        }
        return billCount;
    }

    @Override
    public Provider getProviderById(String id) {
        /*// TODO Auto-generated method stub
        Provider provider = null;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            provider = providerDao.getProviderById(connection, id);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            provider = null;
        } finally {
            BaseDao.closeResource(connection, null, null);
        }*/
        return providerMapper.getProviderById(id);
    }

    @Override
    public int modify(Provider provider) {
       /* // TODO Auto-generated method stub
        Connection connection = null;
        boolean flag = false;
        try {
            connection = BaseDao.getConnection();
            if (providerDao.modify(connection, provider) > 0)
                flag = true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }*/
        return providerMapper.modify(provider);
    }

    @Override
    public int getProCount(String proName, String proCode) {
        return providerMapper.getProCount(proName, proCode);
    }

}
