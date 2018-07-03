package com.java.web.weixin.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.CharBuffer;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class WeiXinVariable {
	public static String AppID="";
	public static String AppSecret="";
	public static String AccessToken="";
	public static long AccessTokenTimeOut=0;
	public static String jsapi_ticket="";
	public static long jsapi_ticket_TimeOut=0;
	
	public static String APIKey="";
	public static String SystemServerUrl="http://127.0.0.1";
	
	
	
}
