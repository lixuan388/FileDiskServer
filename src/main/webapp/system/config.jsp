<%@page import="com.ecity.java.web.system.Config"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
	<jsp:include page="/head.jsp"/>
<title>Insert title here</title>
<style type="text/css">
	.input-group > .input-group-addon
	{
		width:300px;
		text-align: left;
	}
</style>
</head>
<body>

<%
	String tomcatPath=System.getProperty("catalina.home");
	String ConfigPath=tomcatPath+"\\conf\\webConfig.properties";
	System.out.println(ConfigPath);

	Config c = new Config(ConfigPath);
	c.load();
	// TODO Auto-generated catch block
	
	String [] valueList=new String []{"FileDiskServer.SQLConnect.Url",
				"FileDiskServer.SQLConnect.DriverClassName","FileDiskServer.SQLConnect.Username",
				"FileDiskServer.SQLConnect.Password","FileDiskServer.FileServerPath"};
	

	
%>

<div style="margin: 50px;">
	<div class="form-group" >
		<% 
			for (int i =0;i<valueList.length;i++)
			{
			%>
			<div class="input-group">
				<span class="input-group-addon" id="basic-addon1"><%=valueList[i]%></span>
				<input type="text" class="form-control" placeholder="" aria-describedby="basic-addon1" FieldName="<%=valueList[i]%>" value="<%=c.getProperty(valueList[i])%>">
			</div>
			<%
			}
		%>
			
	</div>
	<div style="margin: 10px;">
		<a style="width:100%  " class="btn btn-primary btn-sm" href="javascript:void(0);" onclick="Save()"  role="button" >保存</a>
	</div>
</div>
<%
	
%>

<script type="text/javascript">
	function Save()
	{
		var Data=[];
		$("[FieldName]").each(function(index){
			console.log("index:"+index);
			console.log("Name:"+$(this).attr("FieldName"));
			console.log("Value:"+$(this).val());
			var v={'name':$(this).attr("FieldName"),'value':$(this).val()};
			Data[index]=v;
		})
		console.log(Data);
    $.post("<%=request.getContextPath()%>/web/system/SetConfig.json",JSON.stringify(Data),function(data){
        console.log(data); 
        alert(data.MsgText);
        
    },"json");
		
		
	}

</script>

</body>
</html>