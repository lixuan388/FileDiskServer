<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">    
	
	<meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
	<script type="text/javascript" src="/js/jquery/jquery.form.js"></script>
	
	
    <!-- Bootstrap core CSS -->
    <link href="<%=request.getContextPath() %>/res/css/bootstrap.css" rel="stylesheet">
    
    <style type="text/css">
		body{
			padding-top: 30px;
		}
		.modal-body>.input-group
		{
			padding: 10px;
			width: 100%;
		}.modal-body>.input-group>span
		{

			width: 100px;
			text-align: right;
		}
		th,td
		{
			text-align: center;
		}
		    
input[type=checkbox]:checked +label>.glyphicon-remove{  
    display: none;  
}  
    
input[type=checkbox]:not(:checked) +label>.glyphicon-ok{  
    display: none;  
}  

label>.glyphicon
{
border: 1px solid #ccc;
color: #337ab7;
}
input[type="checkbox"]
{
display: none;
}

div.text-center
{
	display: table-cell;
}
    </style>
	<title>账号管理</title>
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
			<li ><a href="javascript:void(0);" ><div ><span style="width: 84px;">【账号管理】</span></div></a></li>
			
       </ul>
     </div><!--/.nav-collapse -->
    </li>
  </ul>
  </div>			    
</nav>
    
   
<div id="BodyDiv" style="padding: 10px;margin: 20px;" >




<table id="UserList" class="table table-hover">
      <thead>
        <tr>
          <th>ID</th>
          <th>姓名</th>
          <th>账号</th>
          <th>密码</th>
          <th>停用</th>
          <th>管理员</th>      
          <th></th>          
        </tr>
      </thead>
      <tfoot>
	    <tr>
	      <td colspan="6"></td>
	      <td ><a href="javascript:void(0);" onclick="NewUser();">新增</a></td>
	    </tr>
	  </tfoot>
  
      <tbody>
      </tbody>
    </table>
</div>



<div class="modal fade" id="EditUserInfoDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">账号信息</h4>
      </div>
      <div class="modal-body">
        <div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">ID：</span>
		  <input type="text" id="UserID" class="form-control " placeholder="" aria-describedby="basic-addon1"  readonly="readonly">
		</div>
        <div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">姓名：</span>
		  <input type="text" id="UserName" class="form-control" placeholder="请输入姓名" aria-describedby="basic-addon1">
		</div>
        <div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">账号：</span>
		  <input type="text" id="UserCode" class="form-control" placeholder="请输入账号" aria-describedby="basic-addon1">
		</div>
		
		<div style="position: relative;display: table;border-collapse: separate;width: 100%;">
	        <div class="text-center">	        
			  <input type="checkbox" id="UserDeleteFlg" >
			  <label for="UserDeleteFlg"><span class="" id="basic-addon1">停用：</span><span class="glyphicon glyphicon-remove"></span><span class="glyphicon glyphicon-ok"></span>
			  
			  </label>
			</div>
	        <div class="text-center">
			  <input type="checkbox" id="UserIsAdmin" >
			  <label for="UserIsAdmin"><span class="" id="basic-addon1">管理员：</span><span class="glyphicon glyphicon-remove"></span><span class="glyphicon glyphicon-ok"></span>
			  </label>
			</div>
		</div>
      </div>
	
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary"  onclick="SaveUserInfo();">确认</button>
      </div>
      <div id="ErrMsg" class="modal-footer" style="display: none;">
      	<div class="alert alert-danger fade in" >
		    <strong></strong>
		</div>
      </div>
    </div>
  </div>
</div>


<div class="modal fade" id="InitPassDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">重置密码</h4>
      </div>
      <div class="modal-body">
		<span>是否确认重置此账号的密码为"888888"</span>
      </div>	
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
        <button type="button" class="btn btn-primary"  onclick="SaveInitPassWord();">确认</button>
      </div>
      <div id="ErrMsg" class="modal-footer" style="display: none;">
      	<div class="alert alert-danger fade in" >
		    <strong></strong>
		</div>
      </div>
    </div>
  </div>
</div>


<script type="text/javascript">
var userID
function EditUser(UserID)
{	
	$.ajax({
        url:"<%=request.getContextPath() %>/Content/System/GetUserList.json?id="+UserID,
        type:'get',
        dataType:'Json',
        success:function(data){
        	if (data.length>=1)
			{
        		$('#EditUserInfoDiv #UserID').val(data[0].ID);
        		$('#EditUserInfoDiv #UserName').val(data[0].Name);
        		$('#EditUserInfoDiv #UserCode').val(data[0].Code);	        		
        		
    			$('#EditUserInfoDiv #UserDeleteFlg').prop('checked',data[0].DeleteFlg==1); 
    			$('#EditUserInfoDiv #UserIsAdmin').prop('checked',data[0].IsAdmin==1);	        		

    			$('#EditUserInfoDiv #ErrMsg').hide();
        		$('#EditUserInfoDiv').modal('show');
			}
        }
	})		
}

function SaveInitPassWord()
{	
	$.ajax({
        url:"<%=request.getContextPath() %>/Content/System/InitPassWord.json?id="+userID,
        type:'get',
        dataType:'Json',
        success:function(data){
        	if (data.MsgID!=1)
			{       		
		  		$('#InitPassDiv #ErrMsg strong').html(data.MsgTest);
		  		$('#InitPassDiv #ErrMsg').show(500);	
			}
        	else
        	{
  				$('#InitPassDiv #ErrMsg').hide();
        		$('#InitPassDiv').modal('hide');
        		LoadUser(-1);
        	}
        }
	})		
}
function InitPassWord(UserID)
{
	userID=UserID;
	$('#InitPassDiv #ErrMsg').hide();
	$('#InitPassDiv').modal('show');
}

function NewUser()
{	
	$('#EditUserInfoDiv #UserID').val(-1);
	$('#EditUserInfoDiv #UserName').val("");
	$('#EditUserInfoDiv #UserCode').val("");	        		
	 		
	$('#EditUserInfoDiv #UserDeleteFlg').prop('checked',false); 
	$('#EditUserInfoDiv #UserIsAdmin').prop('checked',false);	        		
	
	$('#EditUserInfoDiv #ErrMsg').hide();
	$('#EditUserInfoDiv').modal('show');

}
	function SaveUserInfo()
	{

  		$('#EditUserInfoDiv #ErrMsg').hide();
		var ID=$('#EditUserInfoDiv #UserID').val();
		var UserName=$('#EditUserInfoDiv #UserName').val();
		var UserCode=$('#EditUserInfoDiv #UserCode').val();
		var UserDeleteFlg=$('#EditUserInfoDiv #UserDeleteFlg').prop('checked');
		var UserIsAdmin=$('#EditUserInfoDiv #UserIsAdmin').prop('checked');
		var json={"ID":ID,"UserName":UserName,"UserCode":UserCode,"UserDeleteFlg":UserDeleteFlg,"UserIsAdmin":UserIsAdmin}
		
		$.post("<%=request.getContextPath() %>/Content/System/SaveUserInfo",json,function(data){
		      //console.log(data);
		      if (data.MsgID!=1)
		      {
			  		$('#EditUserInfoDiv #ErrMsg strong').html(data.MsgTest);
			  		$('#EditUserInfoDiv #ErrMsg').show(500);		    	  
		      }
		      else
		   	  {

      				$('#EditUserInfoDiv #ErrMsg').hide();
	        		$('#EditUserInfoDiv').modal('hide');
	        		LoadUser(-1);
		   	  }		      
		},"json");
		
		//console.log(json);
			
	}
	
	function LoadUser(UserID)
	{
		$.ajax({
	        url:"<%=request.getContextPath() %>/Content/System/GetUserList.json?id="+UserID,
	        type:'get',
	        dataType:'Json',
	        success:function(data){
	        	$("#UserList>tbody").html("");
	        	for (i=0;i<data.length;i++)
				{
	        		var tr=$("<tr userid=\""+data[i].ID+"\"></tr>");
	        		tr.append("<td>"+data[i].ID+"</td>");
	        		tr.append("<td>"+data[i].Name+"</td>");
	        		tr.append("<td>"+data[i].Code+"</td>");
	        		tr.append("<td><a href=\"javascript:void(0);\" onclick=\"InitPassWord("+data[i].ID+");\">"+data[i].PassWord+"【重置】</a></td>");
	        		if (data[i].DeleteFlg==1)
	        		{
	        			tr.append("<td><span class=\"glyphicon glyphicon-ok\"></span></td>");	
	        		}
	        		else
	        		{
	        			tr.append("<td><span class=\"glyphicon glyphicon-remove\"></span></td>");	
	        		}
	        		if (data[i].IsAdmin==1)
	        		{
	        			tr.append("<td><span class=\"glyphicon glyphicon-ok\"></span></td>");	
	        		}
	        		else
	        		{
	        			tr.append("<td><span class=\"glyphicon glyphicon-remove\"></span></td>");	
	        		}
	        		
	        		tr.append("<td><a href=\"javascript:void(0);\" onclick=\"EditUser("+data[i].ID+");\">修改</a></td>");                
	                
	        		$("#UserList>tbody").append(tr);
				}
	        }
		})
	}

	$(function () {
		LoadUser(-1);
		/*
		$("input[type='checkbox']").on("change",function(){
			console.log($(this).after());
			
			$(this).after().addClass("glyphicon-ok");
		});*/
	})

</script>


    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</body>
</html>
