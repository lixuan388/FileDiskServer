<%@page import="com.java.version"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
String InitialScale=request.getParameter("InitialScale")==null?"1":(String)request.getParameter("InitialScale");
//String webPath="http://ls.17ecity.cc:18888";
String webPath="";
String d="?d="+version.Version;
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" id="webviewport" content="width=device-width,initial-scale=<%=InitialScale %>,user-scalable=0">
<link rel="stylesheet" href="<%=webPath%>/Res/css/jquery-ui.css<%=d%>">
<script src="<%=webPath%>/Res/js/jquery.min.js<%=d%>"></script>
<script src="<%=webPath%>/Res/js/jquery-ui.js<%=d%>"></script>
<script src="<%=webPath%>/Res/js/bootstrap.min.js<%=d%>"></script>
<script type="text/javascript" src="<%=webPath%>/Res/js/jquery.tmpl.js<%=d%>"></script>
<script type="text/javascript" src="/Res/js/layer/layer/layer.js"></script>
<script type="text/javascript" src="<%=webPath%>/Res/js/ECityAlert.js<%=d%>"></script>
<script type="text/javascript" src="<%=webPath%>/Res/js/date.js<%=d%>"></script>
<script src="<%=webPath%>/Res/js/bootstrap-datetimepicker.min.js<%=d%>"></script>
<script type="text/javascript" src="<%=webPath%>/Res/js/bootstrap-datetimepicker.zh-CN.js<%=d%>"></script>
<link href="<%=webPath%>/Res/css/bootstrap-datetimepicker.min.css<%=d%>" rel="stylesheet">
<link href="<%=webPath%>/Res/css/bootstrap.css<%=d%>" rel="stylesheet">
<link href="<%=request.getContextPath() %>/res/css/Default.css<%=d%>" rel="stylesheet">
<script type="text/javascript" src="/Res/js/layer/layui/layui.js"></script>
<%
  request.setCharacterEncoding("UTF-8") ;  //解决中文乱码的问题
%>
