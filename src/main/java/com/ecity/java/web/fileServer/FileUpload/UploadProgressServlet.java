package com.ecity.java.web.fileServer.FileUpload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecity.java.json.JSONObject;



@WebServlet("/File/GetUploadProgress")

public class UploadProgressServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("application/json;charset=utf-8");       


		String id =request.getParameter("id")==null?"0":request.getParameter("id");

		
		
        String Key = request.getSession().getId()+"_"+id;
        //使用sessionid + 文件名生成文件号，与上传的文件保持一致        
        Object ProgressKey = ProgressSingleton.get(Key + "ProgressKey");
        ProgressKey = ProgressKey == null ? "null" : ProgressKey;         
        
        Object Type = ProgressSingleton.get(Key + "Type");
        Type = Type == null ? "null" : Type;         
        Object size = ProgressSingleton.get(Key + "ContentLength");
        size = size == null ? 100 : size;
        Object progress = ProgressSingleton.get(Key + "BytesRead");        
        progress = progress == null ? 0 : progress; 
        
        JSONObject json = new JSONObject();
        json.put("ProgressKey", ProgressKey);
        json.put("type", Type);
        json.put("size", size);
        json.put("progress", progress);
        response.getWriter().print(json.toString());
        response.getWriter().flush();			
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
	
	

}
