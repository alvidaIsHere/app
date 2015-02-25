package com.sloca.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileDAO {

    /**
     * Method to unzip file. Unzipped files will be stored in "./uploads/"
     *
     * @param unzipPath the directory needed to unzip the file
     * @param zipFile the filename of file to be unzipped
     * @return true if successfully unzip the file, else return false
     */
    public static boolean unzipFile(String unzipPath, String zipFile) {

        File dir = new File(unzipPath);
        // check if directory exists
        if (!dir.exists()) {
            dir.mkdir();
        }

        FileInputStream fis = null;
        ZipInputStream zipIs = null;
        ZipEntry zEntry = null;

        try {
            fis = new FileInputStream(zipFile);
            zipIs = new ZipInputStream(fis);
            while ((zEntry = zipIs.getNextEntry()) != null) {
                try {
                    byte[] tmp = new byte[4 * 1024];
                    FileOutputStream fos = null;
                    String opFilePath = unzipPath + File.separator + zEntry.getName();
                    // create new file
                    File newFile = new File(opFilePath);
                    // place file in new directory
                    new File(newFile.getParent()).mkdirs();
                    fos = new FileOutputStream(opFilePath);
                    int size = 0;
                    while ((size = zipIs.read(tmp)) != -1) {
                        fos.write(tmp, 0, size);
                    }
                    fos.flush();
                    fos.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zipIs.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
