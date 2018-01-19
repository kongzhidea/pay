<%
    /* *
     *功能：支付宝手机网站支付接口调试入口页面， 自己改版后的demo
     *版本：3.3
     *日期：2012-08-17
     *说明：
     *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
     */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>支付宝手机网站支付接口</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <style>
        *{
            margin:0;
            padding:0;
        }
        ul,ol{
            list-style:none;
        }
        body{
            font-family: "Helvetica Neue",Helvetica,Arial,"Lucida Grande",sans-serif;
        }
        .hidden{
            display:none;
        }
        .new-btn-login-sp{
            padding: 1px;
            display: inline-block;
            width: 75%;
        }
        .new-btn-login {
            background-color: #02aaf1;
            color: #FFFFFF;
            font-weight: bold;
            border: none;
            width: 100%;
            height: 30px;
            border-radius: 5px;
            font-size: 16px;
        }
        #main{
            width:100%;
            margin:0 auto;
            font-size:14px;
        }
        .red-star{
            color:#f00;
            width:10px;
            display:inline-block;
        }
        .null-star{
            color:#fff;
        }
        .content{
            margin-top:5px;
        }
        .content dt{
            width:100px;
            display:inline-block;
            float: left;
            margin-left: 20px;
            color: #666;
            font-size: 13px;
            margin-top: 8px;
        }
        .content dd{
            margin-left:120px;
            margin-bottom:5px;
        }
        .content dd input {
            width: 85%;
            height: 28px;
            border: 0;
            -webkit-border-radius: 0;
            -webkit-appearance: none;
        }
        #foot{
            margin-top:10px;
            position: absolute;
            bottom: 15px;
            width: 100%;
        }
        .foot-ul{
            width: 100%;
        }
        .foot-ul li {
            width: 100%;
            text-align:center;
            color: #666;
        }
        .note-help {
            color: #999999;
            font-size: 12px;
            line-height: 130%;
            margin-top: 5px;
            width: 100%;
            display: block;
        }
        #btn-dd{
            margin: 20px;
            text-align: center;
        }
        .foot-ul{
            width: 100%;
        }
        .one_line{
            display: block;
            height: 1px;
            border: 0;
            border-top: 1px solid #eeeeee;
            width: 100%;
            margin-left: 20px;
        }
        .am-header {
            display: -webkit-box;
            display: -ms-flexbox;
            display: box;
            width: 100%;
            position: relative;
            padding: 7px 0;
            -webkit-box-sizing: border-box;
            -ms-box-sizing: border-box;
            box-sizing: border-box;
            background: #1D222D;
            height: 50px;
            text-align: center;
            -webkit-box-pack: center;
            -ms-flex-pack: center;
            box-pack: center;
            -webkit-box-align: center;
            -ms-flex-align: center;
            box-align: center;
        }
        .am-header h1 {
            -webkit-box-flex: 1;
            -ms-flex: 1;
            box-flex: 1;
            line-height: 18px;
            text-align: center;
            font-size: 18px;
            font-weight: 300;
            color: #fff;
        }
    </style>
</head>
<body text=#000000 bgColor="#ffffff" leftMargin=0 topMargin=4>
<header class="am-header">
    <h1>支付宝手机网站支付接口快速通道</h1>
</header>
<div id="main">
    <form name="alipayment"  id="_form">
        <div id="body" style="clear:left">
            <dl class="content">
                <dt>商户订单号
                    ：</dt>
                <dd>
                    <input name="WIDout_trade_no" id="WIDout_trade_no" />
                </dd>
                <hr class="one_line">
                <dt>订单名称(支付时候：订单信息)
                    ：</dt>
                <dd>
                    <input name="WIDsubject" id="WIDsubject" />
                </dd>
                <hr class="one_line">
                <dt>付款金额
                    ：</dt>
                <dd>
                    <input name="WIDtotal_fee" id="WIDtotal_fee" />
                </dd>
                <hr class="one_line">
                <dt>商品展示网址
                    ：</dt>
                <dd>
                    <input name="WIDshow_url" id="WIDshow_url" value="http://www.kk.com/" />
                </dd>
                <hr class="one_line">
                <dt>商品描述：</dt>
                <dd>
                    <input name="WIDbody" id="WIDbody" value="e保养支付测试"/>
                </dd>
                <hr class="one_line">
                <dt></dt>
                <dd id="btn-dd">
                        <span class="new-btn-login-sp">
                            <button class="new-btn-login" type="submit" style="text-align:center;">确 认</button>
                        </span>
                    <span class="note-help">如果您点击“确认”按钮，即表示您同意该次的执行操作。</span>
                </dd>
            </dl>
        </div>
    </form>
    <div id="foot">
        <ul class="foot-ul">
            <li>
                支付宝版权所有 2015-2018 ALIPAY.COM
            </li>
        </ul>
    </div>
</div>
</body>


<script type="text/javascript" src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>

<!-- 微信中使用支付宝,  见ap.js -->
<script type="text/javascript" src="http://www.kk.com/js/ap.js"></script>

<script type="text/javascript">
    $(document).ready(function () {

        function GetDateNow() {
            var vNow = new Date();
            var sNow = "";
            sNow += String(vNow.getFullYear());
            sNow += String(vNow.getMonth() + 1);
            sNow += String(vNow.getDate());
            sNow += String(vNow.getHours());
            sNow += String(vNow.getMinutes());
            sNow += String(vNow.getSeconds());
            sNow += String(vNow.getMilliseconds());
            $("#WIDout_trade_no").val(sNow);
            $("#WIDsubject").val("测试");
            $("#WIDtotal_fee").val("0.01");
        }
        GetDateNow();


        ALIPAY_WAP_URL = 'https://mapi.alipay.com/gateway.do';


        $('#_form').bind('submit',function(e){
            e.preventDefault();
            param = {}
            param["WIDout_trade_no"] = $("#WIDout_trade_no").val();
            param["WIDsubject"] = $("#WIDsubject").val();
            param["WIDtotal_fee"] = $("#WIDtotal_fee").val();
            param["WIDshow_url"] = $("#WIDshow_url").val();
            param["WIDbody"] = $("#WIDbody").val();

            $.post('/kk/alipay/h5pay/api',param,function(ret){
                if (ret.code == 0 ) {
                    console.log(ret);
                    if (typeof _AP != "undefined") { // 微信中使用支付宝
                        var query = stringify_data(ret.params,  true);
                        _AP.pay(ALIPAY_WAP_URL + "?" + query);
                    } else {
                        form_submit(ALIPAY_WAP_URL, "get", ret.params);
                    }
                } else{
                    alert(ret.msg);
                }
            })
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

</html>