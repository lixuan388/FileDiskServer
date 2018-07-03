package com.ecity.java.web.fileServer.FileUpload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.ecity.java.web.fileServer.fun.FileDigestUtil;
import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.ecity.java.web.fileServer.fun.GetGUIDString;
import com.java.sql.SQLConnect;

import net.sf.json.JSONObject;

@WebServlet("/File/UploadToFileServer")

public class FileUpload extends HttpServlet {

  private static final long serialVersionUID = 3655349618159330684L;
//    private File uploadPath;  
//    private File tempPath;

  private static final int MaxStoreSize = 4 * 1024;
  private static final int MaxFileUploadExceptionSize = 1024 * 1024 * 1024;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doPost(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub

    DoFileUpload dofileupload = new DoFileUpload();

    resp.setHeader("Cache-Control", "no-cache");

    resp.setContentType("application/json;charset=utf-8");

    // 临时目录
    File tempPath = dofileupload.GetTempPath(request, resp);

    // 从item_upload.jsp中拿取数据，因为上传页的编码格式跟一般的不同，使用的是enctype="multipart/form-data"
    // form提交采用multipart/form-data,无法采用req.getParameter()取得数据
    // String itemNo = req.getParameter("itemNo");
    // System.out.println("itemNo======" + itemNo);

    /******************************** 使用 FileUpload 组件解析表单 ********************/

    System.out.println("DiskFileItemFactory");
    // DiskFileItemFactory：创建 FileItem 对象的工厂，在这个工厂类中可以配置内存缓冲区大小和存放临时文件的目录。
    DiskFileItemFactory factory = new DiskFileItemFactory();
    // maximum size that will be stored in memory
    factory.setSizeThreshold(MaxStoreSize);
    // the location for saving data that is larger than getSizeThreshold()
    factory.setRepository(tempPath);

    // ServletFileUpload：负责处理上传的文件数据，并将每部分的数据封装成一到 FileItem 对象中。
    // 在接收上传文件数据时，会将内容保存到内存缓存区中，如果文件内容超过了 DiskFileItemFactory 指定的缓冲区的大小，
    // 那么文件将被保存到磁盘上，存储为 DiskFileItemFactory 指定目录中的临时文件。
    // 等文件数据都接收完毕后，ServletUpload再从文件中将数据写入到上传文件目录下的文件中

    ServletFileUpload upload = new ServletFileUpload(factory);
    // maximum size before a FileUploadException will be thrown
    upload.setSizeMax(MaxFileUploadExceptionSize);

    String ProgressKey = request.getHeader("ProgressKey") == null ? "" : request.getHeader("ProgressKey");
    // String ProgressKey = request.getParameter("ProgressKey");
//        String ProgressKey="ProgressKey"; 

    System.out.println("ProgressKey:" + ProgressKey);

    final String SessionID = request.getSession().getId() + "_" + ProgressKey;

    upload.setProgressListener(dofileupload.doProgressListener(request, resp, SessionID));

    ProgressSingleton.put(SessionID + "ProgressKey", ProgressKey);
    ProgressSingleton.put(SessionID + "Type", "doPost");
    ProgressSingleton.put(SessionID + "ContentLength", 0);
    ProgressSingleton.put(SessionID + "BytesRead", 0);
    System.out.println("doPost");
    /******************************* 解析表单传递过来的数据，返回List集合数据-类型:FileItem ***********/
    try {

      System.out.println("begin request");
      List fileItems = upload.parseRequest(request);
      System.out.println("end request");

      FileItem itemFileName = null;
      FileItem itemFileExe = null;
      FileItem itemFilePath = null;
      FileItem itemFileData = null;

      for (Iterator iter = fileItems.iterator(); iter.hasNext();) {
        // 获得序列中的下一个元素
        FileItem item = (FileItem) iter.next();
        // 判断是文件还是文本信息
        // 是普通的表单输入域
        if (item.isFormField()) {
          if ("FileName".equals(item.getFieldName())) {
            itemFileName = item;
          } else if ("FileExe".equals(item.getFieldName())) {
            itemFileExe = item;
          } else if ("FilePath".equals(item.getFieldName())) {
            itemFilePath = item;
          }
        } else if (!item.isFormField()) {
          itemFileData = item;
        }
      }
      if (itemFileData != null) {
        String FileName = new String(itemFileName.getString().getBytes("iso-8859-1"), "utf-8");
//                System.out.println(FileName);
        String FileExe = itemFileExe.getString().toUpperCase();
        String FilePath = "0";
        if (itemFilePath != null) {
          FilePath = itemFilePath.getString();
        }
        dofileupload.doFileUpload(request, resp, itemFileData, SessionID, FileName, FileExe, FilePath);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      upload = null;
    }
    dofileupload.doUploadOK(request, resp);
  }

  public class DoFileUpload {

    private String LocalFileName;
    private String LocalPathName;
    private String FileKey;

    public String GetWebAppsPath(HttpServletRequest request, HttpServletResponse resp) {
//			System.out.println("getServletContext:"+System.getProperty("catalina.home"));
//			return getServletContext().getRealPath("/").replace(request.getContextPath().replace("/", "")+"\\", "");
      return System.getProperty("catalina.home") + "\\webapps\\";
    }

    public File GetTempPath(HttpServletRequest request, HttpServletResponse resp) {

      String WebAppsPath = GetWebAppsPath(request, resp);
//	        System.out.println("WebAppsPath:"+WebAppsPath);
      // 临时目录
      // File tempFile = new File(item.getName())构造临时对象
      File tempPath = new File(WebAppsPath + "UploadTempFile");
      if (!tempPath.exists()) {
        tempPath.mkdir();
      }
//	        System.out.println("tempPath:"+tempPath.getAbsolutePath());
      return tempPath;
    }

    public void doFormField(HttpServletRequest request, HttpServletResponse resp, FileItem item) {
      // System.out.println("getFieldName:" +
      // item.getFieldName()+":"+item.getString());
    }

    public String doFileUpload(HttpServletRequest request, HttpServletResponse resp, FileItem item, String Key,
        String FileName, String FileExe, String DirID) throws Exception {
      // 上传文件的名称和完整路径

      long size = item.getSize();

      String fileName = FileName;
//	    	System.out.println("FileExe:("+FileExe+")");
      if (!FileExe.equals("")) {
        fileName = fileName + "." + FileExe;
      }

      String FileServerPath = (String) getServletContext().getAttribute("FileServerPath");
      String LocalPath = GetFilePathLocal(DirID);
      ExistDir.isExistDir(FileServerPath + LocalPath);
      File uploadPath = new File(FileServerPath + LocalPath);

      LocalPathName = LocalPath + "/";
      LocalFileName = fileName;

      InputStream in = item.getInputStream();

      File file = new File(uploadPath, fileName);
      System.out.println(file.getAbsolutePath());
      FileOutputStream out = new FileOutputStream(file);

      System.out.println("Loacl");
      try {
        byte[] buffer = new byte[1024];
        int readNumber = 0;
        int Progress = 0;
        while ((readNumber = in.read(buffer)) != -1) {
          // 每读取一次，更新一次进度大小
          Progress = Progress + readNumber;
          ProgressSingleton.put(Key + "Type", "Loacl");
          ProgressSingleton.put(Key + "ContentLength", size);
          ProgressSingleton.put(Key + "BytesRead", Progress);
          out.write(buffer, 0, readNumber);
        }
      } finally {
        out.close();
      }

      String FileSha1 = "";
      String OldFileSha1 = "";
      FileSha1 = FileDigestUtil.getSha1(file);

      ProgressSingleton.put(Key + "Type", "success");
      String UserName = WebFunction.GetUserName(request);

      DBTable table = new DBTable(SQLConnect.GetConnect(), "select * from afl_FileList where afl_DirID=" + DirID
          + " and afl_fileName='" + FileName + "' and afl_FileExe='" + FileExe + "'");
      try {
        table.Open();
        if (table.next()) {
          OldFileSha1 = table.getString("afl_Sha");
          FileKey = table.getString("afl_key");
          table.UpdateValue("afl_Sha", FileSha1);
          table.UpdateValue("afl_status", "E");
          table.UpdateValue("afl_date_lst", new Date());
          table.UpdateValue("afl_user_lst", UserName);
          table.UpdateValue("afl_size", file.length());
          table.PostRow();
        } else {
          FileKey = GetGUIDString.UUID();
          table.insertRow();
          table.UpdateValue("afl_FileName", FileName);
          table.UpdateValue("afl_FileExe", FileExe);
          table.UpdateValue("afl_FilePath", LocalPath + '/' + fileName);
          table.UpdateValue("afl_DirID", DirID);
          table.UpdateValue("afl_Sha", FileSha1);
          table.UpdateValue("afl_key", FileKey);
          table.UpdateValue("afl_status", "I");
          table.UpdateValue("afl_date_lst", new Date());
          table.UpdateValue("afl_user_lst", UserName);
          table.UpdateValue("afl_size", file.length());

          table.PostRow();
        }
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        table.Close();
      }
      if (!OldFileSha1.equals(FileSha1)) {
        BackFile(file, FileKey, FileExe, FileSha1);
      }
      System.out.println("upload ok ");
      return fileName;
    }

    public String GetLocalFileName(HttpServletRequest request, HttpServletResponse resp, FileItem item) {
      String fileName = item.getName();
      String ExeName = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
      UUID uuid = UUID.randomUUID();
      fileName = uuid.toString().replaceAll("-", "").substring(0, 31) + "." + ExeName;
      fileName = fileName.toUpperCase();
      LocalFileName = fileName;
      return fileName;

    }

    public void doUploadOK(HttpServletRequest request, HttpServletResponse resp) throws IOException {

      JSONObject json = new JSONObject();
      json.put("FileName", LocalPathName + LocalFileName);
      json.put("FileKey", FileKey);
      json.put("MsgID", 1);
      resp.getWriter().print(json.toString());
      resp.getWriter().flush();
    }

    public ProgressListener doProgressListener(HttpServletRequest request, HttpServletResponse resp, final String Key)
        throws IOException {
      ProgressListener p = new ProgressListener() {

        public void update(long pBytesRead, long pContentLength, int arg2) {
          // System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
          // 向单例哈希表写入文件长度和初始进度
          ProgressSingleton.put(Key + "Type", "Temp");
          ProgressSingleton.put(Key + "ContentLength", pContentLength);
          ProgressSingleton.put(Key + "BytesRead", pBytesRead);
        }
      };
      return p;
    }

    public void BackFile(File OldFile, String key, String Exe, String FileSha1) throws IOException {
      String FileServerPath = (String) getServletContext().getAttribute("FileServerPath");

      Date ss = new Date();
      SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
      String TempPath = format.format(ss.getTime());// 这个就是把时间戳经过处理得到期望格式的时间
      String TempFile = GetGUIDString.UUID() + "." + Exe;
      String LocalPath = "BackupFile/" + TempPath;
      ExistDir.isExistDir(FileServerPath + LocalPath);

      File file = new File(FileServerPath + LocalPath, TempFile);
      System.out.println(file.getAbsolutePath());

      InputStream input = null;
      input = new FileInputStream(OldFile);
      FileOutputStream out = new FileOutputStream(file);
      try {
        byte[] buffer = new byte[1024];
        int readNumber = 0;
        while ((readNumber = input.read(buffer)) != -1) {
          // 每读取一次，更新一次进度大小
          out.write(buffer);
        }
      } finally {
        out.close();
        input.close();
      }

      DBTable table = new DBTable(SQLConnect.GetConnect(), "select * from afb_FileBack where 1<>1");
      try {
        table.Open();
        table.insertRow();
        table.UpdateValue("afb_key", key);
        table.UpdateValue("afb_FileName", TempFile);
        table.UpdateValue("afb_FilePath", "/" + LocalPath + "/" + TempFile);

        table.UpdateValue("afb_Sha", FileSha1);
        table.UpdateValue("afb_Datetime", new Date());
        table.UpdateValue("afb_status", "I");
        table.PostRow();
      } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        table.CloseAndFree();
      }
    }

    public String GetFilePathLocal(String DirID) {
      return FileWebPath.Path(DirID);
    }
  }

}
