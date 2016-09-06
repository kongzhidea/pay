package com.kk.platform.dao;

import com.kk.platform.model.PayMerchantChannel;
import org.apache.ibatis.annotations.*;

import java.util.List;


@MyBatisRepository
public interface PayMerchantChannelDao {
    String FIELDS = " id, pay_type_id, pay_merchant_id, pay_channel_id, trade_type ";

    @Insert("insert into pay_merchant_channel(pay_type_id, pay_merchant_id, pay_channel_id, trade_type) " +
            "values (#{payTypeId}, #{payMerchantId}, #{payChannelId}, #{tradeType})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    long insert(PayMerchantChannel payMerchantChannel);

    @Select("select " + FIELDS + " from pay_merchant_channel where id = #{id}")
    PayMerchantChannel selectById(int id);

    @Select("select " + FIELDS + " from pay_merchant_channel where pay_merchant_id = #{merchantId}")
    List<PayMerchantChannel> selectByMerchantId(int merchantId);

    @Select("select " + FIELDS + " from pay_merchant_channel where pay_merchant_id = #{merchantId} and trade_type = #{tradeType}")
    List<PayMerchantChannel> selectByTradeType(@Param("merchantId") int merchantId, @Param("tradeType") String tradeType);

    @Select("select " + FIELDS + " from pay_merchant_channel where pay_merchant_id = #{merchantId} and pay_type_id = #{payTypeId} and trade_type = #{tradeType}")
    PayMerchantChannel selectByPayType(@Param("merchantId") int merchantId, @Param("payTypeId") int payTypeId, @Param("tradeType") String tradeType);

    @Delete("delete from pay_merchant_channel where	id=#{id}")
    void deleteById(int id);
}
