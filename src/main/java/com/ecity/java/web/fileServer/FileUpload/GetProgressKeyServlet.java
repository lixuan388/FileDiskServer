package com.ecity.java.web.fileServer.FileUpload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecity.java.json.JSONObject;

import java.util.UUID;


@WebServlet("/File/GetProgressKey")

public class GetProgressKeyServlet extends HttpServlet {

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

		String ProgressKey=UUID.randomUUID().toString().replaceAll("-","").toUpperCase();
		request.getSession().setAttribute("ProgressKey", ProgressKey);
		

		final String SessionID= request.getSession().getId()+"_"+ProgressKey;

        ProgressSingleton.put(SessionID + "ProgressKey",ProgressKey);
        ProgressSingleton.put(SessionID + "Type", "doPost");
        ProgressSingleton.put(SessionID + "Type", "doPost");
        ProgressSingleton.put(SessionID + "ContentLength", 0);
        ProgressSingleton.put(SessionID + "BytesRead", 0);    
        
        
        JSONObject json = new JSONObject();
        json.put("ProgressKey", ProgressKey);
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
