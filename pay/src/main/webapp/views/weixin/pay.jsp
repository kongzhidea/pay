<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection" content="telephone=no" />
    <title>微信公众号支付</title>
</head>

<body>
<div class="index_box">
    <div class="apply_name">微信js支付测试</div>


    <div class="branch_con">
        <ul>
            <li><span class="name">测试支付信息，1分钱</span></li>
            <input type="hidden" id="voucherId" value="${voucherId}">
            <input type="hidden" id="total" value="${total}">
        </ul>
        <p class="cz_btn"><a href="javascript:void(0);" class="btn_1" id="payAction">立即支付</a></p>
    </div>
</div>

<script type="text/javascript" src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>

<script type="text/javascript">
    $(document).ready(function () {

        function onBridgeReady(param) {
            WeixinJSBridge.invoke(
                    'getBrandWCPayRequest', {
                        "appId": param['appId'],     //公众号名称，由商户传入
                        "timeStamp": param['timeStamp'],         //时间戳，自1970年以来的秒数
                        "nonceStr": param['nonceStr'], //随机串
                        "package": param['package'],
                        "signType": param['signType'],         //微信签名方式:
                        "paySign": param['paySign']    //微信签名
                    },
                    function (res) {
                        alert(res.err_msg);
                        // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
                        if (res.err_msg == 'get_brand_wcpay_request:ok') {
                            alert("支付成功");
                        }else if(res.err_msg == 'get_brand_wcpay_request:cancel'){
                            alert("取消支付");
                        }else{
                            alert("支付失败");
                        }
                    }
            );
        }

        $("#payAction").click(function(){
            var voucherId = $("#voucherId").val();
            var total = $("#total").val();

            $.post('/kk/weixin/h5pay/do', {voucherId: voucherId,total:total}, function (ret) {
                if (ret.code == 0){
                    console.log(ret);
                    if (typeof WeixinJSBridge == "undefined"){
                        if( document.addEventListener ){
                            document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                        }else if (document.attachEvent){
                            document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                            document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                        }
                    }else{
                        var param = ret.param;
                        onBridgeReady(param);
                    }
                }else{
                    alert(ret.msg);
                }
            });

        });
    });
</script>
</body>
</html>