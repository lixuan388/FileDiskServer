/**

 @Name：layuiAdmin 公共业务
 @Author：贤心
 @Site：http://www.layui.com/admin/
 @License：LPPL
    
 */
 
layui.define(function(exports){
  var $ = layui.$
  ,layer = layui.layer
  ,laytpl = layui.laytpl
  ,setter = layui.setter
  ,view = layui.view
  ,admin = layui.admin
  
  //公共业务的逻辑处理可以写在此处，切换任何页面都会执行
  //……
  
  
  
  //退出
  admin.events.logout = function(){
    //执行退出接口
    admin.req({
      url: layui.setter.base + 'json/user/logout.js'
      ,type: 'get'
      ,data: {}
      ,done: function(res){ //这里要说明一下：done 是只有 response 的 code 正常才会执行。而 succese 则是只要 http 为 200 就会执行
        
        //清空本地记录的 token，并跳转到登入页
        admin.exit(function(){
          location.href = 'user/login.html';
        });
      }
    });
  };
  console.log("common.js");
  var router = layui.router()
  ,path = router.path
  ,pathURL =router.path.join('/');

  console.log("pathURL:"+pathURL);
  console.log("location.href:"+location.href);
  //独立页面
  if(pathURL === '/user/login'){ //此处单独判断登入页，是为了兼容旧版（即未在 config.js 配置 indPage 的情况）
    container.render(router.path.join('/')).done(function(){
      admin.pageType = 'alone';
    });
  } else { //后台框架页面
  	/*
	  if(setter.interceptor){
	
	    console.log("interceptor=true");
	    var local = layui.data(setter.tableName);
	    console.log(local);
	    if(!local[setter.request.tokenName]){
	      return location.href = '/LayuiAdmin/views/user/login.html?redirect='+ encodeURIComponent(pathURL); //跳转到登入页
	    }
	  }
	  */
  }
  
  //对外暴露的接口
  exports('common', {});
});