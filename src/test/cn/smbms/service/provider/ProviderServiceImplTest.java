package cn.smbms.service.provider;

import cn.smbms.dao.user.UserMapperImplTest;
import cn.smbms.pojo.Provider;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Auther xiaohoo
 * @Date 2018/8/31 10:27
 * @Email 1126457667@qq.com
 */
public class ProviderServiceImplTest {
    private Logger logger = Logger.getLogger(UserMapperImplTest.class);
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
    ProviderService providerService = (ProviderService) ctx.getBean("providerService");
    @Test
    public void add() {
        Provider provider = new Provider();
        provider.setProName("Test");
        provider.setProAddress("china");
        provider.setProPhone("12345678");
        provider.setProDesc("nice");
        providerService.add(provider);
        logger.info("Add success");
    }

    @Test
    public void getProviderList() {
        Provider provider = new Provider();
        provider.setProName("北京");
        List<Provider> providerList = new ArrayList<Provider>();
        providerList = providerService.getProviderList("北",null,1,5);
        for (Provider provider1: providerList) {
            logger.info(provider1.getId() + "\t" + provider1.getProName() + "\t" + provider1.getProDesc());
        }
    }

    @Test
    public void deleteProviderById() {
        providerService.deleteProviderById("23");
    }

    @Test
    public void getProviderById() {
        Provider provider = providerService.getProviderById("22");
        logger.info(provider.getId() + "--->" + provider.getProName() + "--->" + provider.getProDesc());
    }

    @Test
    public void modify() {
        Provider provider = new Provider();
        provider.setId(22);
        provider.setProContact("雷军");
        providerService.modify(provider);
    }
}