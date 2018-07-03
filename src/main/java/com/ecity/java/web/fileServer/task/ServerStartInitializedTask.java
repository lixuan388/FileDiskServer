package com.ecity.java.web.fileServer.task;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ecity.java.web.fileServer.fun.FileWebPath;
import com.ecity.java.web.system.Config;
import com.java.sql.SQLConnect;

public class ServerStartInitializedTask implements ServletContextListener {

  public void contextDestroyed(ServletContextEvent event) {
    System.out.println("ServerStartTask.contextDestroyed");
  }

  public void contextInitialized(ServletContextEvent event) {
    System.out.println("ServerStartTask.contextInitialized");

    System.out.println("FileDiskServer.ServerStartTask.contextInitialized");
    String FileServerPath="";
    String tomcatPath = System.getProperty("catalina.home");
    String ConfigPath = tomcatPath + "\\conf\\webConfig.properties";
    try {
      Config c = new Config(ConfigPath);
      c.load();
      SQLConnect.Url = c.getProperty("FileDiskServer.SQLConnect.Url");
      SQLConnect.DriverClassName = c.getProperty("FileDiskServer.SQLConnect.DriverClassName");
      SQLConnect.Username = c.getProperty("FileDiskServer.SQLConnect.Username");
      SQLConnect.Password = c.getProperty("FileDiskServer.SQLConnect.Password");
      FileServerPath = c.getProperty("FileDiskServer.FileServerPath");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.err.println("-------------------");
      System.err.println("配置文件读取失败！");
      System.err.println(ConfigPath);
    }

    FileWebPath.Init();
    //=event.getServletContext().getInitParameter("FileServerPath");
    System.out.println("FileServerPath:"+FileServerPath);
    event.getServletContext().setAttribute("FileServerPath",FileServerPath);
  }
}
