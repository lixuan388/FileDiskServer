package com.ecity.java.web.fileServer.servlet;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.java.sql.table.MySQLTable;

@WebServlet("/File/GetFilePath")
public class GetFilePathServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4341641892587343773L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=utf-8"); 
		response.setCharacterEncoding("UTF-8");  
		response.setHeader("Cache-Control", "no-cache");

		String id =request.getParameter("id")==null?"-1":request.getParameter("id");
		String name =request.getParameter("name")==null?"":request.getParameter("name");

		name=URLDecoder.decode(name,"UTF-8");
		
//		System.out.println(name);

		net.sf.json.JSONObject FileLists = new net.sf.json.JSONObject();      

		FileLists.put("PathID",FileWebPath.PathByName(name));
		FileLists.put("PathName",FileWebPath.Path(id));
		response.getWriter().print(FileLists.toString());
		response.getWriter().flush();		
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
