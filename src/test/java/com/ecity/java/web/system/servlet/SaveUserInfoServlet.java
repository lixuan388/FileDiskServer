package com.ecity.java.web.system.servlet;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import com.java.sql.table.MySQLTable;

@WebServlet("/Content/System/SaveUserInfo")
public class SaveUserInfoServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4341641892587343773L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		
		Map<String, String[]> params = request.getParameterMap();
		String ID =params.get("ID")==null?"":(String)(params.get("ID")[0]);
		String UserName =params.get("UserName")==null?"":(String)(params.get("UserName")[0]);
		String UserCode =params.get("UserCode")==null?"":(String)(params.get("UserCode")[0]);
		String UserDeleteFlg =params.get("UserDeleteFlg")==null?"":(String)(params.get("UserDeleteFlg")[0]);
		String UserIsAdmin =params.get("UserIsAdmin")==null?"":(String)(params.get("UserIsAdmin")[0]);

//		System.out.println("ID:"+ID);
//		System.out.println("UserName:"+UserName);
//		System.out.println("UserCode:"+UserCode);
//		System.out.println("UserDeleteFlg:"+UserDeleteFlg);
//		System.out.println("UserIsAdmin:"+UserIsAdmin);
		
		
		
		resp.setContentType("application/json;charset=utf-8"); 
		resp.setCharacterEncoding("UTF-8");  
		resp.setHeader("Cache-Control", "no-cache");
	    //使用request对象的getSession()获取session，如果session不存在则创建�?�?		

//		System.out.println(UserCode);
		UserCode=URLDecoder.decode(UserCode,"UTF-8");
//		System.out.println(UserCode);
		net.sf.json.JSONObject ReturnJson = new net.sf.json.JSONObject();		
   

//		System.out.println("begin");
		
		
		if (!(Boolean)request.getSession().getAttribute("IsAdmin"))
		{
			ReturnJson.put("MsgID","-1");
			ReturnJson.put("MsgTest","无权限修改账号信息！");
		}
		else if (ID.equals(""))
		{
			ReturnJson.put("MsgID","-1");
			ReturnJson.put("MsgTest","ID不可这空！");
		}
		else if (UserName.equals(""))
		{
			ReturnJson.put("MsgID","-1");
			ReturnJson.put("MsgTest","请姓名不可这空！");
		}
		else if (UserCode.equals(""))
		{
			ReturnJson.put("MsgID","-1");
			ReturnJson.put("MsgTest","请账号不可这空！");
		}
		else
		{
			MySQLTable SameTable=new MySQLTable("select AUs_ID from aus_users where aus_status<>'D' and (AUs_UserName='"+UserName+"' or AUs_UserCode='"+UserCode+"') and  aus_id<>'"+ID+"'");
			try
			{
				SameTable.Open();
				if (SameTable.next())
				{
					ReturnJson.put("MsgID","-1");
					ReturnJson.put("MsgTest","姓名或账号已存在，不可重复！");
					resp.getWriter().print(ReturnJson.toString());
			        resp.getWriter().flush();	
			        return;
				}
			}
			finally
			{
				SameTable.Close();
			}
			
			
			MySQLTable table=new MySQLTable("select AUs_ID,AUs_UserName,AUs_UserCode,aus_password,aus_STEdit,aus_deleteFlg,"
						+"aus_status,aus_user_lst,aus_User_ins,aus_date_lst,aus_date_ins"+
						" from aus_users where aus_status<>'D' and  aus_id='"+ID+"'");
			try
			{
				table.Open();
//				System.out.println("open");
				if (!table.next())
				{					
					if (ID.equals("-1"))
					{
//						System.out.println("moveToInsertRow");
						table.moveToInsertRow();
//						System.out.println("moveToInsertRow end");
//						table.updateString("AUs_ID", "2");
						table.updateString("AUs_UserName", UserName);
						table.updateString("AUs_UserCode", UserCode);
						table.updateBoolean("aus_deleteFlg", UserDeleteFlg.equals("true"));
						table.updateBoolean("aus_STEdit", UserIsAdmin.equals("true"));

						String PassWord3=DigestUtils.sha1Hex("888888").substring(0, 8).toUpperCase();
						table.updateString("aus_password", PassWord3);
						
						
						table.updateString("aus_status", "I");
						table.updateString("aus_User_ins", (String)request.getSession().getAttribute("UserName"));
						table.updateDateTime("aus_date_ins", new Date());

						table.updateString("AUs_UserCode", UserCode);
//						System.out.println("updateRow");
						table.PostRow();

//						System.out.println("updateRow end ");
				        ReturnJson.put("MsgID","1");
						ReturnJson.put("MsgTest","Success");
					}
					else
					{
						ReturnJson.put("MsgID","-1");
						ReturnJson.put("MsgTest","用户名不存在");
					}
				}
				else
				{
//					System.out.println("Update");
					table.updateString("AUs_UserName", UserName);
					table.updateString("AUs_UserCode", UserCode);
					table.updateBoolean("aus_deleteFlg", UserDeleteFlg.equals("true"));
					table.updateBoolean("aus_STEdit", UserIsAdmin.equals("true"));
					
					table.updateString("aus_status", "E");
					table.updateString("aus_user_lst", (String)request.getSession().getAttribute("UserName"));
					table.updateDateTime("aus_date_lst", new Date());					
					
					table.updateRow();
					
			        ReturnJson.put("MsgID","1");
					ReturnJson.put("MsgTest","Success");
				}
			}
			finally
			{
				table.Close();
			}
		}
//		System.out.println(ReturnJson.toString());
        resp.getWriter().print(ReturnJson.toString());
        resp.getWriter().flush();	
	}

}
