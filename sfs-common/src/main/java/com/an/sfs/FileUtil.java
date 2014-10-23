package com.an.sfs;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);
    public static final String DEFAULT_CHARTSET = "UTF-8";

    public static void main(String[] args) {
        String filepath = "D:\\github\\lfs-parent\\raw\\eng\\2013\\1.txt";
        System.out.println(FileUtil.getFileName(filepath));
        System.out.println(FileUtil.getFileNameWithoutSuffix(filepath));
    }

    /**
     * @param filepath
     *            sfs-parent\raw\eng\2013\1.txt
     * @return 1.txt
     */
    public static String getFileName(String filepath) {
        int s = filepath.lastIndexOf(File.separator);
        String filename = filepath.substring(s + 1);
        return filename;
    }

    /**
     * @param filepath
     *            sfs-parent\raw\eng\2013\1.txt
     * @return 1
     */
    public static String getFileNameWithoutSuffix(String filepath) {
        int s = filepath.lastIndexOf(File.separator);
        int e = filepath.lastIndexOf(".");
        String filename = filepath.substring(s + 1, e);
        return filename;
    }

    /**
     * @param dirPath
     */
    public static void createDir(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
                LOGGER.info("Finished creating dir {}", dir.toString());
            } catch (SecurityException e) {
                LOGGER.error("Error: ", e);
                e.printStackTrace();
            }
        } else {
            LOGGER.info("Dir {} exists.", dir.toString());
        }
    }

    /**
     * @param filePath
     * @param content
     */
    public static void writeFile(String filePath, String content) {
        File file = new File(filePath);
        LOGGER.info("Write file {}", filePath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(filePath);
                Writer bf = new BufferedWriter(new OutputStreamWriter(fos, DEFAULT_CHARTSET));) {
            bf.write(content);
        } catch (IOException e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
    }
}
