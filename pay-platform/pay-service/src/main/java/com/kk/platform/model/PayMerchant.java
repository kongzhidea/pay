package com.kk.platform.model;

/**
 * 支付平台对外提供的商户号
 */
public class PayMerchant {
    public static int STATUS_ACTIVE = 1;

    private int id;
    private String name;
    private int status; // 1正常，2 不可用
    private String merchantId; // 商户Id
    private String apiKey; // 秘钥

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "PayMerchant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", merchantId='" + merchantId + '\'' +
                ", apiKey='" + apiKey + '\'' +
                '}';
    }
}
