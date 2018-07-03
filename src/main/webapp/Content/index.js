//<%@page pageEncoding="UTF-8"%>
layui.extend({
  index: 'lib/index' // 主入口模块
}).define(['index','form','element','laydate','table','upload'], function () {
  var $ = layui.$,
  form = layui.form,
  admin = layui.admin,
  setter = layui.setter,
  table = layui.table,
  element= layui.element,
  upload = layui.upload,
  laydate = layui.laydate;
  
  $body = $('body');
  
  table.render({
    elem: '#DataGrid',
    url: setter.ContextPath+ '/File/GetFileList',
    where: {
      id:0
    },  
    done: function(res, curr, count){
      // 如果是异步请求数据方式，res即为你接口返回的信息。
      // 如果是直接赋值的方式，res即为：{data: [], count: 99} data为当前页数据、count为数据总长度
      // console.log(res);
      // 得到当前页码
      // console.log(curr);
      
      // 得到数据总量
      // console.log(count);
      var filepath=res.obj.FilePath.split('/');
      // console.log(filepath);
      $('.filepath').html('')
      filepath[0]='Home';
      if (filepath.length==1){
        $('.backDir').hide();
      }
      else{
        $('.backDir').show();
      }
      
      $('[layadmin-event=backDir]').data('pid',res.obj.ParentID);
        
      for (i in filepath)
      {
        if (i==filepath.length-1){
          $('.filepath').append('<a ><cite>'+filepath[i]+'</cite></a>')
        }
        else {
          $('.filepath').append('<a >'+filepath[i]+'</a>')
        }
      }
      $('.filepath').data('id',res.obj.SID);
      element.render();
      uploadRender();

    },
    cols: [
      [
// {
// type:'checkbox',
// width: 30,
// align: 'center'
// },
      {
        field: 'Name',
        sortable: false,
        align: 'center',
        title: '文件名',
        templet: '#FileName',
      },
      {
        field: 'Size',
        width: 100,
        sortable: false,
        align: 'center',
        title: '大小'
      },
      {
        field: 'LastDate',
        width:200,
        sortable: false,
        align: 'center',
        title: '修改日期'
      }
      ]
    ],
    page: false,
    response: {
      statusCode: 1      // 重新规定成功的状态码为 200，table 组件默认为 0
    },
    parseData: function (res) { // 将原始数据解析成 table 组件所规定的数据
      return {
        'code': res.MsgID, // 解析接口状态
        'msg': res.MsgText, // 解析提示文本
        'count': res.Data==undefined?0:res.Data.length, // 解析数据长度
        'data': res.Data        // 解析数据列表
        ,'obj':res
      };
    },
    height: 'full-140'
    // ,size: 'sm'
  });
  
  var events =  {
      NewDir:function(othis){
        layer.prompt({
          formType: 0,
          value: '新建文件夹',
          title: '新建文件夹',
        }, function(value, index, elem){
          console.log(value); // 得到value
          admin.req({
            url: setter.ContextPath+'/CreateDir?ID=' + 0 + '&Name=' + encodeURI(encodeURI(value))
            ,type: 'get'
            ,success: function(res){
              if (res.MsgID==1){
                layer.alert('<div>文件夹新建成功！</div>',function(index){
                  // do something
                  table.reload('DataGrid',{})
                  layer.closeAll();
                });  
              }
              else{
                layer.alert('<div>'+res.MsgText+'</div>',function(index){
                  // do something
                  layer.close(index);
                });
              }
            }
            ,error:function(){
              
            }
          });
        });
      },
      OpenDir:function(othis){
        // console.log(othis);
        var id=$(othis).data('id');
        var exe=$(othis).data('exe');
// console.log('id:'+id+'/exe:'+exe);
        if (exe=='dir'){
          table.reload('DataGrid',{
              where: {id:id}
          })
        }
        else {
          events.DownloadFile(othis);
        }
      },
      backDir:function(othis){
        // console.log(othis);
        var id=$(othis).data('pid');
        table.reload('DataGrid',{
            where: {id:id}
        })
      },
      DownloadFile:function(othis){
        var id=$(othis).data('id');
        window.open(setter.ContextPath+'/File/Download?id=' + id, '_blank');
      },
      uploadFile:function(othis){
        layer.open({
          type: 1,
          area: ['1000px', '500px'],
          content: $('.layui-upload') // 这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
          ,end:function(){
            table.reload('DataGrid',{
            });
            $("#demoList").html('');
          } 
        });
      },
      DeleteFile:function(othis)
      {
        var id=$(othis).data('id');
        var exe=$(othis).data('exe');
        var url='';
        if (exe=='dir'){
          url=setter.ContextPath+'/File/DeleteDir?ID='+id;
        }
        else{
          url=setter.ContextPath+'/File/DeleteFile?ID='+id;
        }
        layer.confirm('<span style="color:red;">是否确认删除操作？</span>', {
            btn: ['删除','取消'] //按钮
          }, 
          function(index){
            admin.req({
              url: url
              ,type: 'get'
              ,success: function(res){
                if (res.MsgID==1){
                  layer.alert('<div>删除操作成功！</div>',function(index){
                    // do something
                    table.reload('DataGrid',{})
                    layer.close(index);
                  });  
                }
                else{
                  layer.alert('<div>'+res.MsgText+'</div>',function(index){
                    // do something
                    layer.close(index);
                  });
                }  
                layer.close(index);
              }
              ,error:function(){
                layer.alert('<div>操作失败！请重试</div>',function(index){
                  // do something
                  layer.close(index);
                });  
                layer.close(index);
              }
            });
          }, 
          function(index){
            layer.close(index);
          }
        );
      }
      
  }
  // 点击事件
  $body.on('click', '*[layadmin-event]', function(){
    var othis = $(this)    
    ,attrEvent = othis.attr('layadmin-event');
    events[attrEvent] && events[attrEvent].call(this, othis);
  });
  
// 多文件列表示例
  var demoListView = $('#demoList')
  ,uploadListIns = upload.render({
    elem: '#testList'
    ,url: setter.ContextPath+'/File/UploadToFileServer'
    ,accept: 'file'
    ,multiple: true
    ,auto: false
    ,bindAction: '#testListAction'
      ,field:'FileData'
    ,data: {
        id: function(that,index, file){
          config=that.config;
          config.headers={ProgressKey: index}
          return index;
        }
        ,FileName:function(that,index,file){
          var FileName = file.name;
          var index1 = FileName.lastIndexOf('.');
          var index2 = FileName.length;
          if (index1 == - 1)
          {
            return FileName;
          } 
          else
          {
            return FileName.substring(0, index1);
          }
        }
        ,FileExe:function(that,index,file){
          var FileName = file.name;
          var index1 = FileName.lastIndexOf('.');
          var index2 = FileName.length;
          // console.log('index1:' + index1);
          if (index1 == - 1)
          {
            return '';
          } 
          else
          {
            return FileName.substring(index1 + 1, index2);
          } 
        }
        ,FilePath:function(that,index,file){
          return $('.filepath').data('id');
        }
      }
  
    ,before: function(obj){ // obj参数包含的信息，跟 choose回调完全一致，可参见上文。
      console.log(obj); // 上传loading
    }
    ,choose: function(obj){   
      var files = this.files = obj.pushFile(); // 将每次选择的文件追加到文件队列
      // 读取本地文件
      var that=uploadListIns;
      
      obj.preview(function(index, file, result){
        var tr = $(['<tr id="upload-'+ index +'">'
          ,'<td><div>'+ file.name+'</div>'
          ,'<div class="layui-progress layui-progress-big" lay-showpercent="true" id="ProgressKeyValue'+index+'" lay-filter="ProgressKeyValue'+index+'">'
            ,'<div class="layui-progress-bar"  lay-percent="0%"></div>'
          ,'</div></td>'          
          ,'<td>'+ (file.size/1014).toFixed(1) +'kb</td>'
          ,'<td>等待上传</td>'
          ,'<td>'
            ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
            ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
          ,'</td>'
        ,'</tr>'].join(''));
        that.config.getProgress(index);
        
        
        
        // 单个重传
        tr.find('.demo-reload').on('click', function(){
          obj.upload(index, file);
        });
        
        // 删除
        tr.find('.demo-delete').on('click', function(){
          delete files[index]; // 删除对应的文件
          tr.remove();
          uploadListIns.config.elem.next()[0].value = ''; // 清空 input file 值，以免删除后出现同名文件不可选
        });
        
        demoListView.append(tr);
      });
      element.render();
    }
    ,getProgress:function (ProgressKey)
    {

      //var ProgressKey=config.ProgressKey;
      var url=setter.ContextPath+'/File/GetUploadProgress?id='+ProgressKey+ '&d=' + new Date().getTime();
      admin.req({
        url: url
        ,type: 'get'
        ,success: function(data){
          if (data.type == 'doPost')
          {

          }
          else
          if (data.type == 'Temp')
          {
            var n=data.progress*100 /data.size;
            element.progress('ProgressKeyValue'+ProgressKey, n.toFixed()+'%');
          //console.log('正在上传：'+data.size+'/'+data.progress);
          }
          else if (data.type == 'Loacl')
          {
            var n=data.progress*100 /data.size;
            element.progress('ProgressKeyValue'+ProgressKey, n.toFixed()+'%');
          //console.log('正在保存：'+data.size+'/'+data.progress);
          }
          else if (data.type == 'success')
          {
            var n=data.progress*100 /data.size;
            element.progress('ProgressKeyValue'+ProgressKey, n.toFixed()+'%');
          //console.log('上传成功！');
          }
          if (data.type != 'success' && data.type != null)
          {
            setTimeout(function(){uploadListIns.config.getProgress(ProgressKey)}, 1000);
            console.log('setTimeout');
          }
        }
        ,error:function(){
        }
      });
    }
    ,done: function(res, index, upload){
      if(res.MsgID == 1){ // 上传成功
        var tr = demoListView.find('tr#upload-'+ index)
        ,tds = tr.children();
        tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>');
        tds.eq(3).html(''); // 清空操作

        element.progress('ProgressKeyValue'+index, '100%');
        $('#ProgressKeyValue'+index+'>[lay-percent]').attr('lay-percent', '100%');
        return delete this.files[index]; // 删除文件队列已经上传成功的文件
      }
      this.error(index, upload);
    }
    ,error: function(index, upload){
      var tr = demoListView.find('tr#upload-'+ index)
      ,tds = tr.children();
      tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>');
      tds.eq(3).find('.demo-reload').removeClass('layui-hide'); // 显示重传
    }
  })
  
  function uploadRender (){
    // 普通图片上传
    layui.each($('.editFile'),function(){
      var id=$(this).data('id');
      var FileName=$(this).data('name');
      var FileExe=$(this).data('exe');
      var FilePath=$(this).data('dirid');
      var ProgressKey=id+'-'+new Date().getTime();
      
      var uploadInst = upload.render({
        elem: '.editFile[data-id='+id+']'
        ,url: setter.ContextPath+'/File/UploadToFileServer'
        ,accept: 'file'
        ,multiple: false
        ,auto: true
        ,field:'FileData'
         ,headers:{ProgressKey: ProgressKey}
        ,data: {
            id: function(that,index, file){
//              config=that.config;
//              config.headers={ProgressKey: ProgressKey}
              return id;
            }
            ,FileName:function(that,index,file){
                return FileName;
            }
            ,FileExe:function(that,index,file){

                return FileExe;
            }
            ,FilePath:function(that,index,file){
              return FilePath;
            }
        }
        ,choose: function(obj){
          uploadInst.config.progressIndex=layer.open({
            type: 1,
            title:'上传',
            area: ['500px', '150px'],
            content: $('.layui-uploadsingle') // 这里content是一个DOM，注意：最好该元素要存放在body最外层，否则可能被其它的相对元素所影响
            ,end:function(){
              table.reload('DataGrid',{
              });
            } 
          });
          var files = obj.pushFile();

          console.log(files);
          for (i in files )
          {
            console.log(files[i].name);
            $('#uploadFileName').html(files[i].name);
          }
          $('#ProgressKey').html('在正准备上传');
          element.progress('ProgressKeyValue', '0%')
          uploadInst.config.getProgress();

        }
        ,getProgress:function ()
        {

          var ProgressKey=uploadInst.config.headers['ProgressKey'];
          var url=setter.ContextPath+'/File/GetUploadProgress?id='+ProgressKey+ '&d=' + new Date().getTime();
          admin.req({
            url: url
            ,type: 'get'
            ,success: function(data){
              if (data.type == 'doPost')
              {
                $('#ProgressKey').html('在正准备上传');
              //console.log('在正准备上传'+ new Date());
              }
              else
              if (data.type == 'Temp')
              {
                $('#ProgressKey').html('正在上传：');
                var n=data.progress*100 /data.size;
                element.progress('ProgressKeyValue', n.toFixed()+'%');
              //console.log('正在上传：'+data.size+'/'+data.progress);
              }
              else if (data.type == 'Loacl')
              {
                $('#ProgressKey').html('正在保存：' );
                var n=data.progress*100 /data.size;
                element.progress('ProgressKeyValue', n.toFixed()+'%');
              //console.log('正在保存：'+data.size+'/'+data.progress);
              }
              else if (data.type == 'success')
              {
                $('#ProgressKey').html('上传成功！');
                var n=data.progress*100 /data.size;
                element.progress('ProgressKeyValue', n.toFixed()+'%');
              //console.log('上传成功！');
              }
              if (data.type != 'success' && data.type != null)
              {
                setTimeout(uploadInst.config.getProgress, 1000);
                console.log('setTimeout');
              }
            }
            ,error:function(){
              
            }
          });

        }
        
        ,done: function(res){
          // 如果上传失败
          if(res.MsgID!=1){
            return layer.msg('上传失败');
          }
          else{  
            //setTimeout(function(){layer.close(uploadInst.config.progressIndex)}, 3000);
          }
          // 上传成功
        }
        ,error: function(){
          // 演示失败状态，并实现重传
          layer.alert('文件上传错误！');        
        }
      });
    })
  }
  
})



//  
//  
//  
// ///////////////////////////////////////////////////////////////////////////////////////////////////
// var JsonData;
// var DirID = - 1;
// var ParentID = 0;
// var SID = 0;
// function GetFileList(id)
// {
// //console.log("ID:"+id);
// var $loadingToast = $('#loadingToast');
// $loadingToast.fadeIn(100);
// $('#FileTable').html('');
// $.ajax({
// url: '<%=request.getContextPath()%>/File/GetFileList?id=' + id,
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// JsonData = data;
// $('#GotoFrontDirBtn').attr('PID', data.ParentID);
// $('#GotoFrontDirBtn').attr('SID', data.SID);
// if ($('#Span' + data.SID).length == 0)
// {
// $('#Span' + data.ParentID).after('<li id=\'Span' + data.SID + '\' class="active" PID=\'' + data.ParentID + '\' SID=\'' + data.SID + '\'><span>' + data.DirName + '</span></li>');
// $('#Span' + data.ParentID).removeClass('active');
// }
// else
// {
// $('#Span' + data.SID).addClass('active').nextAll().remove();
// }
// DirID = data.SID
// if (data.Dir.length + data.File.length == 0)
// {
// $('#FileTable').html('<span class=\'NoFiles\'>无文件</span>');
// }
// else
// {
// //console.log(data.Dir.length);
// for (var i = 0; i < data.Dir.length; i++)
// {
// AddInfo(data.Dir[i].DirName, 'dir', data.Dir[i].DirID);
// //console.log(data.Dir[i].DirName);
// }
// for (var i = 0; i < data.File.length; i++)
// {
// AddInfo(data.File[i].FileName, data.File[i].FileExe, data.File[i].FileID);
// //console.log(data.File[i].FileName);
// }
// }
// $loadingToast.fadeOut(100);
// }
// });
// }
// function AddInfo(name, Exe, ID)
// {
// var IsFile = ''
// if (Exe != 'dir')
// {
// IsFile = 'IsFile';
// }
// var FileName
// if (Exe == 'dir')
// {
// FileName = name;
// }
// else
// {
// if (Exe == '')
// {
// FileName = name;
// }
// else
// {
// FileName = name + '.' + Exe;
// }
// }
// var div = $('#FileListTempate').clone();
// div.attr('ID', 'FileListDivID_' + ID)
// div.addClass(Exe);
// div.css('display', 'block');
// div.attr('title', FileName);
// div.find('.DefIcon').addClass(Exe + '-large').addClass(IsFile).attr('title', FileName).attr('DivID', ID);
// div.find('.DownloadBtn').attr('DivID', ID);
// div.find('.UploadBtn').attr('DivID', ID);
// div.find('.DeleteBtn').attr('DivID', ID).attr('IsFile', IsFile);
// div.find('.uitPe56').attr('title', FileName).html(FileName);
// div.find('.download').attr('title', FileName);
// div.find('#uploaderInput').attr('FileName', FileName);
// /*
// var div='<div class="cEefyz mmdqJnq open-enable '+Exe+'" style="display: block;" _position="0" _installed="1" title="'+FileName+'">'+
//  	
// '<div class="aekEOaP DefIcon '+Exe+'-large '+IsFile+'" title="'+FileName+'" DivID="'+ID+'">'+
// '<img class="alaQ1kq" style="visibility: hidden;">'+
// '</div>'+
// '<div class="file-name">'+
// '<a node-type="cbqK6b" class="uitPe56" href="javascript:void(0);" title="'+FileName+'">'+FileName+'</a>'+
// '</div>'+
// '<span node-type="EOGexf" class="EOGexf">'+
// '<span class="icon lgwELbP"></span>'+
// '<span class="icon checkgridsmall"></span>'+
// '</span>'+'<a node-type="cbqK6b" class="download" href="javascript:void(0);" style="z-index: 99999;position: relative;" title="'+FileName+'">下载</a>'+
// '</div>';
// */
// $('#FileTable').append(div);
// }
// function GotoFrontDir(d)
// {
// var id = $(d).attr('PID');
// /*var sid=$(d).attr("SID");
// if (id>0)
// {
// //$("#FilePathList").find("#Span"+id).nextAll().remove();
// //$("#FilePathList").find("#Span"+sid).addClass("active");
// DirID=id;
// //console.log("#Span"+id);
// }
// else
// {
// //$("#FilePathList").find("#Span0").remove();
// id=sid
// }
// */
// //$("#FilePathList").find("#Span"+sid).remove();
// GetFileList(id);
// }
// function GotoDir(d)
// {
// var sid = $(d).attr('SID');
// //$("#FilePathList").find("#Span"+sid).nextAll().remove();
// //$("#FilePathList").find("#Span"+sid).addClass("active");
// DirID = sid;
// //console.log("#Span"+sid);
// GetFileList(sid);
// }
// function AddFileMultiple(t)
// {
// for (i = 0; i < $('#uploaderInput2018') [0].files.length; i++)
// {
// console.log($('#uploaderInput2018') [0].files[i].name);
// var formData = new FormData();
// formData.append('FileData', $('#uploaderInput2018') [0].files[i]);
// formData.append('FileName', $('#uploaderInput2018') [0].files[i].name);
// formData.append('FileExe', 'txt');
// formData.append('FilePath', 0);
// $.ajax({
// url: '/FDS/File/UploadToFileServer',
// type: 'POST',
// cache: false,
// data: formData,
// processData: false,
// contentType: false
// }).done(function (json) {
// console.log(json);
// });
// }
// }
// function AddFile(t)
// {
// //var $loadingToast = $('#loadingToast');
// //$loadingToast.fadeIn(100);
// //console.log('AddFile');
// //console.log(t);
// //console.log($("#uploaderInput").val());
// var FileName = $(t).attr('FileName');
// var upFileName
// if (FileName != '')
// {
// upFileName = FileName;
// }
// else
// {
// upFileName = $(t) [0].files[0].name;
// } //console.log($(t)[0]);
//  
// console.log(upFileName);
// var index1 = upFileName.lastIndexOf('.');
// var index2 = upFileName.length;
// console.log('index1:' + index1);
// if (index1 == - 1)
// {
// $(t).parent().find('#FileName').val(upFileName);
// $(t).parent().find('#FileExe').val('');
// }
// else
// {
// $(t).parent().find('#FileName').val(upFileName.substring(0, index1));
// //$("#FileName").val(encodeURI(encodeURI(upFileName.substring(0,index1))));
// $(t).parent().find('#FileExe').val(upFileName.substring(index1 + 1, index2));
// }
// $(t).parent().find('#FilePath').val(DirID);
// $.ajax({
// url: '/FDS/File/GetProgressKey?d=' + new Date(),
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// //console.log("ProgressKey:"+data.ProgressKey);
// $('#MsgDiv').append('<div class=\'Headdiv\' id=\'' + data.ProgressKey + '\'>' + upFileName + '/<span></span></div>');
// UploadImage($(t).parent(), data.ProgressKey);
// }
// });
// }
// function UploadImage(from, ProgressKey)
// {
// var form = $(from);
// form.ajaxSubmit({
// contentType: 'multipart/form-data',
// dataType: 'Json',
// success: function (msg) {
// //$("#msg2").html(msg.FileName);
// //var $loadingToast = $('#loadingToast');
// //$loadingToast.fadeOut(100);
// GetFileList(DirID);
// }
// });
// function getProgress()
// {
// //console.log('getProgress');
// $.ajax({
// url: '/FDS/File/GetUploadProgress?id=' + ProgressKey + '&d=' + new Date(),
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// //console.log(data.type);
// if (data.type == 'doPost')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('在正准备上传');
// //console.log('在正准备上传'+ new Date());
// }
// else
// if (data.type == 'Temp')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('正在上传：' + data.size + '/' + data.progress);
// //console.log('正在上传：'+data.size+'/'+data.progress);
// }
// else if (data.type == 'Loacl')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('正在保存：' + data.size + '/' + data.progress);
// //console.log('正在保存：'+data.size+'/'+data.progress);
// }
// else if (data.type == 'success')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('上传成功！');
// $('#MsgDiv #' + ProgressKey).remove();
// //console.log('上传成功！');
// }
// if (data.type != 'success' && data.type != null)
// {
// setTimeout(getProgress, 1000);
// } //$("#MsgDiv #"+ProgressKey+" span").html(data.type);
//  
// }
// });
// }
// getProgress();
// }
// function NewDir()
// {
// $('#NewDirName').val('');
// $('#NewDirDiv').modal('show');
// }
// function SaveNewDir()
// {
// var NewDirName = $('#NewDirName').val();
// if (NewDirName == '')
// {
// alert('请输入【文件夹名称】！');
// return;
// }
// $.ajax({
// url: '/FDS/CreateDir?ID=' + DirID + '&Name=' + encodeURI(encodeURI(NewDirName)),
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// if (data.MsgID != 1)
// {
// alert(data.MsgTest);
// }
// else
// {
// CloseNewDir();
// GetFileList(DirID);
// $('#NewDirName').val('');
// }
// }
// });
// }
// function DeleteConfirm()
// {
// var id = $('#DeleteConfirm').attr('DivID');
// var IsFile = $('#DeleteConfirm').attr('IsFile');
// var urlStr = '';
// if (IsFile == '')
// {
// urlStr = '/FDS/File/DeleteDir?ID=' + id;
// }
// else
// {
// urlStr = '/FDS/File/DeleteFile?ID=' + id;
// }
// $.ajax({
// url: urlStr,
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// if (data.MsgID != 1)
// {
// alert(data.MsgTest);
// }
// else
// {
// var sid = $('#GotoFrontDirBtn').attr('SID');
// GetFileList(sid);
// // console.log("GetFileList");
// alert(data.MsgTest);
// }
// }
// });
// $('#DeleteConfirm').modal('hide');
// }
// function DownloadFile(id)
// {
// //console.log("/FileServer/File/Download?id="+id);
// window.open('/FDS/File/Download?id=' + id, '_blank');
// // window.open("/FDS/File/PreviewFile?id="+id,"_blank");
// }
// function OpenFile(id)
// {
// //console.log("/FileServer/File/Download?id="+id);
// //window.open("/FDS/File/Download?id="+id,"_blank");
// window.open('/FDS/File/PreviewFile?id=' + id, '_blank');
// }
// function DeleteFile(id, IsFile)
// {
// // $("#DeleteConfirm").fadeIn(100);
// $('#DeleteConfirm').attr('DivID', id);
// $('#DeleteConfirm').attr('IsFile', IsFile);
// $('#DeleteConfirm').modal('show');
// }
// function CloseNewDir()
// {
// $('#NewDirDiv').modal('hide');
// }
// function ChangePassWord()
// {
// $('#ChangePassWordDiv #PassWord1').val('');
// $('#ChangePassWordDiv #PassWord2').val('');
// $('#ChangePassWordDiv #PassWord3').val('');
// $('#ChangePassWordDiv').modal('show');
// }
// function UpLoadFileList(upload)
// {
// $('#FileListMsg').html('');
// // console.log($(upload));
// // console.log(upload.files.length);
// $('#FileListMsg').append('<div>length:' + upload.files.length + '</div>');
// var uploadName = !$(upload).attr('FileName') ? '' : $(upload).attr('FileName');
// for (i = 0; i < upload.files.length; i++)
// {
// var file = upload.files[i];
// UpLoadFileToServer(file, uploadName);
// }
// }
// function UpLoadFileToServer(file, uploadName)
// {
// // console.log("uploadName:"+uploadName);
// // return;
// $.ajax({
// url: '/FDS/File/GetProgressKey?d=' + new Date(),
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// //console.log("ProgressKey:"+data.ProgressKey);
// var ProgressKey = data.ProgressKey;
// $('#MsgDiv').append('<div class=\'Headdiv\' id=\'' + ProgressKey + '\'>' + file.name + '/<span></span></div>');
// var formData = new FormData();
// var FileName = uploadName == '' ? file.name : uploadName;
// var index1 = FileName.lastIndexOf('.');
// var index2 = FileName.length;
// console.log('index1:' + index1);
// if (index1 == - 1)
// {
// formData.append('FileName', FileName);
// formData.append('FileExe', '');
// }
// else
// {
// formData.append('FileName', FileName.substring(0, index1));
// formData.append('FileExe', FileName.substring(index1 + 1, index2));
// }
// formData.append('FileData', file);
// formData.append('ProgressKey', ProgressKey);
// formData.append('FilePath', DirID);
// var filename = file.name;
// $.ajax({
// url: '/FDS/File/UploadToFileServer?ProgressKey=' + ProgressKey,
// type: 'POST',
// cache: false,
// data: formData,
// processData: false,
// contentType: false,
// success: function (data) {
// if ($('#MsgDiv').html() == '')
// {
// GetFileList(DirID);
// }
// }
// });
// function getProgress(ProgressKey)
// {
// //console.log('getProgress');
// $.ajax({
// url: '/FDS/File/GetUploadProgress?id=' + data.ProgressKey + '&d=' + new Date(),
// type: 'get',
// dataType: 'Json',
// success: function (data) {
// //console.log(data.type);
// var ProgressKey = data.ProgressKey;
// if (data.type == 'doPost')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('在正准备上传');
// //console.log('在正准备上传'+ new Date());
// }
// else
// if (data.type == 'Temp')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('正在上传：' + data.size + '/' + data.progress);
// //console.log('正在上传：'+data.size+'/'+data.progress);
// }
// else if (data.type == 'Loacl')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('正在保存：' + data.size + '/' + data.progress);
// //console.log('正在保存：'+data.size+'/'+data.progress);
// }
// else if (data.type == 'success')
// {
// $('#MsgDiv #' + ProgressKey + ' span').html('上传成功！');
// $('#MsgDiv #' + ProgressKey).remove();
// if ($('#MsgDiv').html() == '')
// {
// GetFileList(DirID);
// } //console.log('上传成功！');
//  
// }
// if (data.type != 'success' && data.type != null)
// {
// setTimeout(getProgress(ProgressKey), 1000);
// }
// }
// });
// }
// getProgress(ProgressKey);
// }
// });
// }
// $(function () {
// GetFileList(0);
// $('#FileTable').on('mouseover', '.mmdqJnq', function () {
// $(this).addClass('dscQV9P');
// });
// $('#FileTable').on('mouseout', '.mmdqJnq', function () {
// $(this).removeClass('dscQV9P');
// });
// $('#FileTable').on('click', '.dir-large', function () {
// var id = $(this).attr('DivID');
// if (id < 0)
// {
// id = 0
// }
// GetFileList(id);
// });
// // $("#FileTable>div.dir").on("click",function(){
// // var id=$(this).attr("DivID");
// // if (id<0)
// // {
// // id=0
// // }
// // GetFileList(id);
// // });
// $('#FileTable').on('click', '.IsFile', function () {
// var id = $(this).attr('DivID');
// OpenFile(id);
// });
// // $("#FileTable>div:not(.dir)").on("click",function(){
// // var id=$(this).attr("DivID");
// // OpenFile(id);
// // });
// $('#FileTable').on('click', '.DownloadBtn', function () {
// var id = $(this).attr('DivID');
// DownloadFile(id);
// });
// $('#FileTable').on('click', '.DeleteBtn', function () {
// var id = $(this).attr('DivID');
// var IsFile = $(this).attr('IsFile');
// DeleteFile(id, IsFile);
// });
// /*$("#FileTable").on("click",".UploadBtn",function(){
// var id=$(this).attr("DivID");
// DownloadFile(id);
// });
// */
// $('#FilePathList').on('click', 'li:not(.active)', function () {
// GotoDir(this);
// });
// $('#GotoFrontDirBtn').on('click', function () {
// GotoFrontDir(this);
// });
// });
// //导航条点击后，自动收起
// $(function () {
// var navbar = $('#navbar');
// /*
// $("#navbar").on("click",".navbar-nav>li",function(){
// console.log(navbar);
// navbar.collapse("toggle");
// //$(this).parent().parent().collapse("toggle");
// });
// */
// $('body').on('click', function () {
// //console.log(navbar);
// if ($('#navbar').hasClass('in'))
// {
// navbar.collapse('toggle');
// } //$(this).parent().parent().collapse("toggle");
//  
// });
// });
// $(function () {
// $('[data-toggle="tooltip"]').tooltip()
// })
