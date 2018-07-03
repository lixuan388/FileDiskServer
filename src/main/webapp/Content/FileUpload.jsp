<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">	
    
	<meta http-equiv="Cache-control" content="no-cache">
	<meta http-equiv="Cache" content="no-cache">
    
	<script type="text/javascript" src="/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
</head>
<body>    

<div>
<%
	java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMddHHmmss");   
	java.util.Date currentTime = new java.util.Date();//得到当前系统时间   
	String str_date1 = formatter.format(currentTime); //将日期时间格式化  

	 
%>



	<form name="itemForm"  target="_self" id="itemForm" method="post"  action="/FileServer/File/UploadToFileServer?<%= str_date1 %>" enctype="multipart/form-data" >
		<input name="FileName" id="FileName" type="text" size=200><br>
		<input name="FileExe" id="FileExe" type="text" size=200><br>
		
   			<input name = "FileData" id="uploaderInput" accept="*" multiple="" type="file" onchange="AddFile()">
    </form>	
<input type="button" value="上传文件" onclick="UploadImage();">
</div>
<div id="msg">
	

</div>
<div id="msg2">
	

</div>

<script type="text/javascript">

	function AddFile()
	{
		console.log('AddFile');
		//console.log($("#uploaderInput").val());
		
		var upFileName=$("#uploaderInput")[0].files[0].name;

		console.log($("#uploaderInput")[0]);

		console.log(upFileName);
		
		var index1=upFileName.lastIndexOf(".");
		var index2=upFileName.length;
		
		
		$("#FileName").val(upFileName.substring(0,index1));
		$("#FileExe").val(upFileName.substring(index1+1,index2));
		
		
		
	}
    function getObjectURL(file) {  
        var url = null;  
        if (window.createObjcectURL != undefined) {  
            url = window.createOjcectURL(file);  
        } else if (window.URL != undefined) {  
            url = window.URL.createObjectURL(file);  
        } else if (window.webkitURL != undefined) {  
            url = window.webkitURL.createObjectURL(file);  
        }  
        return url;
    }        
    
	function propertychange(id)
	{
		console.log('propertychange:'+id);
		UploadImage();
	}
	
	function UploadImage()
	{
		var form =$("#itemForm");
		form.ajaxSubmit({
			contentType:"multipart/form-data",
		     dataType: "Json",
		     success: function(msg){
				$("#msg2").html(msg.FileName);
		     }
		 });
		getProgress();
	}
	function getProgress()
	{
		$.ajax({
	        url:"/FDS/File/GetUploadProgress?d="+new Date(),
	        type:'get',
	        dataType:'Json',
	        success:function(data){

	            if (data.type == 'doPost')
	            {
	              $('#msg').html('在正准备上传'+ new Date());
	            } 
	            else
	        	if (data.type=='Temp')
	        	{
	        		$('#msg').html('正在上传：'+data.size+'/'+data.progress);
	        	}
	        	else if (data.type=='Loacl')
	        	{
	        		$('#msg').html('正在保存：'+data.size+'/'+data.progress);
	        	}
	        	else if (data.type=='success')
	        	{
	        		$('#msg').html('上传成功！');
	        	}
				if (data.type!='success' && data.type!=null)
				{
					setTimeout(getProgress,1000);
				}
	        }
		});	
	}
	
	
	
</script>
</body>
</html>