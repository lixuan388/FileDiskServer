package com.ecity.java.web.fileServer.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.java.sql.table.MySQLTable;

@WebServlet("/File/GetFileListJson")
public class GetFileListJsonServlet extends HttpServlet {

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

		String id =request.getParameter("id")==null?"":request.getParameter("id");
		String key =request.getParameter("key")==null?"":request.getParameter("key");

		net.sf.json.JSONObject FileLists = new net.sf.json.JSONObject();      
		net.sf.json.JSONArray Files = new net.sf.json.JSONArray();     
		
		
		MySQLTable table=new MySQLTable();

		try
		{
			if (!key.equals(""))
			{
				table.SQL("select * from afl_FileList where afl_status<>'D' and afl_key='"+key+"'   order by afl_FilePath,afl_FileName");
			}
			else if (!id.equals(""))
			{
				table.SQL("select * from afl_FileList where afl_status<>'D' and afl_id="+id+"  order by afl_FilePath,afl_FileName");
			}
			else
			{
				table.SQL("select * from afl_FileList where afl_status<>'D'   order by afl_FilePath,afl_FileName");
			}
			table.Open();
			while (table.next())
			{   
				Files.add(GetFileInfo(table));
	//			System.out.println(table.getString("afl_FileName"));
			}
		}
		finally
		{
			table.Close();
		}
		FileLists.put("File", Files);
		response.getWriter().print(FileLists.toString());
		response.getWriter().flush();		
	}
	
	public net.sf.json.JSONObject GetFileInfo(MySQLTable table)
	{
		
		net.sf.json.JSONObject FileInfo = new net.sf.json.JSONObject();            
		

		FileInfo.put("FileName",table.getString("afl_FileName"));
		FileInfo.put("FileExe",table.getString("afl_FileExe"));
		FileInfo.put("FilePath",table.getString("afl_FilePath"));
		FileInfo.put("Sha",table.getString("afl_Sha"));
		FileInfo.put("FileExe",table.getString("afl_FileExe"));
		FileInfo.put("DirID",table.getString("afl_DirID"));
		FileInfo.put("FileID",table.getString("afl_id"));
		
		return FileInfo;
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

}
