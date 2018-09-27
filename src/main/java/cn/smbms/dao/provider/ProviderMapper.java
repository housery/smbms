package cn.smbms.dao.provider;

import java.sql.Connection;
import java.util.List;

import cn.smbms.pojo.Provider;
import org.apache.ibatis.annotations.Param;

public interface ProviderMapper {

    /**
     * 增加供应商
     *
     * @param provider
     * @return
     * @throws Exception
     */
    public int add(Provider provider);


    /**
     * 通过供应商名称、编码获取供应商列表-模糊查询-providerList
     *
     * @param
     * @return 供应商列表
     */
    public List<Provider> getProviderList(@Param("proName") String proName, @Param("proCode") String proCode,
                                          @Param("from") int from, @Param("pageSize") int pageSize);

    /**
     * 通过proId删除Provider
     *
     * @param delId 待删除的供应商id
     * @return 操作行数
     */
    public int deleteProviderById(String delId);


    /**
     * 通过proId获取Provider
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Provider getProviderById(String id);

    /**
     * 修改用户信息
     *
     * @param provider
     * @return
     * @throws Exception
     */
    public int modify(Provider provider);

    public int getProCount(@Param("proName") String proName, @Param("proCode") String proCode);
}
