package com.kk.platform.dao;

import com.kk.platform.model.PayChannel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@MyBatisRepository
public interface PayChannelDao {
    String FIELDS = "id, pay_type_id, pay_type_name, pay_type_code, pay_channel_name, sign_type, cert_file_id, api_key, app_id, mch_id, status,mch_key,platform_key,query_channel_id ";

    @Insert("insert into pay_channel(pay_type_id, pay_type_name, pay_type_code, pay_channel_name, sign_type, cert_file_id, api_key, app_id, mch_id, status,mch_key,platform_key) " +
            "values (#{payTypeId}, #{payTypeName}, #{payTypeCode}, #{payChannelName}, #{signType}, #{certFileId}, #{apiKey}, #{appId}, #{mchId}, #{status},#{mchKey},#{platformKey})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    long insert(PayChannel payChannel);

    @Select("select " + FIELDS + " from pay_channel where id = #{id}")
    PayChannel selectById(int id);

    @Update("update pay_channel set pay_type_id = #{payTypeId}, pay_type_name = #{payTypeName}, pay_type_code = #{payTypeCode}, pay_channel_name = #{payChannelName}, sign_type = #{signType}, cert_file_id = #{certFileId}, api_key = #{apiKey}, app_id = #{appId}, mch_id = #{mchId},mch_key=#{mchKey},platform_key=#{platformKey} where id = #{id}")
    void update(PayChannel payChannel);

    @Update("update pay_channel set status=#{param2} where id = #{param1}")
    void updateStatusById(int id, int status);

    @Update("update pay_channel set pay_type_name=#{param2}, pay_type_code=#{param3} where pay_type_id = #{param1}")
    void updateByPayTypeId(int payTypeId, String payTypeName, String payTypeCode);

    @Delete("delete from pay_channel where id=#{id}")
    void deleteById(int id);

    @Select("select " + FIELDS + " from pay_channel where id in (${ids})")
    List<PayChannel> getPayChannelList(@Param("ids") String ids);
}
