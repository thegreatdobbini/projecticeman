package com.deshpande.camerademo;

import android.content.Context;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

public class MyFTPClientFunctions {
    public FTPClient mFTPClient = null;

    public String ftpConnect(String host, String username, String password, int port, String path, Context context, char intent, String destName) {
        try {
            mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(host, port);
            Log.d(TAG, "Below:");
            Log.d(TAG, mFTPClient.getReplyString());
            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                // login using username & password
                //mFTPClient.enterLocalPassiveMode();
                boolean status = mFTPClient.login(username, password);
                mFTPClient.setBufferSize(1024 * 1024);
                mFTPClient.changeWorkingDirectory("upload");
                Log.d(TAG, "Below number 2:");
                Log.d(TAG, mFTPClient.getReplyString());
                /*
                 * Set File Transfer Mode
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                 * transferring text, image, and compressed files.
                 */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);

                if(intent == 'u'){
                    ftpUpload(path, destName, "", context);
                }
                else if(intent == 'd'){
                    ftpDown(destName, "/storage/emulated/0/" + destName);
                }
                Log.d(TAG, "Below number 3:");
                String log = mFTPClient.getReplyString().trim();
                Log.d(TAG, "," + log + ",");

                mFTPClient.changeToParentDirectory();
                //mFTPClient.enterLocalPassiveMode();
                Log.d(TAG, Boolean.toString(log == "550 Failed to open file."));
                Log.d(TAG, log);
                Log.d(TAG, "550 Failed to open file.");
                if(log.charAt(0) == '5' && log.charAt(1) == '5' && log.charAt(2) == '0') {
                    return "nf";
                }else{
                    return "f";
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "Error: could not connect to host " + host);
        }
        return "Error.";
    }

    public boolean ftpDisconnect() {
        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            return true;
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
            Log.d(TAG, "Error occurred while disconnecting from ftp server.");
        }
        return false;
    }

    public boolean ftpUpload(String srcFilePath, String desFileName, String desDirectory, Context context) {
        boolean status = false;
        try {
            FileInputStream srcFileStream = new FileInputStream(srcFilePath);
            // change working directory to the destination directory
            // if (ftpChangeDirectory(desDirectory)) {
            status = mFTPClient.storeFile(desFileName, srcFileStream);
            //}
            Log.d(TAG, "Stored? " + status);
            //Log.d(TAG, "Below number 4:");
            //Log.d(TAG, Boolean.toString(mFTPClient.storeFile(desFileName, srcFileStream)));
            srcFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "upload failed: " + e);
        }
        return status;
    }

    public boolean ftpDown(String serverName, String destFile){
        boolean status = false;
        try{
            OutputStream out = new FileOutputStream(destFile);
            status = mFTPClient.retrieveFile(serverName, out);
            out.close();
            return status;
        } catch (Exception e){
            e.printStackTrace();
            Log.d(TAG, "Download failed: " + e);
        }
        return false;
    }
}
