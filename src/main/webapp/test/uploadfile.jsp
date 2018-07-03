<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>    
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">

<title>Insert title here</title>

</head>
<body>

<h>0</h>
<div>
	<!-- Split button -->
	<div id="uploadbutton" class="btn-group">
	  <button type="button" class="btn btn-default">上传</button>
	  <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	    <span class="caret"></span>
	    <span class="sr-only">Toggle Dropdown</span>
	  </button>
	  <ul class="dropdown-menu">
	    <li><a href="javascript:hidebutton();" style="position: relative;">上传文件
                <input title="点击选择文件夹" id="h5Input1" multiple="" accept="*/*" 
                    name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" type="file"
                     onchange="LoadFileList();">

	    </a></li>
	    <li><a href="javascript:hidebutton();" style="position: relative;">上传文件夹
                <input title="点击选择文件夹" id="h5Input2" multiple="" webkitdirectory="" accept="*/*" name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" type="file">
 
	    </a></li>
	  </ul>
	</div>
</div>


<div id="FileListMsg"></div>

<div style="position: relative;">
            <form name="itemForm"  target="_self" id="itemForm" method="post"  action="" enctype="multipart/form-data" >
                <input title="点击选择文件夹" id="h5Input13" multiple="" accept="*/*" 
                    name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" 
                    type="file" onchange="LoadFileList();">
            </form>  

            <form name="itemForm"  target="_self" id="itemForm" method="post"  action="" enctype="multipart/form-data" >
                <input title="点击选择文件夹" id="h5Input23" multiple="" webkitdirectory="" accept="*/*" name="html5uploader" style="position:absolute;opacity:0;top:0;left:0;width:100%;height:100%;cursor:pointer;" type="file">
            </form>   
</div>
<h>1</h>
    <form name="itemForm"  target="_self" id="itemForm" method="post"  action="" enctype="multipart/form-data" >
        <input name="FileName" id="FileName" type="text" size=200><br>
        <input name="FileExe" id="FileExe" type="text" size=200><br>
        
            <input name = "FileData" id="uploaderInput" accept="*" multiple="" type="file" onchange="AddFile()">
    </form> 
    
<h>2</h>    
<form name="itemForm"  target="_self" id="itemForm" method="post"  action="" enctype="multipart/form-data" >
    <input title="点击选择文件夹" id="h5Input2" multiple="" webkitdirectory="" accept="*/*" name="html5uploader" style="" type="file">

    </form>     
    
    
    
<script type="text/javascript">
  function hidebutton()
  {
	  $('#uploadbutton').removeClass('open');
  }
  function LoadFileList()
  {
	  
	  $("#FileListMsg").html("");
	  $("#FileListMsg").append("<div>length:"+$("#h5Input1")[0].files.length+"</div>");
	  for (i=0;i<$("#h5Input1")[0].files.length;i++)
	  {  
		  var file=$("#h5Input1")[0].files[i];
		  UpdateFile(file);		    
	  }
  }
  function UpdateFile(file)
  {
	  $.ajax({
	        url:"/FDS/File/GetProgressKey?d="+new Date(),
	        type:'get',
	        dataType:'Json',
	        success:function(data){
	            //console.log("ProgressKey:"+data.ProgressKey);

	            var ProgressKey=data.ProgressKey;
	            $("#FileListMsg").append("<div id="+ProgressKey+">name:"+file.name+"/"+ProgressKey+"/<span></span></div>");
	            var formData = new FormData();
	               formData.append('FileData', file);
	               formData.append('FileName', file.name);
                   formData.append('FileExe', 'txt');
                   formData.append('ProgressKey', ProgressKey);
	               formData.append('FilePath', 0);
	               var filename=file.name;
	               $.ajax({
	                     url: '/FDS/testupload',
	                 //    url: '/FDS/File/UploadToFileServer?ProgressKey='+ProgressKey,
	                 //url: '/FDS/File/UploadToFileServer',
	                 type: 'POST',
	                 cache: false,
	                 data: formData,
	                 processData: false,
	                 contentType: false,
	                 success: function(data){
	                     //console.log(json);
	                     $("#FileListMsg").append("<div>"+filename+"--"+JSON.stringify(data)+"</div>");
	                  }                        
	               });
	               function getProgress(ProgressKey)
	               {

	                   //console.log('getProgress');
	                   $.ajax({
	                       url:"/FDS/File/GetUploadProgress?id="+data.ProgressKey+"&d="+new Date(),
	                       type:'get',
	                       dataType:'Json',
	                       success:function(data){
	                           //console.log(data.type);
	                           var ProgressKey=data.ProgressKey;
	                           
                               $("#FileListMsg").append("<div>getProgress:"+ProgressKey+"/"+JSON.stringify(data)+"</div>");
                               
	                           if (data.type == 'doPost')
	                           {
	                               $("#FileListMsg #"+ProgressKey+" span").html('在正准备上传');
	                               //console.log('在正准备上传'+ new Date());
	                           } 
	                           else
	                           if (data.type=='Temp')
	                           {
	                               $("#FileListMsg #"+ProgressKey+" span").html('正在上传：'+data.size+'/'+data.progress);
	                               //console.log('正在上传：'+data.size+'/'+data.progress);
	                           }
	                           else if (data.type=='Loacl')
	                           {
	                               $("#FileListMsg #"+ProgressKey+" span").html('正在保存：'+data.size+'/'+data.progress);
	                               //console.log('正在保存：'+data.size+'/'+data.progress);
	                           }
	                           else if (data.type=='success')
	                           {
	                               $("#FileListMsg #"+ProgressKey+" span").html('上传成功！');
	                               //console.log('上传成功！');
	                           }
	                           if (data.type!='success' && data.type!=null)
	                           {
	                               setTimeout(getProgress(ProgressKey),1000);
	                           }

	                           
	                       }
	                   }); 
	               }
	              // getProgress(ProgressKey);	            
	        }
	    });
  }
  
  </script>

    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
 
</body>
</html>