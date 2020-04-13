<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script src="${path}/bootstrap/js/jquery.min.js"></script>

<script>
    $(function () {
        //点击搜索按钮触发
        $("#btnId").click(function () {
            //点击之后清空表单
            $("#queryTable").empty();
            //获取数据渲染页面
            $("#queryTable").append("<tr>" +
                "<td>ID</td>" +
                "<td>标题</td>" +
                "<td>描述</td>" +
                "<td>封面</td>" +
                "<td>创建时间</td>"+
                "</tr>");

           //点击获取内容
            var content = $("#contentId").val();

           //向后台发请求查询数据
            $.ajax({
                url:"${path}/video/querySearch",
                type:"post",
                dataType:"Json",
                data:{"content":content},
                success:function(data){
                    console.log(data);

                    //遍历取出数据 (data:要遍历的数据)
                    $.each(data,function (index,video) {

                        //获取数据渲染页面
                        $("#queryTable").append("<tr>" +
                            "<td>"+video.id+"</td>" +
                            "<td>"+video.title+"</td>" +
                            "<td>"+video.brief+"</td>" +
                            "<td><img src='"+video.cover+"' style='width: 200px;height: 100px'/></td>" +
                            "<td>"+video.publishDate+"</td>" +

                        "</tr>");
                    })
                }
            })
        });
    });
</script>
<%--检索搜索框--%>
<div align="center">
    <div class="input-group" style="width: 300px">
        <input id="contentId" type="text" class="form-control" placeholder="请输入搜索视频" aria-describedby="basic-addon2">
        <span class="input-group-btn" id="basic-addon2">
            <button id="btnId" class="btn btn-warning">百知一下</button>
        </span>
    </div>
</div>
<hr>

<%--展示检索内容--%>
<div class="panel panel-default">
    <!--面板标题-->
    <div class="panel-heading">搜索结果</div>

    <!--搜索内容-->
    <table id="queryTable" class="table">
        <%--<tr>
            <td>ID</td>
            <td>标题</td>
            <td>描述</td>
            <td>封面</td>
            <td>时间</td>
        </tr>--%>
    </table>
</div>