package com.ecity.java.web.fileServer.fun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.ecity.java.sql.db.DBTable;
import com.java.sql.SQLConnect;

public class FileWebPath {

  public static Map<String, String> map = new HashMap<String, String>();

  public static void Init() {
    DBTable table = new DBTable(SQLConnect.GetConnect(),
        "select adl_dirname,adl_parentid,adl_id from adl_dirlist order by adl_parentid,adl_id ");
    map.clear();
    try {
      table.Open();
      while (table.next()) {
        String ParentPath = map.get(table.getString("adl_parentid")) == null ? ""
            : map.get(table.getString("adl_parentid"));
        map.put(table.getString("adl_id"), ParentPath + "/" + table.getString("adl_dirname"));
      }
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }

  }

  public static String Path(String key) {
    String value = map.get(key) == null ? "" : map.get(key);
    return value;
  }

  public static String PathByName(String Name) {
    for (Map.Entry entry : map.entrySet()) {
      if (Name.equals(entry.getValue())) {
        return (String) entry.getKey();
      }
    }
    return "-1";
  }

  public static void UpdatePath(String ParentID) {
    DBTable table = new DBTable(SQLConnect.GetConnect(),"select adl_dirname,adl_parentid,adl_id from adl_dirlist where adl_parentid='"
        + ParentID + "' order by adl_parentid,adl_id ");
    try {
      table.Open();
      while (table.next()) {
        String ParentPath = map.get(table.getString("adl_parentid")) == null ? ""
            : map.get(table.getString("adl_parentid"));
        map.put(table.getString("adl_id"), ParentPath + "/" + table.getString("adl_dirname"));
      }
    }catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } finally {
      table.CloseAndFree();
    }
  }

}
