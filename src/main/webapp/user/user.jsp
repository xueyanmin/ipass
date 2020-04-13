<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function () {


        /**初始化一个表格*/
        $("#userTable").jqGrid(
            {
                url: "${path}/user/queryByPage", //接受 page：当前页 rows:每页展示条数
                editurl: "${path}/user/edit",
                datatype: "json",              //返回  page:当前页 rows:返回总条数List total:总页数  records:总条数
                rowNum: 2, //每页显示条数
                rowList: [2, 4, 5, 20, 30],  //可选条数
                pager: '#userPage',  //分页工具栏
                viewrecords: true,   //显示总条数
                styleUI: "Bootstrap",
                height: "auto",
                autowidth: true,
                colNames: ['Id', '用户名', '手机号', '头像', '签名', '微信', '状态', '注册时间'],
                colModel: [
                    {name: 'id', width: 55},
                    {name: 'username', editable: true, width: 90},
                    {name: 'phone', editable: true, width: 100},
                    {name: 'headImg', editable: true, width: 150, align: "center", edittype: "file",
                        formatter: function (cellvalue, options, rowObject) {

                            //本地路径拼接
                            //return "<img src='${path}/upload/photo/" + cellvalue + "' height='100px' width='100px'/>";
                            //网络路径拼接
                            return "<img src='http://yingx-185.oss-cn-beijing.aliyuncs.com/" + cellvalue + "' height='100px' width='100px'/>";

                        }
                    },
                    {name: 'sign', editable: true, width: 80, align: "right"},
                    {name: 'wechat', editable: true, width: 80, align: "right"},
                    {name: 'status', width: 150, sortable: false, align: "center",
                        //cellvalue 列的值 options:行操作 rowObject:行对象
                        formatter: function (cellvalue, options, rowObject) {
                            if (cellvalue == 1) {//1为正常 0为冻结
                                return "<button onclick='updateStatus(\"" + rowObject.id + "\",\"" + cellvalue + "\")' class='btn btn-info'>冻结</button>";
                            } else {
                                return "<button onclick='updateStatus(\"" + rowObject.id + "\",\"" + cellvalue + "\")' class='btn btn-danger'>解除冻结</button>";
                            }
                        }
                    },
                    {name: 'createDate', width: 150, sortable: false}
                ]

            });

        /**分页工具栏*/
        $("#userTable").jqGrid('navGrid', '#userPage', {
                edit: true,
                add: true,
                del: true,
                edittext: "修改",
                addtext: "添加",
                deltext: "删除"
            },
            {
                closeAfterEdit: true,  //修改

                afterSubmit: function (response) {
                    $.ajaxFileUpload({
                        url: "${path}/user/uploadUser",
                        type: "post",
                        dataType: "text",
                        fileElementId: "headImg",
                        data: {id: response.responseText},
                        success: function () {
                            $("#userTable").trigger("reloadGrid");
                        }
                    });
                    return "hello";
                }
            },//添加之后的额外操作
            {
                closeAfterAdd: true,//添加
                //文件上传 afterSubmit在提交之后要做体格响应
                afterSubmit: function (response) {

                    //！：数据的入库
                    //  ：遗留的两个问题
                    //  1:图片路径不对
                    //2：文件根本没有上传

                    //3:修改图片路径 需要三个参数 ID 图片路径
                    //fileElementId 需要上传的文件域的id
                    //2： 在提交之后做文件上传  本地
                    console.log(response);

                    //异步文件上传
                    $.ajaxFileUpload({
                        url: "${path}/user/uploadUser",   //访问路径
                        type: "post",                     //请求方式
                        fileElementId: "headImg",         //照片路径
                        dataType: "text",                 //控制页面刷新
                        data: {id: response.responseText},  //照片修给后的路径
                        success: function () {           //提交成功
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");

                        }
                    })

                    /*关闭添加操作*/
                    return "hello";
                }
            },
            {
                closeAfterDel: true,   //删除

                //文件上传 afterSubmit在提交之后要做体格响应
                afterSubmit: function () {
                    //异步文件上传
                    $.ajax({
                        url: "${path}/user/deleteUser",
                        type: "post",
                        datatype: "text",
                        success: function () {
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");

                        }
                    })

                    /*关闭删除操作*/
                    return "hello";
                }
            }//删除
        );

        //发送验证码
        $("#basic-addon2").click(function () {

           var phone =  $("#phoneInput").val();  //获取值
            alert(phone);

        });

    });

    /**修改状态*/
    function updateStatus(id, status) {

        /*根据id 状态*/
        // id status
        if (status == 1) {
            /*修改*/
            $.ajax({
                url: "${path}/user/edit",
                type: "post",
                data: {"id": id, "status": "0", "oper": "edit"},
                success: function () {
                    //刷新页面
                    $("#userTable").trigger("reloadGrid")
                }
            });
        } else {
            /*修改*/
            $.ajax({
                url: "${path}/user/edit",
                type: "post",
                data: {"id": id, "status": "1", "oper": "edit"},
                success: function () {
                    //刷新页面
                    $("#userTable").trigger("reloadGrid")
                }
            });
        }
    }


</script>

<%--初始化面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h5>用户展示</h5>
    </div>

    <div class="input-group" style="width: 300px;height: 30px;float: right">
        <input id="phoneInput" type="text" class="form-control" placeholder="请输入验证码" aria-describedby="basic-addon2">
        <span class="input-group-addon" id="basic-addon2">点击发送验证码</span>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户信息</a></li>
    </div>

    <%--按钮--%>
    <div class="panel panel-body">
        <button class="btn btn-success">导出用户信息</button>
    </div>



    <%--表--%>
    <table id="userTable"/>

    <%--分页工具栏--%>
    <div id="userPage"></div>


</div>