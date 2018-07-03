package com.java.test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

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



@WebServlet("/testupload")



public class testupload extends HttpServlet {

	private static final long serialVersionUID = 3655349618159330684L;	
//  private File uploadPath;  
//  private File tempPath;

	private static final int MaxStoreSize=4*1024;
	private static final int MaxFileUploadExceptionSize=1024*1024*1024;
  
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

//        System.out.println("post:");
//		InputStream in=  req.getInputStream();
//		byte[] buf=new byte[1024];
//		int len=0;
//		while((len=in.read())!=-1){
//		    String str= new String(buf,0,len);
//		    System.out.println("len"+len);
//		}
		
		

		resp.setHeader("Cache-Control", "no-cache");
		
        
        
           
        //获取根目录对应的真实物理路径  
        File uploadPath =new File( getServletContext().getRealPath("/").replace(request.getContextPath().replace("/", "")+"\\", ""));
        //临时目录  
        File tempPath =new File( getServletContext().getRealPath("/").replace(request.getContextPath().replace("/", "")+"\\", ""));

             
          
    /********************************使用 FileUpload 组件解析表单********************/  

        System.out.println("DiskFileItemFactory");  
        //DiskFileItemFactory：创建 FileItem 对象的工厂，在这个工厂类中可以配置内存缓冲区大小和存放临时文件的目录。  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        // maximum size that will be stored in memory  
        factory.setSizeThreshold(MaxStoreSize);  
        // the location for saving data that is larger than getSizeThreshold()  
        factory.setRepository(tempPath);  
          
        //ServletFileUpload：负责处理上传的文件数据，并将每部分的数据封装成一到 FileItem 对象中。  
        //在接收上传文件数据时，会将内容保存到内存缓存区中，如果文件内容超过了 DiskFileItemFactory 指定的缓冲区的大小，  
        //那么文件将被保存到磁盘上，存储为 DiskFileItemFactory 指定目录中的临时文件。  
        //等文件数据都接收完毕后，ServletUpload再从文件中将数据写入到上传文件目录下的文件中

        System.out.println("ServletFileUpload");  
        
        ServletFileUpload upload = new ServletFileUpload(factory);  
        // maximum size before a FileUploadException will be thrown  
        upload.setSizeMax(MaxFileUploadExceptionSize);        

        upload.setProgressListener(doProgressListener(request,resp,"SessionID"));
          
        /*******************************解析表单传递过来的数据，返回List集合数据-类型:FileItem***********/  
          
        try {                


            System.out.println("upload.parseRequest(request)");  
            List fileItems = upload.parseRequest(request);         
            System.out.println("upload.parseRequest(request) OK");       
            

            
            

//            for (Iterator iter = fileItems.iterator(); iter.hasNext();) {
//                //获得序列中的下一个元素  
//                FileItem item = (FileItem) iter.next();  
//                //判断是文件还是文本信息  
//                //是普通的表单输入域  
//                if(item.isFormField()) {  
//                    if ("itemNo".equals(item.getFieldName())) {  
//                        itemNo = item.getString();  
//                    }        	    
//                }    
//            }  

        } catch (Exception e) {  
            e.printStackTrace();  
        }     
	    resp.getWriter().flush();				
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(arg0, arg1);
	}
    public ProgressListener doProgressListener(HttpServletRequest request, HttpServletResponse resp,final String Key) throws IOException
    {
    	ProgressListener p =new ProgressListener(){
        	
            public void update(long pBytesRead, long pContentLength, int arg2) {
//                System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead);
                //向单例哈希表写入文件长度和初始进度
            	
            	System.out.println("doProgressListener");
//            	System.out.println("ContentLength:"+pContentLength);
//            	System.out.println("BytesRead:"+ pBytesRead);     
            }
            
        };
        return p;
    }
}
