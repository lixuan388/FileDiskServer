package com.ecity.java.web.fileServer.fun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;


public class FileDigestUtil {


    /*** 
     * 计算SHA1码 
     *  
     * @return String 适用于上G大的文件 
     * @throws FileNotFoundException 
     * @throws NoSuchAlgorithmException 
     * */  
    public static String getSha1(File file) 
    {  
        FileInputStream in;

		String sha1Str="";
		
		//SHA1加密
		try {
			in = new FileInputStream(file);
			sha1Str = DigestUtils.sha1Hex(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(sha1Str);
		return sha1Str.toUpperCase();

		
		
    }  
  
}
