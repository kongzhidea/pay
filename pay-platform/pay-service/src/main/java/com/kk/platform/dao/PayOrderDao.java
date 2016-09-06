package com.kk.platform.dao;

import com.kk.platform.model.PayOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@MyBatisRepository
public interface PayOrderDao {
    String FIELDS = " id, pay_type_code, trade_pay_no, pay_order_no, pre_pay_id, pay_id, user_ip, pay_amount, pay_time, status, error_code, error_msg, start_time, expire_time, open_id, buyer_logon_id, notify_url, extra, subject, detail, code_url,trade_type, merchant_id, return_url, refund_amount ";

    @Insert("insert into pay_order(pay_type_code, trade_pay_no, pay_order_no, pre_pay_id, pay_id, user_ip, pay_amount, pay_time, status, error_code, error_msg, start_time, expire_time, open_id, buyer_logon_id, notify_url, extra, subject, detail, code_url,trade_type,merchant_id,return_url,refund_amount) values" +
            "(#{payTypeCode}, #{tradePayNo}, #{payOrderNo}, #{prePayId}, #{payId}, #{userIp}, #{payAmount}, #{payTime}, #{status}, #{errorCode}, #{errorMsg}, #{startTime}, #{expireTime}, #{openId},#{buyerLogonId},  #{notifyUrl}, #{extra}, #{subject}, #{detail}, #{codeUrl}, #{tradeType}, #{merchantId}, #{returnUrl}, #{refundAmount})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(PayOrder payOrder);

    @Select("select " + FIELDS + " from pay_order where id = #{id}")
    PayOrder selectById(int id);

    @Select("select " + FIELDS + " from pay_order where pay_order_no = #{payOrderNo}")
    PayOrder selectByPayOrderNo(String payOrderNo);

    @Select("select " + FIELDS + " from pay_order where pre_pay_id = #{prePayId}")
    PayOrder selectByPrePayId(String prePayId);

    @Select("select " + FIELDS + " from pay_order where trade_pay_no = #{tradePayNo} and merchant_id=#{mchId}")
    List<PayOrder> selectByTradeOrderNo(@Param("mchId") String mchId, @Param("tradePayNo") String tradePayNo);

    @Update("update pay_order set pay_amount = #{payAmount},pay_type_code = #{payTypeCode},trade_type = #{tradeType},pay_id = #{payId}, pay_time = #{payTime}, status = #{status}, error_code = #{errorCode}, error_msg = #{errorMsg}, open_id = #{openId},buyer_logon_id=#{buyerLogonId}, code_url = #{codeUrl} where id = #{id}")
    void update(PayOrder payOrder);

    @Update("update pay_order set pre_pay_id = #{prePayId}, code_url = #{codeUrl}, status = #{status} where id = #{id}")
    void updatePrePayById(@Param("id") int id, @Param("prePayId") String prePayId, @Param("codeUrl") String codeUrl, @Param("status") int status);

    @Update("update pay_order set pay_type_code = #{payTypeCode}, trade_type = #{tradeType} where id = #{id}")
    void updatePayTypeById(@Param("id") int id, @Param("payTypeCode") String payTypeCode, @Param("tradeType") String tradeType);

    @Update("update pay_order set status = #{status}, error_code = #{errorCode}, error_msg = #{errorMsg} where id = #{id}")
    void updateStatusById(@Param("id") int id, @Param("status") int status, @Param("errorCode") String errorCode, @Param("errorMsg") String errorMsg);

    @Update("update pay_order set status = #{status}, refund_amount = #{refundAmount} where id = #{id}")
    void updateRefundById(@Param("id") int id, @Param("status") int status, @Param("refundAmount") int refundAmount);

    @Delete("delete from pay_order where id=#{id}")
    void deleteById(int id);
}
