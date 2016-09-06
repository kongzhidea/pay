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
    <title>支付宝网页支付</title>
</head>

<body>
<div class="index_box">
    <div class="apply_name">支付宝网页支付测试</div>


    <div class="branch_con">
        <ul>
            <li><span class="name">测试支付信息，1分钱</span></li>
            <input type="hidden" id="voucherId" value="${voucherId}">
            <input type="hidden" id="total" value="${total}">
        </ul>
        <p class="cz_btn"><a href="javascript:void(0);" class="btn_1" id="payAction">立即支付</a></p>
    </div>
</div>

<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>

<!-- 微信中使用支付宝,  见ap.js -->
<script type="text/javascript" src="/views/alipay/ap.js"></script>

<script type="text/javascript">
    $(document).ready(function () {


        ALIPAY_WAP_URL = 'https://mapi.alipay.com/gateway.do';


        $("#payAction").click(function(){
            var voucherId = $("#voucherId").val();
            var total = $("#total").val();

            $.post('/caller/h5/alipay/do', {voucherId: voucherId,total:total}, function (ret) {
                if (ret.code == 0){
                    console.log(ret);
                    console.log(ret);
                    if (typeof _AP != "undefined") { // 微信中使用支付宝
                        var query = stringify_data(ret.params,  true);
                        _AP.pay(ALIPAY_WAP_URL + "?" + query);
                    } else {
                        form_submit(ALIPAY_WAP_URL, "get", ret.params);
                    }
                }else{
                    alert(ret.msg);
                }
            });

        });


        function stringify_data(data,  urlencode) {
            if (typeof urlencode == "undefined") {
                urlencode = false;
            }
            var output = [];
            for (var i in data) {
                output.push(i + '=' + (urlencode ? encodeURIComponent(data[i]) : data[i]));
            }
            return output.join('&');
        }

        function form_submit(url, method, params) {
            var form = document.createElement("form");
            form.setAttribute("method", method);
            form.setAttribute("action", url);

            for (var key in params) {
                var hiddenField = document.createElement("input");
                hiddenField.setAttribute("type", "hidden");
                hiddenField.setAttribute("name", key);
                hiddenField.setAttribute("value", params[key]);
                form.appendChild(hiddenField);
            }

            document.body.appendChild(form);
            form.submit();
        }

    });
</script>
</body>
</html>