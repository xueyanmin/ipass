<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>持明法州后台管理系统</title>
    <link rel="icon" href="${path}/bootstrap/img/arrow-up.png" type="image/x-icon">
    <link rel="stylesheet" href="${path}/bootstrap/css/bootstrap.css">

    <%--引入jqgrid中主题css--%>
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/css/css/hot-sneaks/jquery-ui-1.8.16.custom.css">
    <link rel="stylesheet" href="${path}/bootstrap/jqgrid/boot/css/trirand/ui.jqgrid-bootstrap.css">
    <%--引入js文件--%>
    <script src="${path}/bootstrap/js/jquery.min.js"></script>
    <script src="${path}/bootstrap/js/bootstrap.js"></script>
    <script src="${path}/bootstrap/jqgrid/js/i18n/grid.locale-cn.js"></script>
    <script src="${path}/bootstrap/jqgrid/boot/js/trirand/jquery.jqGrid.min.js"></script>
    <script src="${path}/bootstrap/js/ajaxfileupload.js"></script>

</head>
<body>
  <%--导航条标题右边--%>
  <nav class="navbar navbar-default">
      <div class="container-fluid">
          <!-- 导航条右边内容 -->
          <div class="navbar-header">
              <a class="navbar-brand" href="#">应学视频APP后台管理系统</a>
          </div>

          <!-- 导航条右边内容 -->
          <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
              <ul class="nav navbar-nav navbar-right">
                  <li><a href="#">欢迎：<span class="text text-danger">${admin.username}</span> </a></li>
                  <li><a href="${path}/admin/logout">退出<span class="glyphicon glyphicon-log-out"/></a></li>
              </ul>
          </div>
      </div>
  </nav>

  <%--栅格系统--%>
  <div class="container-fluid">
      <div class="row">
          <div class="col-md-2">

              <!--左边手风琴部分-->
              <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
                  <div class="panel panel-info" align="center">
                      <div class="panel-heading" role="tab" id="headingOne">
                          <h4 class="panel-title">
                              <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                                  用户管理
                              </a>
                          </h4>
                      </div>
                      <div id="collapseOne" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingOne">
                          <div class="panel-body">
                              <button class="btn btn-info">
                                  <a href="javascript:$('#MainId').load('${path}/user/user.jsp')">用户展示</a>
                              </button><br/><br/>
                              <button class="btn btn-info"><a href="javascript:$('#MainId').load('${path}/test/echartsGoEasy.jsp')">用户统计</a></button><br/><br/>
                              <button class="btn btn-info"> <a href="javascript:$('#MainId').load('${path}/test/chinaGoEasy.jsp')">用户分布</a></button><br/><br/>
                          </div>
                      </div>
                  </div>
                  <div class="panel panel-primary" align="center">
                      <div class="panel-heading" role="tab" id="headingTwo">
                          <h4 class="panel-title">
                              <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo">
                                 分类管理
                              </a>
                          </h4>
                      </div>
                      <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                          <div class="panel-body">
                              <button class="btn btn-primary">
                                  <a href="javascript:$('#MainId').load('${path}/category/category.jsp')">分类展示</a>
                                  </button><br/><br/>
                          </div>
                      </div>
                  </div>
                  <div class="panel panel-warning" align="center">
                      <div class="panel-heading" role="tab" id="headingThree">
                          <h4 class="panel-title">
                              <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree" aria-expanded="false" aria-controls="collapseThree">
                                  视频管理
                              </a>
                          </h4>
                      </div>
                      <div id="collapseThree" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingThree">
                          <div class="panel-body">
                              <button class="btn btn-warning">
                                  <a href="javascript:$('#MainId').load('${path}/video/video.jsp')">视屏展示</a>
                              </button><br/><br/>
                              <button class="btn btn-warning">
                                  <a href="javascript:$('#MainId').load('${path}/video/searchVideo.jsp')">视屏检索</a>
                              </button>
                          </div>
                      </div>
                  </div>
                  <div class="panel panel-success" align="center">
                      <div class="panel-heading" role="tab" id="headingTree">
                          <h4 class="panel-title">
                              <a class="collapsed" role="button" data-toggle="collapse" data-parent="#accordion" href="#collapseThree">
                                  反馈管理
                              </a>
                          </h4>
                      </div>
                      <div id="collapseFish" class="panel-collapse collapse" role="tabpanel" aria-labelledby="collapseFish">
                          <div class="panel-body">
                              <button class="btn btn-success">反馈展示</button>
                          </div>
                      </div>
                  </div>
              </div>
          </div>

          <!--右边巨幕开始-->
          <div style="height: 1000px;" class="col-md-9" id="MainId" >
              <div class="jumbotron">
                  <h2>欢迎来到应学视频APP后台管理系统</h2>
              </div>

          <%--右边轮播图部分--%>
          <div id="carousel-example-generic" class="carousel slide" data-ride="carousel">
              <!-- Indicators -->
              <%--中间按钮--%>
              <ol class="carousel-indicators">
                  <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
                  <li data-target="#carousel-example-generic" data-slide-to="1"></li>
                  <li data-target="#carousel-example-generic" data-slide-to="2"></li>
                  <li data-target="#carousel-example-generic" data-slide-to="3"></li>
              </ol>

              <!-- 图片 -->
              <div class="carousel-inner" role="listbox" align="center">
                  <div class="item active">
                      <%--本地图片展示--%>
                    <%-- <img src="${path}/bootstrap/img/pic1.jpg" alt="...">--%>
                     <%-- 网络图片展示<img src="https://yingx-185.oss-cn-beijing.aliyuncs.com/photo/金色小猫.jpg" alt="...">--%>
                      <%--网络视屏展示--%>
                         <video src="https://yingx-185.oss-cn-beijing.aliyuncs.com/红金宝车.mp4" controls
                                poster="https://yingx-185.oss-cn-beijing.aliyuncs.com/1585834590213-4.jpg"/><%--视频封面展示--%>
                      <div class="carousel-caption">
                          ...
                      </div>
                  </div>
                  <div class="item">
                      <img src="${path}/bootstrap/img/pic2.jpg" alt="...">
                      <div class="carousel-caption">
                          ...
                      </div>
                  </div>
                  <div class="item">
                      <img src="${path}/bootstrap/img/pic3.jpg" alt="...">
                      <div class="carousel-caption">
                          ...
                      </div>
                  </div>
                  <div class="item">
                      <img src="${path}/bootstrap/img/pic4.jpg" alt="...">
                      <div class="carousel-caption">
                          ...
                      </div>
                  </div>
                  ...
              </div>

              <!-- Controls -->
              <a class="left carousel-control" href="#carousel-example-generic" role="button" data-slide="prev">
                  <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
                  <span class="sr-only">Previous</span>
              </a>
              <a class="right carousel-control" href="#carousel-example-generic" role="button" data-slide="next">
                  <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
                  <span class="sr-only">Next</span>
              </a>
          </div>
          </div>
      </div>
  </div><br/>
     <!--页脚-->
   <div class="panel panel-footer" align="center">@百知教育zhangcn@zaprkhr.com</div>
</body>
</html>
