<%@page import="com.java.version"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String d="?d="+version.Version;
%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<script src="/Res/js/jquery.min.js<%=d%>"></script>
<script type="text/javascript" src="/Res/js/date.js<%=d%>"></script>
<script type="text/javascript" src="/Res/js/layer/layer/layer.js"></script>
<script type="text/javascript" src="/Res/js/ECityAlert.js<%=d%>"></script>
<script type="text/javascript" src="/Res/js/layer/layui/layui.js"></script>
<script src="<%=request.getContextPath() %>/layuiadmin/layui/init.js"></script>
<!--  <link href="/Res/js/layer/layui/css/layui.css<%=d%>" rel="stylesheet">-->
 <link href="<%=request.getContextPath() %>/layuiadmin/layui/css/layui.css<%=d%>" rel="stylesheet">

<link href="<%=request.getContextPath() %>/res/css/Default.css<%=d%>" rel="stylesheet">
<%
	request.setCharacterEncoding("UTF-8") ;  //解决中文乱码的问题
%>
