package com.alipay.demo.trade.model;

import com.alipay.demo.trade.utils.Utils;
import com.google.gson.annotations.SerializedName;

public class GoodsDetail {

    @SerializedName("goods_id")
    private String goodsId;

    @SerializedName("alipay_goods_id")
    private String alipayGoodsId;

    @SerializedName("goods_name")
    private String goodsName;
    private int quantity;
    private String price;

    @SerializedName("goods_category")
    private String goodsCategory;
    private String body;

    public static GoodsDetail newInstance(String goodsId, String goodsName, long price, int quantity) {
        GoodsDetail info = new GoodsDetail();
        info.setGoodsId(goodsId);
        info.setGoodsName(goodsName);
        info.setPrice(price);
        info.setQuantity(quantity);
        return info;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("GoodsDetail{");
        sb.append("goodsId='").append(this.goodsId).append('\'');
        sb.append(", alipayGoodsId='").append(this.alipayGoodsId).append('\'');
        sb.append(", goodsName='").append(this.goodsName).append('\'');
        sb.append(", quantity=").append(this.quantity);
        sb.append(", price='").append(this.price).append('\'');
        sb.append(", goodsCategory='").append(this.goodsCategory).append('\'');
        sb.append(", body='").append(this.body).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getGoodsId() {
        return this.goodsId;
    }

    public GoodsDetail setGoodsId(String goodsId) {
        this.goodsId = goodsId;
        return this;
    }

    public String getAlipayGoodsId() {
        return this.alipayGoodsId;
    }

    public GoodsDetail setAlipayGoodsId(String alipayGoodsId) {
        this.alipayGoodsId = alipayGoodsId;
        return this;
    }

    public String getGoodsName() {
        return this.goodsName;
    }

    public GoodsDetail setGoodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public GoodsDetail setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getPrice() {
        return this.price;
    }

    public GoodsDetail setPrice(long price) {
        this.price = Utils.toAmount(price);
        return this;
    }

    public String getGoodsCategory() {
        return this.goodsCategory;
    }

    public GoodsDetail setGoodsCategory(String goodsCategory) {
        this.goodsCategory = goodsCategory;
        return this;
    }

    public String getBody() {
        return this.body;
    }

    public GoodsDetail setBody(String body) {
        this.body = body;
        return this;
    }
}
