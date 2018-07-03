<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">    
	
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
	<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
	<link type="text/css" rel="stylesheet" href="<%=request.getContextPath() %>/res/css/FileIcon.css?d=${ sessionScope.URLDateString}">
	
	
    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath() %>/res/css/bootstrap.css" rel="stylesheet">
    <link href="<%=request.getContextPath() %>/Content/index.css" rel="stylesheet">
	<title>文件服务</title>
	
<style type="text/css">
	#MsgDiv>.Headdiv
	{
	  display: inline-block;
	  width: auto; 
	  
	}
</style>	
</head>	
<body>

<nav class="navbar navbar-default navbar-fixed-top">
  <div class="container" style="width:100%">
     <div class="navbar-header">
       <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
         <span class="sr-only">Toggle navigation</span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
         <span class="icon-bar"></span>
       </button>
       <a class="navbar-brand" href="<%=request.getContextPath() %>/Content/index.jsp">文件服务</a>
     </div>
     <div id="navbar" class="collapse navbar-collapse">
       <ul class="nav navbar-nav">

			<li  data-toggle="tooltip" data-placement="bottom" title="在当前文件夹下新建新的文件夹"><a href="javascript:void(0);"   data-toggle="modal" data-target="#NewDirDiv"  ><div ><span style="width: 84px;">【新建文件夹】</span></div></a></li>
			<li  data-toggle="tooltip" data-placement="bottom" title="上传文件至当前文件夹"><a class="" href="javascript:void(0);"><div class="qhP7DL " style="white-space: nowrap; position: relative;" >
                    <span class="menu" style="width: 84px;">【上传文件】              
                        <input title="点击选择文件" id="uploaderInput" multiple="" accept="*/*" 
                            name="uploaderInput" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" type="file"
                             onchange="UpLoadFileList(this);">      
                    </span>
                </div></a>
            </li>
            <li  data-toggle="tooltip" data-placement="bottom" title="批量上传文件至当前文件夹"><a class="" href="javascript:void(0);"><div class="qhP7DL " style="white-space: nowrap; position: relative;" >
                    <span class="menu" style="width: 84px;color:#77afff ;">【上传文件夹】              
                        <input title="点击选择文件" id="uploaderInput" multiple="" accept="*/*" webkitdirectory=""
                            name="uploaderInput" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" type="file"
                             onchange="UpLoadFileList(this);">      
                    </span>
                </div></a>
                
            </li>
			<li  data-toggle="tooltip" data-placement="bottom" title="返回上层文件夹"><a id="GotoFrontDirBtn" href="javascript:void(0);" PID="0">
				<div>			
					<span>【返回上一层】</span>
				</div></a>
			</li>
       </ul>
       <!-- 
       <ul class="nav navbar-nav navbar-right">
	      <li>		
	        <div class="input-group">
			  <input type="text" class="form-control" placeholder="查找文件" id="SearchFileInput">
			  <span class="input-group-btn">
			    <button class="btn btn-default" type="button" id="SearchFileBtn">查询</button>
			  </span>
			</div>
		</li>
       </ul>
        -->

	    <ul class="nav navbar-nav navbar-right">
	    <li><a href="javascript:void(0);" onclick="ChangePassWord()"  ><div ><span style="width: 84px;color:red;">【修改密码】</span></div></a></li>
	<% 
		if ((Boolean)request.getSession().getAttribute("IsAdmin"))
		{
	%>    
	      <li>	<a href="<%=request.getContextPath() %>/Content/manger/UserInfo.jsp"    ><div ><span style="width: 84px;color:red;">【账号管理】</span></div></a></li>
	<%			
			
		}
	
	%> 
       </ul>
       

       
       
     </div><!--/.nav-collapse -->
    </li>
  </ul>
  </div>			    
</nav>
    
<div class="Headdiv">
	<ol class="breadcrumb" id="FilePathList" >
		  <li  id="Span0" class="active"  PID='0' SID='0'><span>Home</span></li>
	</ol>
</div>    


<div id="MsgDiv" style="max-height:100px;overflow-y: auto;overflow-x: hidden;">
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
    
<!-- 文件框 -->
<div id="FileListTempate" class="cEefyz mmdqJnq open-enable " style="display:none;" title="File">
	<div class="aekEOaP DefIcon" title="UploadFile" ><img class="alaQ1kq" style="visibility: hidden;">
	</div>
	<div class="file-name">
		<a class="uitPe56" href="javascript:void(0);" title="File">File			
		</a>
	</div>
	<span class="EOGexf">
		<span class="icon lgwELbP">
		</span>
		<span class="icon checkgridsmall">
		</span>
	</span>
	<div class="download row" style="z-index: 99999;position: relative;padding-left: 15px;padding-right: 15px;">
		<button type="button" class="btn btn-success btn-xs DownloadBtn col-md-6" >下载</button>
		<div  class="btn btn-info btn-xs UploadBtn col-md-6" style="white-space: nowrap; position: relative;">
					<span class="menu" style="width: 84px;">
						更新						
						<input title="点击选择文件" id="uploaderInput" multiple="" accept="*/*" FileName=""
                            name="uploaderInput" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" type="file"
                             onchange="UpLoadFileList(this);">     
					</span>
		</div>
	</div>
	<div class="Delete" style="z-index: 99999;position: relative;">		
		<button type="button" class="btn btn-danger btn-xs DeleteBtn" style="width:100%">删除</button>
	</div>
</div>

<!-- 新建文件夹 -->
<!--
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
 -->

<div class="modal fade" id="NewDirDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">新建文件夹</h4>
      </div>
      <div class="modal-body">
        <!--<div class="input-group">-->
		  <!--  <span class="input-group-addon" id="basic-addon1">名称：</span>-->
		  <input type="text" id="NewDirName" class="form-control" placeholder="请输入文件夹名称" aria-describedby="basic-addon1">
		<!--</div>-->
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary"  onclick="SaveNewDir();">确认</button>
      </div>
    </div>
  </div>
</div>


<div class="modal fade" id="ChangePassWordDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">修改密码</h4>
      </div>
      <div class="modal-body">
        <div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">旧密码：</span>
		  <input type="text" id="PassWord1" class="form-control" placeholder="请输入旧密码" aria-describedby="basic-addon1">
		</div>
        <div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">新密码：</span>
		  <input type="text" id="PassWord2" class="form-control" placeholder="请输入新密码" aria-describedby="basic-addon1">
		</div>
        <div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">再次确认：</span>
		  <input type="text" id="PassWord3" class="form-control" placeholder="请输入新密码" aria-describedby="basic-addon1">
		</div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary"  onclick="SaveChangePassWord();">确认</button>
      </div>
      <div id="ErrMsg" class="modal-footer" style="display: none;">
      	<div class="alert alert-danger fade in" >
		    <strong></strong>
		</div>
      </div>
    </div>
  </div>
</div>


<!-- 删除确认 -->
<div class="modal fade" id="DeleteConfirm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">删除确认</h4>
      </div>
      <div class="modal-body">
      	<span>是否确认删除此项目？</span>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary" onclick="DeleteConfirm();">确认删除</button>
      </div>
      
    </div>
  </div>
</div>
    

<script type="text/javascript">

<%@ include file = "/Content/index.js" %>
</script>
    
    <script type="text/javascript">

    function SaveChangePassWord()
    {
    	
    	$('#ChangePassWordDiv #ErrMsg').hide();

    	var PassWord1=$('#ChangePassWordDiv #PassWord1').val();
    	var PassWord2=$('#ChangePassWordDiv #PassWord2').val();
    	var PassWord3=$('#ChangePassWordDiv #PassWord3').val();

    	if (PassWord2!=PassWord3)
    	{
      		$('#ChangePassWordDiv #ErrMsg strong').html("两次输入的新密码不一致！");
      		$('#ChangePassWordDiv #ErrMsg').show(500);	
      		return;
    	}
    		
    	var json={"PassWord1":PassWord1,"PassWord2":PassWord2}
    	$.post("<%=request.getContextPath() %>/Content/System/ChangePassWord.json",json,function(data){
    	    //console.log(data);
    	    if (data.MsgID!=1)
    	    {
    	    	$('#ChangePassWordDiv #ErrMsg strong').html(data.MsgTest);
    		  	$('#ChangePassWordDiv #ErrMsg').show(500);		    	  
    	    }
    	    else
    	 	{
    	
    			$('#ChangePassWordDiv #ErrMsg').hide();
    	  		$('#ChangePassWordDiv').modal('hide');
    	 	}		      
    	},"json");
    }    
    </script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>
