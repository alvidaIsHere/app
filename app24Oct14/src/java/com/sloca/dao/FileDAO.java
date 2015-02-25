package com.sloca.dao;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;



public class FileDAO {
    public static boolean unzipFile(String unzipPath, String zipFile){  
        
        File dir = new File(unzipPath);
        // check if directory exists
        if(!dir.exists())   {
            dir.mkdir();
        } 
        
        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        ZipEntry zEntry = null;
        
        try {
            fis = new FileInputStream(zipFile);
            zipIs = new ZipInputStream(fis);
            System.out.println("Extracting file name: " + zipFile);
            while((zEntry = zipIs.getNextEntry()) != null){
                try{
                    byte[] tmp = new byte[4*1024];
                    FileOutputStream fos = null;                                        
                    String opFilePath = unzipPath + File.separator + zEntry.getName();
                    // create new file
                    File newFile = new File(opFilePath);
                    // place file in new directory
                    new File(newFile.getParent()).mkdirs();                    
                    System.out.println("Extracting file to " + opFilePath);
                    fos = new FileOutputStream(opFilePath);
                    int size = 0;
                    while((size = zipIs.read(tmp)) != -1){
                        fos.write(tmp, 0 , size);
                    }
                    fos.flush();
                    fos.close();
                } catch(Exception ex){
                     // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }
            zipIs.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }
}
