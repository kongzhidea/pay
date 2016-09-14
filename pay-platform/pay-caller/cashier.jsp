<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport"   content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
  <meta name="format-detection" content="telephone=no" />
  <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="Expires" content="0" />

  <title>${title}</title>

  <style type="text/css">
    *{margin: 0;padding: 0;}
    ul,ol,li{list-style: none;}
    html{
      background: #f8f8f8;
      -moz-user-select: none;
      -webkit-user-select: none;
      -ms-user-select: none;
      -khtml-user-select: none;
      user-select: none;
      -webkit-touch-callout: none;
      font-family: '黑体';
    }
    b{font-weight: normal;}
    i{font-style: normal;}
    img {
      pointer-events: none;
      -webkit-user-select: none;
    }
    /*
        基本样式
     */
    .db-float-left{
      float: left!important;
    }
    .db-float-right{
      float: right!important;
    }
    .db-back-white{
      background: #FFFFFF!important;
    }
    .container{
      min-height: 100%;
      font-size: .32rem;
      color: #1c1b21;
      font-family: "黑体";
    }
    /*
        支付金额
     */
    .payment{
      position: relative;
      background: red;
      padding: .87rem .23rem .4rem;
      margin-bottom: .25rem;
    }
    /*更新*/
    .payment h4{
      max-width: 5rem;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
      position: absolute;
      left: 1.5rem;
      top: 0.4rem;
    }
    .blackcat{
      overflow: hidden;
      position: absolute;
      top: .18rem;
      left: .23rem;
    }
    .blackcat img{
      display: block;
      width: 1.14rem;
      height: .84rem;
    }
    .payment-cont{
      padding: .25rem .2rem .21rem .13rem;
      height: 1rem;
      border: .02rem solid #c7c7c7;
      border-radius: 10px;
      background: #f7f7f7;
      overflow: hidden;
    }
    .payment-cont p{
      height: 1rem;
      line-height: 1rem;
    }
    .payment-cont-icon,.payment-cont-num{
      font-size: .6rem;
    }
    .payment-cont-icon{
      width: 0.72rem;
    }
    .payment-cont-icon span{
      float: left;
    }
    #marker{
      line-height: 0.95rem;
    }
    /*
        输入键盘
     */
    .support-box{
      height: 0.26rem;
      padding: 0 0.3rem;
      margin-bottom: 0.2rem;
    }
    .keybord-box{
      width: 100%;
      position: fixed;
      bottom: 0;
      left: 0;
    }
    .support{
      height: 0.26rem;
      width: 100%;
      background-size: 100%;
    }
    .keybord{
      width: 100%;
      border-collapse: collapse;
    }
    .keybord td{
      width: 25%;
      height: 1.34rem;
      line-height: 1.34rem;
      text-align: center;
      font-size: .5rem;
      -moz-user-select: none;
      -webkit-user-select: none;
      -ms-user-select: none;
      -khtml-user-select: none;
      user-select: none;
      border: 1px solid #c9c9c9;
    }
    .keybord-back{
      background: url("http://www.kk.com/images/icon-back.png") no-repeat center;
      background-size: 50%;
    }
    .keybord .border-top{
      border-top: 1px solid #dfdede;
    }
    .keybord .border-left{
      border-left: 1px solid #dfdede;
    }
    .keybord .makesure{
      line-height: .7rem;
      background: #d7d3d4;
      color: #ffffff;
    }
    /*确认支付可以点击时微信和京东的不同状态*/
    .keybord .canClick{
      background: #06be04;
    }
    .keybord .makesure span{
      display: block;
    }
    .keybord-down img{
      width: .48rem;
      height: .48rem;
    }

    .lastpay{
      padding: .34rem 0;
      width: 7rem;
      text-align: center;
      background: #d7d3d4;
      border-radius: 5px;
      color: #fff;
      font-size: .34rem;
      margin: 0 auto;
    }

    .loading-bg{
      width: 100%;
      height: 100%;
      position: absolute;
      left: 0;
      top: 0;
      z-index: 99;
      /*background: rgba(22,22,22,0.5);*/
      display: none;
    }
    .loading{
      width: 3.12rem;
      height: 3.12rem;
      position: absolute;
      left: 50%;
      top: 45%;
      margin-left: -1.56rem;
      margin-top: -1.56rem;
      border-radius: 0.2rem;
      z-index: 100;
      background: rgba(0,0,0,0.5);
    }
    .loading img{
      width: 96px;
      height: 96px;
      position: absolute;
      left: 50%;
      top: 50%;
      margin-left: -48px;
      margin-top: -48px;
    }

    @keyframes mymove
    {
      from {opacity:0;}
      to {opacity:1;}
    }

    @-webkit-keyframes mymove /*Safari and Chrome*/
    {
      from {opacity:0;}
      to {opacity:1;}
    }
  </style>	<script>
  (function () {
    document.addEventListener('DOMContentLoaded', function () {
      var html = document.documentElement;
      var windowWidth = html.clientWidth;
      html.style.fontSize = windowWidth / 7.5 + 'px';
      // 等价于html.style.fontSize = windowWidth / 640 * 100 + 'px';
    }, false);
  })();
</script>
</head>

<body>
<form action="" method="POST" id="payForm">
  <!-- 总输入金额 -->
  <input type="hidden" id="amount" name="orderAmount" value="" />
</form>
<div class="loading-bg">
  <div class="loading">
    <img src="http://www.kk.com/images/loading.gif" alt="">
  </div>
</div>

<div class="container">
  <!-- 金额显示栏 -->
  <div class="payment db-back-white">
    <p class="blackcat">
    </p>
    <div class="payment-cont">
      <span class="cursor-blink"></span>
      <p class="payment-cont-word db-float-left">消费金额</p>
      <p class="payment-cont-num db-float-right"></p>
      <p class="payment-cont-icon db-float-right"><span>¥</span><span id="marker">|</span></p>
    </div>
  </div>
  <!-- 输入键盘 -->
  <div class="keybord-box">
    <div class="support-box">
      <div class="support"></div>
    </div>
    <table class="keybord db-back-white">
      <tr>
        <td name="number" class="border-top">1</td>
        <td name="number" class="border-left border-top">2</td>
        <td name="number" class="border-left border-top">3</td>
        <td class="border-left keybord-back border-top"></td>
      </tr>
      <tr>
        <td name="number" class="border-top">4</td>
        <td name="number" class="border-top border-left">5</td>
        <td name="number" class="border-top border-left">6</td>

        <td class="border-top border-left makesure" id="lastpay" rowspan="3">
          <span>确认</span>
          <span>支付</span>
        </td>

      </tr>
      <tr>
        <td name="number" class="border-top">7</td>
        <td name="number" class="border-top border-left">8</td>
        <td name="number" class="border-top border-left">9</td>
      </tr>
      <tr>
        <td name="number" class="border-top" colspan="2">0</td>
        <td name="number" class="border-top border-left">.</td>
      </tr>
    </table>
  </div>
</div>
<script type="text/javascript">
  // 异步提交表单
  function ajaxSubmitForm(){
    var orderAmount = document.getElementById('amount').value;
    var payType = "WECHAT_PAY";
    ajax({
      url: '/pay',
      type: 'POST',
      data: {"orderAmount":orderAmount,"payType":payType},
      success: function(str){
        var ret = JSON.parse(str);
        if(ret.code == "0"){
          onBridgeReady(ret.data.credential,ret.data.tradePayNo);
          loadingHide();
        }else{
          alert(ret.msg);
          loadingHide();
          addEvent(document.querySelector('#lastpay'),'touchend',commit);
          addClass(document.querySelector('#lastpay'),"canClick");
        }
      }
    });
  }

  function onBridgeReady(param,voucherId){
    WeixinJSBridge.invoke(
            'getBrandWCPayRequest', {
              "appId": param['appId'],     //公众号名称，由商户传入
              "timeStamp": param['timeStamp'],         //时间戳，自1970年以来的秒数
              "nonceStr": param['nonceStr'], //随机串
              "package": param['package'],
              "signType": param['signType'],         //微信签名方式:
              "paySign": param['paySign']    //微信签名
            },
            function(res){
              if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                var successUrl = "/h5/pay/shop_pay/success?voucherId=" + voucherId;
                window.location.href = successUrl;
//                alert(successUrl);
              }else{
                addEvent(document.querySelector('#lastpay'),'touchend',commit);
                addClass(document.querySelector('#lastpay'),"canClick");
              }
            }
    );
  }
</script>
<script type="text/javascript">
  //监听触摸键盘事件
  var aNumbers = document.getElementsByName('number');
  var oBack = document.querySelector('.keybord-back');
  var oMakeSure = document.getElementById('lastpay');
  for (var i = 0; i < aNumbers.length; i++) {
    aNumbers[i].ontouchend = function(e){
      this.style.background = '#fff';
      e.preventDefault();
    }
    addEvent(aNumbers[i], 'touchstart', inputing);
  }
  //按下退格键颜色变化
  oBack.ontouchend = function (e){
    this.style.backgroundColor = '#fff';
    e.preventDefault();
  }
  //按下退格按钮
  addEvent(oBack, 'touchstart', backClick);
  //光标调用
  setInterval(markBlink(), 580);
  //点击确认支付
  var oMakesure = document.getElementById('lastpay');
  addEvent(oMakesure,'touchend',commit);

  //id简易选择器
  function $(selector) {
    return document.getElementById(selector);
  }
  //光标闪烁
  function markBlink() {
    var n = 1;
    function inner() {
      if (n % 2 == 0) {
        $("marker").style.color = "#1c1b21";
      } else {
        $("marker").style.color = "#f7f7f7";
      }
      return n++;
    }
    return inner;
  }
  function islessZero(tar){
    if(tar=='undefind'||tar==null||tar==''){
      return true;
    }
    return parseFloat(tar)<=0;
  }
  //输入金额函数
  function inputing(e) {
    this.style.background = '#eee';
    var oPayNum = document.querySelector('.payment-cont-num');
    var amount = document.getElementById("amount").value;
    var num = this.innerHTML;
    var newAmount;
    //判断输入金额在小数点前五位、小数点后两位
    if((amount == null || amount == '') && num == '.') {
      newAmount = "0" + num;
    }else if(amount == null || amount == ''){
      newAmount = num;
    }else if(num == '.' && amount.indexOf(".") > 0){
      newAmount = amount;
    }else if(num == '0' && amount.indexOf(".") < 0 && amount.indexOf("0") == 0){
      newAmount = amount;
    }else if(num == '.' && amount.indexOf(".") < 0 && parseInt(amount) < 50000){
      newAmount = amount + num;
    }else if(amount.indexOf(".") > 0 && (amount.length - amount.indexOf(".")) > 2){
      newAmount = amount;
    }else if(amount.length > 8){
      newAmount = amount;
    }else if(amount.indexOf(".") < 0 && islessZero(amount) && num != '.'){
      newAmount = num;
    }else if (amount.indexOf(".") < 0 && parseInt(amount) == 5000 && num != '0'){
      newAmount = amount;
    }else if(amount.indexOf(".") < 0 && parseInt(amount) > 5000){
      newAmount = amount;
    }else if(amount.indexOf(".") < 0 && parseInt(amount) <= 50000 && amount.length >= 5 && num != '.'){
      newAmount = amount;
    }else{
      newAmount = amount + num;
    }
    document.getElementById("amount").value = newAmount;
    oPayNum.innerHTML = newAmount;
    speFunOpen();
    e.preventDefault();
  }
  //按下退格键函数
  function backClick(e){
    this.style.backgroundColor = '#eee';
    var amount = document.getElementById("amount").value;
    var newAmount;
    if (amount == null || amount == '') {
      return;
    } else {
      newAmount = amount.substring(0, amount.length - 1);
    }
    document.getElementById("amount").value = newAmount;
    document.querySelector('.payment-cont-num').innerHTML = newAmount;

    speFunEnd();     //输入金额临界值触发的事件
    e.preventDefault();
  }

  //输入金额临界值触发的事件
  function speFunOpen(){
    $("marker").style.display = "none";
    if(parseFloat($("amount").value)){
      addClass($("lastpay"),"canClick");
    }
  }
  function speFunEnd(){
    if (parseFloat($("amount").value) == 0) {
      removeClass($("lastpay"),"canClick");
    }else if($("amount").value == ''){
      $("marker").style.display = "block";
      removeClass($("lastpay"),"canClick");
    }
  }
  //提交金额执行函数
  function commit(){
    var oAmount = document.getElementById('amount');
    var oCashedNum = document.getElementById('cashedNum');
    var oPayForm = document.getElementById('payForm');

    if(parseFloat(oAmount.value) > 0){
      removeEvent(document.querySelector('#lastpay'),'touchend',commit);
      // $("lastpay").style.backgroundColor = "#d7d3d4";
      removeClass($("lastpay"),"canClick");
      loadingShow();
      ajaxSubmitForm();
    }else{
      return false;
    }
  }

  // 加载图片的显示隐藏
  function loadingShow(){
    var oLoading = document.querySelector('.loading-bg');
    oLoading.style.display = 'block';
    oLoading.style.animation = 'mymove 2s';
  }
  function loadingHide(){
    var oLoading = document.querySelector('.loading-bg');
    oLoading.style.display = 'none';
    oLoading.style.animation = '';
  }
  //事件监听
  function addEvent(obj, sEv, fn) {
    if (obj.addEventListener) {
      obj.addEventListener(sEv, fn, false);
    } else {
      obj.attachEvent('on' + sEv, fn);
    }
  }
  function removeEvent(obj, sEv, fn) {
    if (obj.removeEventListener) {
      obj.removeEventListener(sEv, fn, false);
    } else {
      obj.detachEvent('on' + sEv, fn);
    }
  }
  //增加className
  function addClass(obj,sClass){
    if(obj.className){
      var reg = new RegExp('\\b'+sClass+'\\b','g');
      if(obj.className.search(reg) == -1){
        obj.className += ' '+sClass;
      }
    }else{
      obj.className = sClass;
    }
  }
  //删除className
  function removeClass(obj,sClass){
    if(obj.className){
      var reg = new RegExp('\\b'+sClass+'\\b','g');
      if(obj.className.search(reg) != -1){
        obj.className = obj.className.replace(reg,'').replace(/^\s+|\s+$/,' ').replace(/\s+/g,' ');
        if(!obj.className){
          obj.removeAttribute("class");
        }
      }
    }
  }
  //查看是否有这个class名，有就返回true否则返回false
  function hasClass(obj, cls){
    var obj_class = obj.className,//获取 class 内容.
            obj_class_lst = obj_class.split(/\s+/);//通过split空字符将cls转换成数组.
    var x = 0;
    for(x in obj_class_lst) {
      if(obj_class_lst[x] == cls) {//循环数组, 判断是否包含cls
        return true;
      }
    }
    return false;
  }
  //ajax函数 封装
  function json2url(json){
    var arr = [];
    for(var name in json){
      arr.push(name+'='+encodeURIComponent(json[name]));
    }
    return arr.join('&');
  }
  function ajax(json){
    json = json||{};
    if(!json.url)return;
    json.data = json.data||{};
    json.type = json.type||'get';

    if(window.XMLHttpRequest){
      var oAjax = new XMLHttpRequest();
    }else{
      var oAjax = new ActiveXObject('Microsoft.XMLHTTP');
    }

    switch(json.type.toLowerCase()){
      case 'get':
        oAjax.open('GET',json.url+'?'+json2url(json.data),true);
        oAjax.send();
        break;
      case 'post':
        oAjax.open('POST',json.url,true);
        oAjax.setRequestHeader('Content-type','application/x-www-form-urlencoded');
        oAjax.send(json2url(json.data));
        break;
    }
    oAjax.onreadystatechange=function(){
      if(oAjax.readyState==4){
        if(oAjax.status>=200&&oAjax.status<300||oAjax.status==304){
          json.success&&json.success(oAjax.responseText);
        }else{
          json.error&&json.error(oAjax.status);
        }
      }
    };
  }

</script>
</body>
</html>
