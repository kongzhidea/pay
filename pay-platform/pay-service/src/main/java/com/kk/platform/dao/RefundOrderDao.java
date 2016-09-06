package com.kk.platform.dao;

import com.kk.platform.model.RefundOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@MyBatisRepository
public interface RefundOrderDao {
    String FIELDS = " id, pay_channel_id, pay_type_code,trade_pay_no, pay_order_no, pay_id, pay_amount, refund_order_no, refund_id, refund_amount, status, error_code, error_msg, refund_time,trade_type, trade_refund_no, merchant_id, notify_url, refund_reason,create_time ";

    @Insert("insert into refund_order(pay_channel_id, pay_type_code,pay_order_no,pay_id,pay_amount,refund_order_no,refund_id,refund_amount,status,error_code,error_msg,refund_time,trade_pay_no,merchant_id,trade_type,trade_refund_no,notify_url,refund_reason,create_time) " +
            "values(#{payChannelId},#{payTypeCode},#{payOrderNo},#{payId},#{payAmount},#{refundOrderNo},#{refundId},#{refundAmount},#{status},#{errorCode},#{errorMsg},#{refundTime},#{tradePayNo},#{merchantId},#{tradeType},#{tradeRefundNo},#{notifyUrl},#{refundReason},#{createTime})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void insert(RefundOrder refundOrder);

    @Select("select " + FIELDS + " from refund_order where id = #{id}")
    RefundOrder selectById(int id);

    @Select("select " + FIELDS + " from refund_order where refund_order_no = #{refundOrderNo}")
    RefundOrder selectByRefundOrderNo(String refundOrderNo);

    @Update("update refund_order set refund_id=#{refundId},status=#{status},error_code=#{errorCode},error_msg=#{errorMsg},refund_time=#{refundTime} where id=#{id}")
    void update(RefundOrder refundOrder);

    @Update("update refund_order set status=#{status},error_code=#{errorCode},error_msg=#{errorMsg} where id=#{id}")
    void updateStatusById(@Param("id") int id, @Param("status") int status, @Param("errorCode") String errorCode, @Param("errorMsg") String errorMsg);

    @Delete("delete from refund_order where	id=#{id}")
    void deleteById(int id);

    @Select("select " + FIELDS + " from refund_order where pay_order_no = #{param1}")
    List<RefundOrder> selectByPayOrderNo(String payOrderNo);

    @Select("select " + FIELDS + " from refund_order where pay_order_no = #{param1} and status = #{param2}")
    List<RefundOrder> selectByStatus(String payOrderNo, int status);

    @Update("update refund_order set refund_amount = #{refundAmount} where id = #{id}")
    void updateRefundAmount(RefundOrder refundOrder);

    @Select("select " + FIELDS + " from refund_order where trade_refund_no = #{param1} and merchant_id=#{param2}")
    RefundOrder getRefundOrderByTradeRefundNoMchId(String tradeRefundNo, String merchantId);
}
