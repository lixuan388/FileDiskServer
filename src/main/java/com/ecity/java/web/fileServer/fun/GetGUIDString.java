package com.ecity.java.web.fileServer.fun;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/GetGUIDString")
public class GetGUIDString extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		resp.setContentType("text/html;charset=utf-8");		
		
		resp.getWriter().print(UUID());	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	static public String UUID()
	{
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-","").toUpperCase();
		
	}
}
