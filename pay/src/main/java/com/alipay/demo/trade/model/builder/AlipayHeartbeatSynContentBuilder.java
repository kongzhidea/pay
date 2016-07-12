package com.alipay.demo.trade.model.builder;

import com.alipay.demo.trade.model.hb.EquipStatus;
import com.alipay.demo.trade.model.hb.ExceptionInfo;
import com.alipay.demo.trade.model.hb.PosTradeInfo;
import com.alipay.demo.trade.model.hb.Product;
import com.alipay.demo.trade.model.hb.SysTradeInfo;
import com.alipay.demo.trade.model.hb.TradeInfo;
import com.alipay.demo.trade.model.hb.Type;
import com.alipay.demo.trade.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class AlipayHeartbeatSynContentBuilder extends RequestBuilder {
    private Product product;
    private Type type;

    @SerializedName("equipment_id")
    private String equipmentId;

    @SerializedName("equipment_status")
    private EquipStatus equipmentStatus;
    private String time;

    @SerializedName("manufacturer_app_id")
    private String manufacturerPid;

    @SerializedName("sys_service_provider_id")
    private String providerId;

    @SerializedName("store_id")
    private String storeId;

    @SerializedName("equipment_position")
    private String equipmentPosition;

    @SerializedName("bbs_position")
    private String bbsPosition;

    @SerializedName("network_status")
    private String networkStatus;

    @SerializedName("network_type")
    private String networkType;
    private String battery;

    @SerializedName("wifi_mac")
    private String wifiMac;

    @SerializedName("wifi_name")
    private String wifiName;
    private String ip;
    private String mac;

    @SerializedName("trade_info")
    private List<TradeInfo> tradeInfoList;

    @SerializedName("exception_info")
    private List<ExceptionInfo> exceptionInfoList;

    @SerializedName("extend_info")
    private Map<String, Object> extendInfo;

    public boolean validate() {
        if (this.product == null) {
            throw new NullPointerException("product should not be NULL!");
        }
        if (this.type == null) {
            throw new NullPointerException("type should not be NULL!");
        }
        if (StringUtils.isEmpty(this.equipmentId)) {
            throw new NullPointerException("equipment_id should not be NULL!");
        }
        if (this.equipmentStatus == null) {
            throw new NullPointerException("equipment_status should not be NULL!");
        }
        if (StringUtils.isEmpty(this.time)) {
            throw new NullPointerException("time should not be NULL!");
        }
        return true;
    }

    public String getMac() {
        return this.mac;
    }

    public AlipayHeartbeatSynContentBuilder setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public String getNetworkType() {
        return this.networkType;
    }

    public AlipayHeartbeatSynContentBuilder setNetworkType(String networkType) {
        this.networkType = networkType;
        return this;
    }

    public String getBattery() {
        return this.battery;
    }

    public AlipayHeartbeatSynContentBuilder setBattery(String battery) {
        this.battery = battery;
        return this;
    }

    public String getWifiMac() {
        return this.wifiMac;
    }

    public AlipayHeartbeatSynContentBuilder setWifiMac(String wifiMac) {
        this.wifiMac = wifiMac;
        return this;
    }

    public String getWifiName() {
        return this.wifiName;
    }

    public AlipayHeartbeatSynContentBuilder setWifiName(String wifiName) {
        this.wifiName = wifiName;
        return this;
    }

    public String getNetworkStatus() {
        return this.networkStatus;
    }

    public AlipayHeartbeatSynContentBuilder setNetworkStatus(String networkStatus) {
        this.networkStatus = networkStatus;
        return this;
    }

    public String getBbsPosition() {
        return this.bbsPosition;
    }

    public AlipayHeartbeatSynContentBuilder setBbsPosition(String bbsPosition) {
        this.bbsPosition = bbsPosition;
        return this;
    }

    public String getManufacturerPid() {
        return this.manufacturerPid;
    }

    public AlipayHeartbeatSynContentBuilder setManufacturerPid(String manufacturerPid) {
        this.manufacturerPid = manufacturerPid;
        return this;
    }

    public String getProviderId() {
        return this.providerId;
    }

    public AlipayHeartbeatSynContentBuilder setProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public String getEquipmentId() {
        return this.equipmentId;
    }

    public AlipayHeartbeatSynContentBuilder setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
        return this;
    }

    public String getEquipmentPosition() {
        return this.equipmentPosition;
    }

    public AlipayHeartbeatSynContentBuilder setEquipmentPosition(String equipmentPosition) {
        this.equipmentPosition = equipmentPosition;
        return this;
    }

    public EquipStatus getEquipmentStatus() {
        return this.equipmentStatus;
    }

    public AlipayHeartbeatSynContentBuilder setEquipmentStatus(EquipStatus equipmentStatus) {
        this.equipmentStatus = equipmentStatus;
        return this;
    }

    public List<ExceptionInfo> getExceptionInfoList() {
        return this.exceptionInfoList;
    }

    public AlipayHeartbeatSynContentBuilder setExceptionInfoList(List<ExceptionInfo> exceptionInfoList) {
        this.exceptionInfoList = exceptionInfoList;
        return this;
    }

    public Map<String, Object> getExtendInfo() {
        return this.extendInfo;
    }

    public AlipayHeartbeatSynContentBuilder setExtendInfo(Map<String, Object> extendInfo) {
        this.extendInfo = extendInfo;
        return this;
    }

    public String getIp() {
        return this.ip;
    }

    public AlipayHeartbeatSynContentBuilder setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public AlipayHeartbeatSynContentBuilder setProduct(Product product) {
        this.product = product;
        return this;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public AlipayHeartbeatSynContentBuilder setStoreId(String storeId) {
        this.storeId = storeId;
        return this;
    }

    public String getTime() {
        return this.time;
    }

    public AlipayHeartbeatSynContentBuilder setTime(String time) {
        this.time = time;
        return this;
    }

    public List<TradeInfo> getTradeInfoList() {
        return this.tradeInfoList;
    }

    public AlipayHeartbeatSynContentBuilder setSysTradeInfoList(List<SysTradeInfo> sysTradeInfoList) {
        if (Utils.isListNotEmpty(sysTradeInfoList)) {
            this.tradeInfoList = new ArrayList(sysTradeInfoList);
        }
        return this;
    }

    public AlipayHeartbeatSynContentBuilder setPosTradeInfoList(List<PosTradeInfo> posTradeInfoList) {
        if (Utils.isListNotEmpty(posTradeInfoList)) {
            this.tradeInfoList = new ArrayList(posTradeInfoList);
        }
        return this;
    }

    public Type getType() {
        return this.type;
    }

    public AlipayHeartbeatSynContentBuilder setType(Type type) {
        this.type = type;
        return this;
    }
}
