<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">    
	<jsp:include page="/head.jsp"/>
	
	<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
	<link type="text/css" rel="stylesheet" href="/FileDiskServer/res/css/FileIcon.css?d=${ sessionScope.URLDateString}">
	<script type="text/javascript">
	 var JsonData;
	 var DirID=-1;
	 var ParentID=0;
	 var SID=0;
	 
	 
		function GetFileList(id)
		{

    		console.log("ID:"+id);
			var $loadingToast = $('#loadingToast');
	        $loadingToast.fadeIn(100);      
			
			$("#FileTable").html("");
			$.ajax({
		        url:"/FileDiskServer/File/GetFileList?id="+id,
		        type:'get',
		        dataType:'Json',
		        success:function(data){
		        	JsonData=data;
		        	$("#GotoFrontDirBtn").attr("PID",data.ParentID);
		        	$("#GotoFrontDirBtn").attr("SID",data.SID);
		        	if (DirID!=data.SID && data.SID>0)
					{
		        		$("#Span"+data.ParentID).append("<span id='Span"+data.SID+"'>><a PID='"+data.ParentID+"' SID='"+data.SID+"' href='javascript:void(0);'>"+data.DirName+"</a></span>");	
					}
		        	DirID=data.SID

		        	if (data.Dir.length+data.File.length==0)
		        	{
		        		$("#FileTable").html("<span class='NoFiles'>无文件</span>");
		        	}
		        	else
		        	{
		        		//console.log(data.Dir.length);
			        	for (var i =0;i<data.Dir.length;i++)
			        	{
			        		AddInfo(data.Dir[i].DirName,"dir",data.Dir[i].DirID);
			        		//console.log(data.Dir[i].DirName);
			        		
			        	}
			        	for (var i =0;i<data.File.length;i++)
			        	{
			        		AddInfo(data.File[i].FileName,data.File[i].FileExe,data.File[i].FileID);
			        		//console.log(data.File[i].FileName);
			        	}
		        	}
	                $loadingToast.fadeOut(100);
		        }
			});	


		}
		
		function AddInfo(name,Exe,ID)
		{		
			var IsFile=""
			if (Exe!="dir")
			{
				IsFile="IsFile";
			}
			var FileName
			if (Exe=='dir')
			{
				FileName=name;
			}
			else
			{
				FileName=name+'.'+Exe;
			}
			var div='<div class="cEefyz mmdqJnq open-enable" style="display: block;" _position="0" _installed="1">'+
			'<div class="aekEOaP DefIcon '+Exe+'-large '+IsFile+'" title="" DivID="'+ID+'">'+
			'<img class="alaQ1kq" style="visibility: hidden;">'+
				'</div>'+
				'<div class="file-name">'+
					'<a node-type="cbqK6b" class="uitPe56" href="javascript:void(0);" title="'+FileName+'">'+FileName+'</a>'+
				'</div>'+
				'<span node-type="EOGexf" class="EOGexf">'+
					'<span class="icon lgwELbP"></span>'+
					'<span class="icon checkgridsmall"></span>'+
				'</span>'+
			'</div>';
			$("#FileTable").append(div);		
		}
		function GotoFrontDir(d)
		{

			var id=$(d).attr("PID");
			var sid=$(d).attr("SID");
			if (id>=0)
			{
				$("#FilePathList").find("#Span"+sid).remove();
				DirID=id;
				console.log("#Span"+id);
			}
			else
			{
				//$("#FilePathList").find("#Span0").remove();
				id=0
			}
			//$("#FilePathList").find("#Span"+sid).remove();
			GetFileList(id);
		}

		function GotoDir(d)
		{
			var sid=$(d).attr("SID");
				$("#FilePathList").find("#Span"+sid+">span").remove();
				DirID=sid;
				console.log("#Span"+sid);
			GetFileList(sid);
		}

		
	    $(function(){
	    	GetFileList(0);	
	    	
			$("#FileTable").on("mouseover",".mmdqJnq",function(){

				$(this).addClass('dscQV9P');
			});
			$("#FileTable").on("mouseout",".mmdqJnq",function(){
				$(this).removeClass('dscQV9P');
				
			});

			$("#FileTable").on("click",".dir-large",function(){
				var id=$(this).attr("DivID");
				if (id<0)
				{
					id=0
				}
				GetFileList(id);
			});
			$("#FileTable").on("click",".IsFile",function(){
				var id=$(this).attr("DivID");
				DownloadFile(id);
			});
			$("#FilePathList").on("click","span>a",function(){
				GotoDir(this);
			});
			$("#GotoFrontDirBtn").on("click",function(){				
				GotoFrontDir(this);
			});   
	    });	
		function AddFile()
		{

			var $loadingToast = $('#loadingToast');
	        $loadingToast.fadeIn(100);      
			
	        
			console.log('AddFile');
			//console.log($("#uploaderInput").val());
			
			var upFileName=$("#uploaderInput")[0].files[0].name;

			console.log($("#uploaderInput")[0]);

			console.log(upFileName);
			
			var index1=upFileName.lastIndexOf(".");
			var index2=upFileName.length;
			

			$("#FileName").val(upFileName.substring(0,index1));
			//$("#FileName").val(encodeURI(encodeURI(upFileName.substring(0,index1))));
			$("#FileExe").val(upFileName.substring(index1+1,index2));
			$("#FilePath").val(DirID);
			
			UploadImage()
			
		}
		function UploadImage()
		{
			var form =$("#itemForm");
			form.ajaxSubmit({
				contentType:"multipart/form-data",
			     dataType: "Json",
			     success: function(msg){
					//$("#msg2").html(msg.FileName);

						var $loadingToast = $('#loadingToast');
				        $loadingToast.fadeOut(100);    
				        GetFileList(DirID);
			     }
			 });
		}


		function NewDir()
		{

			$("#NewDirName").val("");
			$("#NewDirDiv").fadeIn(100);
			

		}

		function SaveNewDir()
		{
			var NewDirName=$("#NewDirName").val();
			if (NewDirName=="")
			{
				alert("请输入【文件夹名称】！");
				return;
			}			
			
			$.ajax({
		        url:"/FileDiskServer/CreateDir?ID="+DirID+"&Name="+encodeURI(encodeURI(NewDirName)),
		        type:'get',
		        dataType:'Json',
		        success:function(data){
					if (data.MsgID!=1)
					{
						alert(data.MsgTest);
					}
					else
					{
						CloseNewDir();
						GetFileList(DirID);
					}
		        }
			});	
		}
		function DownloadFile(id)
		{
			//console.log("/FileServer/File/Download?id="+id);
			window.open("/FileDiskServer/File/Download?id="+id,"_blank");
		}
		
		function CloseNewDir()
		{
			$("#NewDirDiv").fadeOut(100);
			

		}
		
	</script>
	<style type="text/css">
		.cEefyz {
		    width: 120px;
		    height: 127px;
		    float: left;
		    margin: 4px 0 0 6px;
		    text-align: center;
		    border: 1px solid #fff;
		    position: relative;
		}
		
		.cEefyz .aekEOaP {
		    position: relative;
		    margin: 9px auto 0;
		    width: 84px;
		    height: 84px;
		    background-repeat: no-repeat;
		    overflow: hidden;
		}
		.cEefyz .file-name {
		    display: block;
		    white-space: nowrap;
		    text-overflow: ellipsis;
		    overflow: hidden;
		    margin: 6px 5px 5px;
		}
		 .cEefyz.dscQV9P
		 {
		 	border:1px solid #f1f5fa;
		 	border-radius:5px;
		 	background:#f1f5fa
		 }
		 body, button, input, select, textarea {
		    font: 12px/1.5 "Microsoft YaHei",arial,SimSun,"宋体";
		    color: #666;
		}
		 
		 


		 .cEefyz .file-name a, 
		 .cEefyz .file-name a:hover, 
		 .cEefyz .file-name a:active {		
		    color: #424e67;
		    cursor: default;
		    text-decoration: none;		
		}
		
		.NewDirDiv {
			position: fixed;
			z-index: 5000;
			width: 400px;
			height: 120px;
			top: 50%;
			left: 50%;
			margin-left: -200px;
			background: #f8f8f8;
			border-radius: 5px;
			padding: 10px;
			margin-top: -60px;
			border: 1px solid #d5d5d5;;
		}

		#FilePathList span>a
		{
			border: 1px solid #9fa4ff;
			border-radius: 2px;
			color: #000;
			padding-left: 5px;
			padding-right: 5px;
			margin-left: 2px;
			margin-right: 2px;
		}
		
		.Headdiv>div
		{
			float: left;
			margin-left: 20px;
		}

		.Headdiv
		{
		    height: 25px;
		    margin: 5px;
		    width: 100%;
		}
		.Headdiv>div>span
		{
			position: relative;
			margin-left: auto;
			margin-right: auto;
			padding-left: 14px;
			text-align: center;
			text-decoration: none;
		}
		
		.NoFiles
		{
			position: fixed;
			z-index: 5000;
			width: 400px;
			top: 50%;
			left: 50%;
			margin-left: -200px;
			background: #f8f8f8;
			border-radius: 5px;
			padding: 10px;
			margin-top: -70px;
			border: 1px solid #d5d5d5;
			text-align: center;
			font-size: 30px;
		}
	</style>
</head>	
<body>
<div class="Headdiv">
	<div ><span style="width: 84px;"><a href="javascript:void(0);" onclick="NewDir()">【新建文件夹】</a></span></div>
	<div class="qhP7DL " style="white-space: nowrap; position: relative;">
		<span class="menu" style="width: 84px;">
			<a class="" href="javascript:void(0);">【上传文件】
			
				<form name="itemForm"  target="_self" id="itemForm" method="post" style="height:0px;"  action="/FileDiskServer/File/UploadToFileServer" enctype="multipart/form-data;charset=utf-8" >
				
					<input name="FileName" id="FileName" type="text" size=200  style="display:none;height:0px;"><br>
					<input name="FileExe" id="FileExe" type="text" size=200  style="display:none;height:0px;"><br>
					<input name="FilePath" id="FilePath" type="text" size=200  style="display:none;height:0px;"><br>
			   		<input name = "FileData" id="uploaderInput" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer" accept="*" multiple="" type="file" onchange="AddFile()">
			    </form>	
			</a>
		</span>
	</div>
	<div>
	
		<span><a id="GotoFrontDirBtn" href="javascript:void(0);" PID="0">【返回上一层】</a></span>
		<span id="FilePathList" style="margin-left: 0px;padding: 0px;">
			<span id="Span0"><a PID='0' SID='0' href='javascript:void(0);'>文件夹</a></span>
		</span>
		</div>
	</div>
</div>
<div>
<div id ="FileTable"></div>
</div>


<div id="loadingToast" style=" display: none;">
        <div class="weui-mask_transparent"></div>
        <div class="weui-toast">
            <i class="weui-loading weui-icon_toast"></i>
            <p class="weui-toast__content">数据加载中</p>
        </div>
</div>
    
    
<div id="NewDirDiv" style=" display: none;">
        <div class="weui-mask_transparent"></div>
        <div class="NewDirDiv">
	        <div class="weui-cells__title">新建文件夹</div>
	        
			<div class="weui-cells">
	            <div class="weui-cell">
	                <div class="weui-cell__bd">
	                    <input class="weui-input" id="NewDirName" placeholder="请输入文件夹名称" type="text">
	                </div>
	            </div>
	        </div>
			<div class="button-sp-area" style="text-align: center;">
	            <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary" onclick="SaveNewDir()">确定</a>
	            <a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_warn" onclick="CloseNewDir()">取消</a>
	        </div>
        	        
        </div>
</div>
    
    

</body>
</html>
