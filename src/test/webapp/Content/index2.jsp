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
       <a class="navbar-brand" href="/FileDiskServer/Content/index2.jsp">文件服务</a>
     </div>
     <div id="navbar" class="collapse navbar-collapse">
       <ul class="nav navbar-nav">
			<!-- <li data-toggle="tooltip" data-placement="bottom" title="在当前文件夹下新建新的文件夹"><a  href="/FileDiskServer/Content/IteratorPath.jsp" ><div><span style="width: 84px;">【手动更新文件】</span></div></a></li> -->
			
			
			<li ><a href="javascript:void(0);" onclick="NewDir()"><div ><span style="width: 84px;">【新建文件夹】</span></div></a></li>
			<li  data-toggle="tooltip" data-placement="bottom" title="在当前文件夹下新建新的文件夹"><a href="javascript:void(0);"   data-toggle="modal" data-target="#NewDirDiv"  ><div ><span style="width: 84px;">【新建文件夹】</span></div></a></li>
			<li  data-toggle="tooltip" data-placement="bottom" title="上传文件至当前文件夹"><a class="" href="javascript:void(0);"><div class="qhP7DL " style="white-space: nowrap; position: relative;" >
			<span class="menu" style="width: 84px;">
				【上传文件】						
				<form name="itemForm"  target="_self" id="itemForm" method="post" style="height:0px;"  action="/FileDiskServer/File/UploadToFileServer" enctype="multipart/form-data;charset=utf-8" >
				
					<input name="FileName" id="FileName" type="text" size=200  style="display:none;height:0px;"><br>
					<input name="FileExe" id="FileExe" type="text" size=200  style="display:none;height:0px;"><br>
					<input name="FilePath" id="FilePath" type="text" size=200  style="display:none;height:0px;"><br>
			   		<input name = "FileData" id="uploaderInput" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer" accept="*" multiple="" type="file" onchange="AddFile(this)" FileName="">
					    </form>	
					</span>
				</div>
				</a>
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
<div id="MsgDiv">
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
						<form name="itemForm"  target="_self" id="itemForm" method="post" style="height:0px;"  action="/FileDiskServer/File/UploadToFileServer" enctype="multipart/form-data;charset=utf-8" >
						
							<input name="FileName" id="FileName" type="text" size=200  style="display:none;height:0px;"><br>
							<input name="FileExe" id="FileExe" type="text" size=200  style="display:none;height:0px;"><br>
							<input name="FilePath" id="FilePath" type="text" size=200  style="display:none;height:0px;"><br>
					   		<input name = "FileData" id="uploaderInput" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer" accept="*" multiple="" type="file" onchange="AddFile(this)">
					    </form>	
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
    
    <script src="<%=request.getContextPath() %>/Content/index.js"></script>
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>
