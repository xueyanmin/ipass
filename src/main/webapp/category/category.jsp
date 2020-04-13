<%@page pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>

    //懒加载
    $(function () {
        /*初始化表格*/
        pageInit();

        //删除按钮
        $("#delBtn").click(function () {

            //1：判段选择一行
            //selrow:制度属性 最后选择行的ID
            //getGridParam:获取选择返回的参数信息ID
            //rowId:行Id
            //2：确认数据删除
            var rowId = $("#cateTable").jqGrid("getGridParam", "selrow");
            if (rowId != null) {

                //根据行ID选择要删除的一行整行数据
                //getRowData:返回指定行的数据，返回数据类型为name:value，name为colModel中的名称，value为所在行的列的值，
                // 如果根据rowid找不到则返回空
                // 。在编辑模式下不能用此方法来获取数据，它得到的并不是编辑后的值
                //3：调用删除方法 传递相关数据
                var row = $("#cateTable").jqGrid("getRowData", rowId);
                //4:回写提示信息
                $.post("${path}/category/edit", {"id": rowId, "oper": "del"}, function (data) {

                    /*刷新页面*/
                    $("#cateTable").trigger("reloadGrid");
                    //向警告框写入内容
                    $("#showMsg").html(data.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function () {
                        //关闭提示框
                        $("#deleteMsg").hide();
                    }, 3000);
                }, "JSON");


            } else {
             alert("请选着要删除的数据");
            }
        })
    });

    /*初始化表格方法*/
    edit

    function pageInit() {

        /*父表格*/
        $("#cateTable").jqGrid(
            {
                url: "${path}/category/queryByOnePage",
                editurl: "${path}/category/edit",
                datatype: "json",
                autowidth: true,
                rowNum: 8,
                rowList: [8, 10, 20, 30],
                pager: '#catePage',  //分页工具栏
                viewrecords: true,   //显示总条数
                styleUI: "Bootstrap",
                height: "auto",
                viewrecords: true,
                colNames: ['Id', '类别名', '级别'],
                colModel: [
                    {name: 'id', index: 'id', width: 55},
                    {name: 'cateName', editable: true, index: 'invdate', width: 90},
                    {name: 'levels', index: 'levels', width: 100}
                ],

                subGrid: true,  //是否开启子表格
                //1：subgrid_id 点击一行时会在表格中创建一个div，用来容纳子表格，subgrid_id就是div的id
                //2:row_id 点击行的id
                subGridRowExpanded: function (subgridId, rowId) {  //设置子表格相关的属性

                    //复制子表格内容
                    addSubGrid(subgridId, rowId);
                }
            });
        /*父表格分页工具栏*/
        $("#cateTable").jqGrid('navGrid', '#catePage', {
                add: true,
                edit: true,
                del: true,
                edittext: "修改",
                addtext: "添加",
                deltext: "删除"
            },
            {
                closeAfterEdit: true,//关闭对话框
            }, //修改
            {
                closeAfterAdd: true,//关闭对话框
            },
            {
                closeAfterDel: true,//关闭对话框
                //提交之后的操作
                //responseJSON: {message: "删除失败，该类别下有子类别", status: "400"
                afterSubmit: function (response) {
                    //向警告框写入内容
                    $("#showMsg").html(response.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function () {
                        //关闭提示框
                        $("#deleteMsg").hide();
                    }, 3000);  //时间为3秒
                    //关闭对话框必须有返回值
                    return "hello";

                }
            }
        );


    }

    //子表格
    function addSubGrid(subgridId, rowId) {


        var subgridTableId = subgridId + "Table";  //定义子表格table的id
        var pagerId = subgridId + "Page"; //定义子表格工具栏Id

        //在子表格容器中创建子表格和子表格分页工具栏
        $("#" + subgridId).html("<table id='" + subgridTableId + "'/><div id='" + pagerId + "'>");


        //子表格
        $("#" + subgridTableId).jqGrid(
            {
                url: "${path}/category/queryByTwoPage?parentId=" + rowId,//拼接父级Id+行Id
                editurl: "${path}/category/edit?parentId=" + rowId, //拼接父级Id+行Id
                //url: "${path}/category/twoCategory.json",
                // editurl: "${path}/category/edit",
                datatype: "json",
                autowidth: true,
                rowNum: 20,
                pager: "#" + pagerId,
                viewrecords: true,   //显示总条数
                styleUI: "Bootstrap",
                height: "auto",
                colNames: ['Id', '类别名', '级别', '上级类别id'],
                colModel: [
                    {name: 'id', index: 'id', width: 55},
                    {name: 'cateName', editable: true, index: 'invdate', width: 90},
                    {name: 'levels', index: 'name', width: 100},
                    {name: 'parentId', index: 'name', width: 100}

                ]
            });


        //子表格分页工具栏
        $("#" + subgridTableId).jqGrid('navGrid', "#" + pagerId, {
                edit: true,
                add: true,
                del: true,
                edittext: "修改",
                addtext: "添加",
                deltext: "删除"
            },
            {
                closeAfterEdit: true,//关闭对话框
            },
            {
                closeAfterAdd: true,//关闭对话框
            },
            {
                closeAfterDel: true,//关闭对话框
                //提交之后的操作
                afterSubmit: function (response) {
                    //向警告框写入内容
                    $("#showMsg").html(response.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function () {
                        //关闭提示框
                        $("#deleteMsg").hide();
                    }, 3000); //时间为3秒

                    //关闭对话框必须有返回值
                    return "hello";

                }
            }
        );
    }


</script>

<%--初始化一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h5>用户展示</h5>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户信息</a></li>
    </div>

    <%--警告框--%>
    <div id="deleteMsg" class="alert alert-warning alert-dismissible" role="alert" style="width: 300px; display: none">
        <span id="showMsg"/>
    </div>

    <%--按钮组--%>
    <div class="panel panel-body">
        <button id="delBtn" class="btn btn-warning">
            删除类别
        </button>
    </div>


    <%--表--%>
    <table id="cateTable"/>

    <%--分页工具栏--%>
    <div id="catePage"></div>


</div>