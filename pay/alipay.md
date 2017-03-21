
```
开放平台地址：https://open.alipay.com/platform/home.htm

商家平台：https://b.alipay.com/signing/home.htm

支付宝地址：https://www.alipay.com/
    选择个人用户， https://shanghu.alipay.com/home/i.htm
    可查看交易记录
    
文档中心：
http://doc.open.alipay.com/doc2/detail?spm=0.0.0.0.E3tvGh&treeId=26&articleId=103286&docType=1  

注意事项：
https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.IH0sUA&treeId=26&articleId=103279&docType=1 

SDK下载地址：
https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.haqwFy&treeId=26&articleId=862&docType=4  

如果使用 mvn，则 需要将 alipay-sdk.jar  放到mvn私服或者安装到本地。 例如：
<dependency>
    <groupId>com.alipay.api</groupId>
    <artifactId>alipay-sdk</artifactId>
    <version>1.5</version>
</dependency>



demo 下载：

https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7386797.0.0.sOXUe9&treeId=54&articleId=104506&docType=1



安装alipay-trade-sdk.jar(对alipay-sdk以及加密，解密等做了封装)包到本地：

mvn install:install-file -DgroupId=com.alipay.api -DartifactId=alipay-trade-sdk -Dversion=1.5 -Dpackaging=jar -Dfile=alipay-trade-sdk.jar

<dependency>
    <groupId>com.alipay.api</groupId>
    <artifactId>alipay-trade-sdk</artifactId>
    <version>1.5</version>
</dependency>

推荐不直接使用demo.jar,  而是反编译出来，然后再自己修改。

反编译注意：
public class GsonFactory {
    public static Gson getGson() {
        return GsonHolder.gson;
    }

    private static class GsonHolder {
        private static Type exceptionListType = new TypeToken<List<ExceptionInfo>>() {
        }.getType();
        private static Type tradeInfoListType = new TypeToken<List<TradeInfo>>() {
        }.getType();

        private static Gson gson = new GsonBuilder()
                .registerTypeAdapter(exceptionListType, new ExceptionInfoAdapter())
                .registerTypeAdapter(tradeInfoListType, new TradeInfoAdapter())
                .registerTypeAdapter(EquipStatus.class, new EquipStatusAdapter())
                .create();
    }
}

  
```

## 公钥私钥
```
支付宝公钥由开放平台分配

商户私钥即开发者公钥，需要手动上传，  开放平台和合作伙伴都需要上传一份。  其中开放平台用于 窗口应用等，  合作作伴用于创建的网站，app等应用。

* 支付宝支付采用了RSA加密签名的安全通信机制，开发者可以通过支付宝的公钥验证消息的来源，同时使用自己的私钥（商户私钥）进行信息加密
   * 集成支付宝支付，我们需要在本地留有  商户私钥，支付宝公钥，  在开放平台设置 商户公钥。
   * 支付宝私钥，公钥由开放平台提供。
```

* 公钥私钥校验工具可从支付宝开放平台下载。
* 支付宝公钥： 支付宝公钥用来验证返回给开发者的信息是否是支付宝发送的。每个应用的支付宝公钥内容是一样的，使用下载的demo或sdk中自带的支付宝公钥即可；第三方应用使用的支付宝公钥，可以在对应的应用环境中“支付宝公钥”处查看。
* https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.7W7fUQ&treeId=44&articleId=103242&docType=1
* 自己的公钥和私钥(公钥上传到支付宝，本地使用私钥加密，支付宝用公钥解密)：
* java 需要转成 pkcs8格式（头部不一样， 一般私钥头：-----BEGIN RSA PRIVATE KEY-----，  pkcs8私钥头：-----BEGIN PRIVATE KEY-----）。

```
openssl pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform PEM -nocrypt -out private_key_pkcs8.pem
```
* 然后把 头部和尾部 信息去掉，并且把中间的回车去掉， 剩下就是  需要的公钥和私钥。