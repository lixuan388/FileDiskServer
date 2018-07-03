 var JsonData;
 var DirID=-1;
 var ParentID=0;
 var SID=0;
 
 
	function GetFileList(id)
	{

		//console.log("ID:"+id);
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
        		$("#Span"+data.ParentID).after("<span class=\"spantag"+data.ParentID+"\">></span><span id='Span"+data.SID+"' class=\"btn btn-default btn-xs\" PID='"+data.ParentID+"' SID='"+data.SID+"' href='javascript:void(0);'>"+data.DirName+"</span>");	
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
	var div=$("#FileListTempate").clone();
	div.attr("ID","FileListDivID_"+ID)
	div.addClass(Exe);
	div.css("display","block");
	div.attr("title",FileName);
	div.find(".DefIcon").addClass(Exe+"-large").addClass(IsFile).attr("title",FileName).attr("DivID",ID);
	div.find(".DownloadBtn").attr("DivID",ID);
	div.find(".UploadBtn").attr("DivID",ID);
	div.find(".uitPe56").attr("title",FileName).html(FileName);
	div.find(".download").attr("title",FileName);
	div.find("#uploaderInput").attr("FileName",FileName);
	
	
	
	
	/*
	var div='<div class="cEefyz mmdqJnq open-enable '+Exe+'" style="display: block;" _position="0" _installed="1"  title="'+FileName+'">'+
	
	'<div class="aekEOaP DefIcon '+Exe+'-large '+IsFile+'" title="'+FileName+'" DivID="'+ID+'">'+
	'<img class="alaQ1kq" style="visibility: hidden;">'+
		'</div>'+
		'<div class="file-name">'+
		'<a node-type="cbqK6b" class="uitPe56" href="javascript:void(0);" title="'+FileName+'">'+FileName+'</a>'+
		'</div>'+
		'<span node-type="EOGexf" class="EOGexf">'+
			'<span class="icon lgwELbP"></span>'+
			'<span class="icon checkgridsmall"></span>'+
		'</span>'+'<a node-type="cbqK6b" class="download" href="javascript:void(0);" style="z-index: 99999;position: relative;" title="'+FileName+'">下载</a>'+
	'</div>';
	*/
	$("#FileTable").append(div);		
}
function GotoFrontDir(d)
{

	var id=$(d).attr("PID");
	var sid=$(d).attr("SID");
	if (id>0)
	{
		$("#FilePathList").find("#Span"+id).nextAll().remove();
		DirID=id;
		//console.log("#Span"+id);
	}
	else
	{
		//$("#FilePathList").find("#Span0").remove();
		id=sid
	}
	//$("#FilePathList").find("#Span"+sid).remove();
	GetFileList(id);
}

function GotoDir(d)
{
	var sid=$(d).attr("SID");
		$("#FilePathList").find("#Span"+sid).nextAll().remove();
		DirID=sid;
		//console.log("#Span"+sid);
	GetFileList(sid);
}

function AddFile(t)
{

	//var $loadingToast = $('#loadingToast');
    //$loadingToast.fadeIn(100);      

	//console.log('AddFile');
	//console.log(t);
	//console.log($("#uploaderInput").val());
	
	var FileName=$(t).attr("FileName");
	
	var upFileName
	if (FileName!="")
	{
		upFileName=FileName;
	}
	else
	{
		upFileName=$(t)[0].files[0].name;
	}

	//console.log($(t)[0]);

	//console.log(upFileName);
	
	var index1=upFileName.lastIndexOf(".");
	var index2=upFileName.length;
	
	$(t).parent().find("#FileName").val(upFileName.substring(0,index1));
	//$("#FileName").val(encodeURI(encodeURI(upFileName.substring(0,index1))));
	$(t).parent().find("#FileExe").val(upFileName.substring(index1+1,index2));
	$(t).parent().find("#FilePath").val(DirID);
	
	

	$.ajax({
        url:"/FileDiskServer/File/GetProgressKey?d="+new Date(),
        type:'get',
        dataType:'Json',
        success:function(data){
			//console.log("ProgressKey:"+data.ProgressKey);
			$("#MsgDiv").append("<div class='Headdiv' id='"+data.ProgressKey+"'>"+upFileName+"/<span></span></div>");
			UploadImage($(t).parent(),data.ProgressKey);
        }
	});	
	
}
function UploadImage(from,ProgressKey)
{	
	var form =$(from);
	form.ajaxSubmit({
		contentType:"multipart/form-data",
	     dataType: "Json",
	     success: function(msg){
			//$("#msg2").html(msg.FileName);

				//var $loadingToast = $('#loadingToast');
		        //$loadingToast.fadeOut(100);    
		        GetFileList(DirID);
	     }
	 });
	function getProgress()
	{

		//console.log('getProgress');
		$.ajax({
	        url:"/FileDiskServer/File/GetUploadProgress?id="+ProgressKey+"&d="+new Date(),
	        type:'get',
	        dataType:'Json',
	        success:function(data){
	        	//console.log(data.type);
	            if (data.type == 'doPost')
	            {
	            	$("#MsgDiv #"+ProgressKey+" span").html('在正准备上传');
	            	//console.log('在正准备上传'+ new Date());
	            } 
	            else
	        	if (data.type=='Temp')
	        	{
	            	$("#MsgDiv #"+ProgressKey+" span").html('正在上传：'+data.size+'/'+data.progress);
	        		//console.log('正在上传：'+data.size+'/'+data.progress);
	        	}
	        	else if (data.type=='Loacl')
	        	{
	            	$("#MsgDiv #"+ProgressKey+" span").html('正在保存：'+data.size+'/'+data.progress);
	        		//console.log('正在保存：'+data.size+'/'+data.progress);
	        	}
	        	else if (data.type=='success')
	        	{
	            	$("#MsgDiv #"+ProgressKey+" span").html('上传成功！');
	            	$("#MsgDiv #"+ProgressKey).remove();
	        		//console.log('上传成功！');
	        	}
				if (data.type!='success' && data.type!=null)
				{
	            	
					setTimeout(getProgress,1000);
				}

            	//$("#MsgDiv #"+ProgressKey+" span").html(data.type);
            	
	        }
		});	
	}
	getProgress();
	
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
				$("#NewDirName").val("");
			}
        }
	});	
}
function DownloadFile(id)
{
	//console.log("/FileServer/File/Download?id="+id);
	window.open("/FileDiskServer/File/Download?id="+id,"_blank");
//			window.open("/FileDiskServer/File/PreviewFile?id="+id,"_blank");
}

function OpenFile(id)
{
	//console.log("/FileServer/File/Download?id="+id);
	//window.open("/FileDiskServer/File/Download?id="+id,"_blank");
	window.open("/FileDiskServer/File/PreviewFile?id="+id,"_blank");
}

function CloseNewDir()
{
	//$("#NewDirDiv").fadeOut(100);
	$('#NewDirDiv').modal('hide')
	

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
		OpenFile(id);
	});
	
	$("#FileTable").on("click",".DownloadBtn",function(){
		var id=$(this).attr("DivID");
		DownloadFile(id);
	});
	/*$("#FileTable").on("click",".UploadBtn",function(){
		var id=$(this).attr("DivID");
		DownloadFile(id);
	});
	*/
	$("#FilePathList").on("click","span",function(){
		GotoDir(this);
	});
	$("#GotoFrontDirBtn").on("click",function(){				
		GotoFrontDir(this);
	});   
});	

//导航条点击后，自动收起
$(function(){	
	var navbar=$("#navbar");
	
	
	/*
	$("#navbar").on("click",".navbar-nav>li",function(){
		console.log(navbar);
		navbar.collapse("toggle");
		//$(this).parent().parent().collapse("toggle");
	});
	*/
	$("body").on("click",function(){
		//console.log(navbar);
		if ($("#navbar").hasClass("in"))
		{
			navbar.collapse("toggle");
		}
		//$(this).parent().parent().collapse("toggle");
	});
});	

$(function () {
	  $('[data-toggle="tooltip"]').tooltip()
})
	
    