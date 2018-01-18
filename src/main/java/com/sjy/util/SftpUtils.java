package com.sjy.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SftpUtils {
	
    protected String host;
    protected int port;
    protected String username;
    protected String password;

    /**
     * @param host ip
     * @param port 端口
     * @param username 账号
     * @param password 密码
     * */
    public SftpUtils(String host, int port, String username, String password) {
        set(host, port, username, password);
    }

    public void set(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    Session sshSession = null ; 
    
    /**
     * 链接linux
     * */
    public ChannelSftp connect() {
        ChannelSftp sftp = null ;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            sshSession = jsch.getSession(username, host, port);
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            log.info(String.format("%s connect success" , host));
            Channel channel = sshSession.openChannel("sftp");
            channel.connect() ; 
            sftp = (ChannelSftp) channel;
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("connect:" + host+"{}", e ); 
            close( null );
            return null ; 
        }
        return sftp;
    }
    /**
     * linux上传文件
     * @param ftpPath 文件目录
     * @param file  本地文件
     */
    public boolean uploadSftpFile(String ftpPath ,String localPath,String fileName){
        ChannelSftp sftp = connect() ; 
        try {
            if(null != sftp){
                sftp.cd(ftpPath);
                log.info(String.format("cd %s" , ftpPath));
                File file = new File(localPath+fileName);
                sftp.put(new FileInputStream(file), file.getName());
            }
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("uploadSftpFile异常:" + e.getMessage());
        }finally{
            close(sftp);
        }
        return false;
    }

    /**
     * linux下载文件
     * @param ftpPath 下载文件路径
     * @param localPath  本地文件路径
     * @param fileName  文件名称
     */
    public void downloadSftpFile(String ftpPath, String localPath,String fileName) {
        ChannelSftp sftp = connect() ; 
        try {
            if(null != sftp){
            	String ftpFilePath = ftpPath + "/" + fileName;
        		String localFilePath = localPath + File.separatorChar + fileName;
                sftp.get(ftpFilePath , localFilePath); 
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	log.error("linux下载文件异常"+e.getMessage());
        }finally{
            close(sftp);
        }
    }

    protected void close(ChannelSftp sftp){
        if(sftp!=null){
            sftp.disconnect() ;
            sftp.exit();
        }
        if(sshSession!=null){
            sshSession.disconnect();
        }
    }

    public static void main(String[] args) {
        /**
         * @param host ip
         * @param port 端口
         * @param username 账号
         * @param password 密码
         * */
        SftpUtils sftpUtils = new SftpUtils("192.168.1.68", 22 , "root" , "zhy2017") ; 
//        sftpUtils.uploadSftpFile("/webapps/uploads/","d:/download/","12492974011120180119.txt");
//        System.out.println("上传成功");
        sftpUtils.downloadSftpFile("/webapps/uploads/" ,"D:/download","12492974011120180118.txt");
        System.out.println("下载成功");
    }
}