### 签名算法------（签名校验工具）
#### 签名生成的通用步骤如下：
* 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
特别注意以下重要规则：
    * 参数名ASCII码从小到大排序（字典序）；
    * 如果参数的值为空不参与签名；
    * 参数名区分大小写；
    * 验证调用返回或微信主动通知签名时，传送的sign参数不参与签名，将生成的签名与该sign值作校验。
    * 微信接口可能增加字段，验证签名时必须支持增加的扩展字段
* 第二步，在stringA最后拼接上key得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。
key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
    * 假设传送的参数如下：
        * appid：	wxd930ea5d5a258f4f
        * mch_id：	10000100
        * device_info：	1000
        * body：	test
        * nonce_str：	ibuaiVcKdpRxkhJA
    * 第一步：对参数按照key=value的格式，并按照参数名ASCII字典序排序如下：
     * stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA";
    * 第二步：拼接API密钥：
        * stringSignTemp="stringA&key=192006250b4c09247ec02edce69f6a2d"
        * sign=MD5(stringSignTemp).toUpperCase()="9A0A8659F005D6984697E2CA0A9CF3B7"

* 生成随机数算法
    * 微信支付API接口协议中包含字段nonce_str，主要保证签名不可预测。我们推荐生成随机数算法如下：调用随机数函数生成，将得到的值转换为字符串。