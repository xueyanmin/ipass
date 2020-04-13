<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="https://cdn.goeasy.io/goeasy-1.0.5.js"></script>

<script>
    //初始化GoEasy对象
    var goEasy = new GoEasy({
        host:'hangzhou.goeasy.io', //应用所在的区域地址: 【hangzhou.goeasy.io |singapore.goeasy.io】
        appkey: "BC-6a39baaf409a4aa386c0f99e311248f7", //替换为您的应用appkey
    });
    //GoEasy-OTP可以对appkey进行有效保护,详情请参考​ ​

    //浏览器接受消息
    goEasy.subscribe({
        channel: "my_channel", //替换为您自己的channel
        onMessage: function (message) {
            console.log("Channel:" + message.channel + " content:" + message.content);
        }
    });



</script>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>

</body>
</html>