

package com.ecity.java.web.fileServer.FileUpload;


import java.io.File;
import java.io.IOException;

/**
 * @author Administrator
 * @category Java�ļ��в���_�ж϶༶·���Ƿ����_�����ھʹ���,������windows��linux�µ�·���ַ�������,����Windows��Linux
 *
 */
public class ExistDir {
	

	
	 public static void isExistDir(String filePath) {
	        String paths[] = {""};
	        //�и�·��
	        try {
	            String tempPath = new File(filePath).getCanonicalPath();//File����ת��Ϊ��׼·���������и������windows��linux

//	            System.out.println("·����"+tempPath);
	            paths = tempPath.split("\\\\");//windows            
	            if(paths.length==1){paths = tempPath.split("/");}//linux
	        } catch (IOException e) {
//	            System.out.println("�и�·������");
	            e.printStackTrace();
	        }
	        //�ж��Ƿ��к�׺
	        boolean hasType = false;
	        if(paths.length>0){
	            String tempPath = paths[paths.length-1];
	            if(tempPath.length()>0){
	                if(tempPath.indexOf(".")>0){
	                    hasType=true;
	                }
	            }
	        }        
	        //�����ļ���
	        String dir = paths[0];
	        for (int i = 0; i < paths.length - (hasType?2:1); i++) {// ע��˴�ѭ���ĳ��ȣ��к�׺�ľ����ļ�·����û�����ļ���·��
	            try {
	                dir = dir + "/" + paths[i + 1];
	                File dirFile = new File(dir);
	                if (!dirFile.exists()) {
	                    dirFile.mkdir();
//	                    System.out.println("�ɹ�����Ŀ¼��" + dirFile.getCanonicalFile());
	                }
	            } catch (Exception e) {
//	                System.err.println("�ļ��д��������쳣");
	                e.printStackTrace();
	            }
	        }
	    }    
}
