```
文档地址：https://pay.weixin.qq.com/wiki/doc/api/index.html


商户在微信公众平台(申请扫码支付、公众号支付，刷卡支付)或开放平台：mp.weixin.qq.com (申请APP支付)
申请开通微信支付 文档： http://kf.qq.com/faq/140225MveaUz150107UVFNjy.html

微信支付后台；
https://pay.weixin.qq.com/index.php/home/login


开通微信支付后，会把 微信支付的账号，密码，以及 apikey发给开发者。
```


* 确保 商户功能 审核通过，会有官方邮件
* js支付： 支付授权目录（注意看文档，大小写关系很大 点击支付按钮，提示“access_denied” 网上有很多关于此问题的解决）  , url 一定要 以  /  结尾，  如：http://kk/h5/pay/cashier/
* 支付的时候，要先提交表单 到后端，  后端 先调用  统一下单接口， 设置openId，支付方式，回调url等信息，再生成 前端需要的参数信息，例如  package,paySign等。  前端收到请求后 直接调用微信js方法。
* 获取openId  需要进行微信授权。  具体操作 见 微信公众平台：微信auth认证。
* 在 微信 公众平台， 微信支付--开发配置  ，  配置授权目录，以及测试白名单用户。
点击支付按钮，提示“access_not_allow” 需要将测试人的微信帐号加入白名单
* 【在开发调试阶段，测试链接需要在公众号内点击打开 白名单用户在公众号内向公众号发一条消息，消息内容即为测试链接，然后点击打开】文档中写得很清楚，但中招的人还是不计其数（偶也中了……）。
* 计算签名时候，【参数大小写敏感】md5 运算后，字符串的字符要转换为大写，注意是MD5运算模块。   参与计算签名的值 均 非空。
* body 字段 最长128个字符。 编码要是 utf-8编码。
* 微信支付 退款 需要证书。
* 微信支付 返回结果，如果中文乱码，需要 进行转码：


```
try {
    resultStr = new String(resultStr.getBytes("ISO-8859-1"), "utf-8");
} catch (UnsupportedEncodingException e) {
    e.printStackTrace();
}
```
* 退款时候  需要安装证书:apiclient_cert.p12，默认密码是 商户号。
    * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=4_3

```
