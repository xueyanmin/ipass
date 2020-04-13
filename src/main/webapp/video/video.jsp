<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    $(function() {

            /**初始化表单属性*/
            $("#vTable").jqGrid({
                url : "${path}/video/queryByPage", //分页查询   page  rows  total recoreds  page  rows
                editurl:"${path}/video/edit",
                datatype : "json",
                rowNum : 3,  //每页展示是条数
                rowList : [ 3,5,10, 20, 30 ],
                pager : '#vPager',
                styleUI:"Bootstrap",
                height:"auto",
                autowidth:true,
                viewrecords:true,  //是否展示数据总条数
                colNames : [ 'ID', '标题', '描述', '视频', '上传时间','用户Id', '类别id','分组id'],
                colModel : [
                    {name : 'id',width : 55},
                    {name : 'title',editable:true,width : 90},
                    {name : 'brief',editable:true,width : 100},
                    {name : 'path',editable:true,width : 400,align : "center",edittype:"file",
                     formatter:function (cellvalue,options,rowObject) {
                         return "<video id='media' src='"+cellvalue+"' controls width='200px' heigt='400px' poster='"+rowObject.cover+"'/>";
                    }
                    },
                    {name : 'uploadTime',width : 80,align : "right"},
                    {name : 'userId',width : 80,align : "right"},
                    {name : 'categoryId',width : 80,align : "center"},
                    {name : 'groupId',width : 150,sortable : false}
                ]
            });

        /**处理曾删改查操作   工具栏*/
        $("#vTable").jqGrid('navGrid', '#vPager', {edit: true, add: true, del: true,
            edittext:"修改",addtext:"添加",deltext:"删除"},
            {
                closeAfterEdl:true,//关闭对话框
                beforeShowForm:function (obj) {
                    obj.find("#path").attr("disabled",true); //禁用input

                }
            },//执行修改之后的额外操作
            {
               closeAfterAdd:true,  //关闭添加对话框
               //异步上传
                afterSubmit:function (data) {
                 $.ajaxFileUpload({
                     fileElementId:"path",   //需要上传的文件域ID 即<input type="file">的ID。
                     url:"${path}/video/uploadVideo",       //上传后台方法的路径
                     type:"post",  //当要提交自定义参数时,这个参数要设置成post
                     data:{id:data.responseText},  //responseTest: "b90dfb11-cb7a-4984-ae72-5cae3c5a0389"
                     success:function () { //提交成功之后自动执行的处理函数,参数data就是服务器返回的数据
                         //刷新页面
                         $("#vTable").trigger("reloadGrid");
                     }
                 });
                 //必须有返回值
                 return "hello";
               }
            },//执行添加之后的额外操作
            {
                afterSubmit:function (data) {
                    //向警告矿中追加错误信息
                    $("#showMsg").html(data.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function () {
                        $("A#deleteMsg").hide();
                    },3000);
                return "hello";
                }
              }//执行删除之后的额外操作
            );
        });



</script>

<%--初始化面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading" align="center">
        <h2>视频管理</h2>
    </div>

    <%--选项卡--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">视频信息</a></li>
    </div>

    <%--警告提示框--%>
    <div id="deleteMsg" class="alert alert-danger" style="height: 50px;width: 250px;display: none" align="center">
        <span id="showMsg"/>
    </div>

    <%--初始化表单--%>
    <table id="vTable" />

    <%--工具栏--%>
    <div id="vPager" />

</div>