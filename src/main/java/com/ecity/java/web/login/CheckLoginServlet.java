package com.ecity.java.web.login;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;

import com.ecity.java.json.JSONObject;
import com.ecity.java.sql.db.DBTable;
import com.ecity.java.web.WebFunction;
import com.java.version;
import com.java.sql.SQLConnect;

/**
 * 登录验证
 */

@WebServlet("/Login/CheckLogin")
public class CheckLoginServlet extends HttpServlet {

  private static final long serialVersionUID = 1221671299145751538L;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
//        System.out.println("doPost begin");
    Map<String, String[]> params = req.getParameterMap();
    String UserCode = params.get("UserCode") == null ? "" : (String) (params.get("UserCode")[0]);
    String PassWord = params.get("PassWord") == null ? "" : (String) (params.get("PassWord")[0]);
//           System.out.println("doPost end");
    CheckLogin(req, resp, UserCode, PassWord);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // TODO Auto-generated method stub
    doGet(req, resp);

  }

  public void CheckLogin(HttpServletRequest req, HttpServletResponse resp, String UserCode, String PassWord)
      throws IOException {
    resp.setContentType("application/json;charset=utf-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setHeader("Cache-Control", "no-cache");
    // 使用request对象的getSession()获取session，如果session不存在则创建�?�?

    UserCode = URLDecoder.decode(UserCode, "UTF-8");
    JSONObject ReturnJson = new JSONObject();

    if (UserCode.equals("")) {
      ReturnJson.put("MsgID", "-1");
      ReturnJson.put("MsgTest", "请输入用户名！");
    } else {

      DBTable table = new DBTable(SQLConnect.GetConnect(),
          "select AUs_ID,AUs_UserName,AUs_UserCode,aus_password from smarttraveldb.dbo.aus_users where aus_status<>'D' and  aus_UserCode='"
              + UserCode + "'");
      try {
          table.Open();
        if (!table.next()) {
          ReturnJson.put("MsgID", "-1");
          ReturnJson.put("MsgTest", "用户名不存在");
        } else {

          String PassWord3 = DigestUtils.sha1Hex(PassWord).substring(0, 8).toUpperCase();
          String PassWord2 = table.getString("aus_password");
          if (!PassWord2.equals(PassWord3)) {
            ReturnJson.put("MsgID", "-2");
            ReturnJson.put("MsgTest", "密码错误！");
          }
          else {
            HttpSession session = req.getSession();
            String sessionId = session.getId();
            // 将数据存储到session�?
            session.setAttribute("UserSessionID", sessionId);
            session.setAttribute("UserID", table.getString("AUs_ID"));
            session.setAttribute("UserName", table.getString("AUs_UserName"));
            session.setAttribute("UserCode", table.getString("AUs_UserCode"));
            session.setAttribute("IsAdmin", false);
            ReturnJson.put("MsgID", "1");
            ReturnJson.put("MsgTest", "Success");

            JSONObject ConfigJson=new JSONObject();
            ConfigJson.put("ServerName", WebFunction.GetServerNameUrl(req));
            ConfigJson.put("ContextPath", WebFunction.GetContextPath(req));
            ConfigJson.put("Version", version.Version);
            ReturnJson.put("Config",ConfigJson);            
          }
        }
      }catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        table.CloseAndFree();
      }
    }
//  System.out.println(ReturnJson.toString());
    resp.getWriter().print(ReturnJson.toString());
    resp.getWriter().flush();
  }

}
