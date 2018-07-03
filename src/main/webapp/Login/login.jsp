<%@page import="com.java.version"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  request.getSession().setAttribute("UserID","");
  String d=version.Version;
  String LastUrl=request.getParameter("LastUrl")==null?"":(String)request.getParameter("LastUrl");
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<jsp:include page="/layuihead2018.jsp" />
<link href="<%=request.getContextPath() %>/Login/login.css" rel="stylesheet">
<link href="/Res/css/bootstrap.css?d=<%=d%>" rel="stylesheet">
<title>登录</title>
</head>
<body>
  <div id="login">
    <h3>用户登入</h3>
    <img class="avator" src="<%=request.getContextPath() %>/Login/image/avatar.gif" width="96" height="96" />
    <div class="input-group">
      <span class="input-group-addon" aria-hidden="true" id="basic-addon1"> <span class=" glyphicon glyphicon-user" aria-hidden="true"></span>
      </span> <input id="UserName" type="text" class="form-control" placeholder="请输入用户名" aria-describedby="basic-addon1">
    </div>
    <div id="UserNameAlert" class="alert alert-danger fade in" style="display: none;">
      <strong></strong>
    </div>
    <div class="input-group">
      <span class="input-group-addon" aria-hidden="true" id="basic-addon2"> <span class=" glyphicon glyphicon-lock" aria-hidden="true"></span>
      </span> <input id="PassWord" type="password" class="form-control" placeholder="请输入密码" aria-describedby="basic-addon2">
    </div>
    <div id="PassWordAlert" class="alert alert-danger fade in" style="display: none;">
      <strong></strong>
    </div>
    <div class="input-group" style="margin-left: auto; margin-right: auto;">
      <a href="javascript:Login();" class="btn btn-success btn-sm btn-block" role="button" style="width: 150px;margin-left: auto;margin-right: auto;">登录</a>
    </div>
  </div>
  <script type="text/javascript">
  function Login()
  {
    var UserCode = $('#UserName').val();
    var PassWord = $('#PassWord').val();
    $('#UserNameAlert').hide(500);
    $('#PassWordAlert').hide(500);
    if (UserCode == '')
    {
      $('#UserNameAlert strong').html('请输入用户名');
      $('#UserNameAlert').show(500);
      return;
    }
    $.post('<%=request.getContextPath() %>/Login/CheckLogin', {
      'UserCode': UserCode,
      'PassWord': PassWord
    }, function (data) {
      console.log(data);
      if (data.MsgID == - 1)
      {
        $('#UserNameAlert strong').html(data.MsgTest);
        $('#UserNameAlert').show(500);
        $('#UserName').focus();
        $('#UserName').select();
      } 
      else if (data.MsgID == - 2)
      {
        $('#PassWordAlert strong').html(data.MsgTest);
        $('#PassWordAlert').show(500);
        $('#PassWord').focus();
        $('#PassWord').select();
      } 
      else
      {
        layui.data(layuiTableName,{key:"config",value:data.Config});
        layui.data(layuiTableName+".login",{key:"UserName",value:UserCode});
        <%
        if (LastUrl.equals(""))
        {
          %>
          var url='<%=request.getContextPath() %>/Content/index.html';
          <%
        }
        else
        {
          %>
          var url="<%=LastUrl%>";
          <%
        }
        %>
        //console.log(url);
        window.location.href=url;
      }
    }, 'json');
  }
  $(function () {
    $('#UserName').keyup(function (event) {
      if (event.which == 13) {
        $('#PassWord').focus();
        $('#PassWord').select();
      }
    });
    $('#PassWord').keyup(function (event) {
      if (event.which == 13) {
        Login();
      }
    });
    var UserName=layui.data(layuiTableName+".login").UserName?layui.data(layuiTableName+".login").UserName:"";
    $("#UserName").val(UserName);
  })
  </script>
</body>
</html>