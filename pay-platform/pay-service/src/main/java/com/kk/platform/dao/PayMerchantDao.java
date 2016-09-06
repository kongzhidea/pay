package com.kk.platform.dao;

import com.kk.platform.model.PayMerchant;
import org.apache.ibatis.annotations.*;

@MyBatisRepository
public interface PayMerchantDao {
    String FIELDS = " id, name, status, merchant_id, api_key ";

    @Insert("insert into pay_merchant(name, status, merchant_id, api_key) " +
            "values (#{name}, #{status}, #{merchantId}, #{apiKey})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    long insert(PayMerchant payMerchant);

    @Select("select " + FIELDS + " from pay_merchant where id = #{id}")
    PayMerchant selectById(int id);

    @Select("select " + FIELDS + " from pay_merchant where merchant_id = #{merchantId}")
    PayMerchant selectByMerchantId(String merchantId);

    @Update("update pay_merchant set name=#{name}, api_key=#{apiKey} where id=#{id}")
    void update(PayMerchant payMerchant);

    @Update("update pay_merchant set status=#{param2} where id=#{param1}")
    void updateStatusById(int id, int status);

    @Delete("delete from pay_merchant where	id=#{id}")
    void deleteById(int id);
}
