package com.an.sfs;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.vo.FileVo;

public class SfsUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(SfsUtil.class);
    public static final String SFS_PARENT_HOME = "SFS_PARENT_HOME";
    private static final String DIR_DATA = "D:\\data";
    private static final String DIR_CONF = "conf";
    private static final String DIR_SHAREHOLDER = "shareholder";
    private static final String DIR_SZ = "sz";

    public static final DecimalFormat FLOAT_FORMAT_0 = new DecimalFormat("##0");
    public static final DecimalFormat FLOAT_FORMAT_1 = new DecimalFormat("##0.0");
    public static final DecimalFormat FLOAT_FORMAT_2 = new DecimalFormat("##0.00");

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT_FULL = "yyyy-MM-dd hh:mm:ss";
    public static final int LEAGUE_FROM_YEAR = 2004;
    public static final int LEAGUE_TO_YEAR = 2014;

    /**
     * @param dirPath
     * @return
     */
    public static File[] getFilesInDir(String dirPath) {
        if (dirPath == null || dirPath.isEmpty()) {
            LOGGER.warn("Empty dir path {}", dirPath);
            return null;
        }
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            LOGGER.error("Not found {}", dirPath);
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            LOGGER.warn("Not found rate files.");
            return null;
        }
        return files;
    }

    public static List<File> getFilesInDirSorted(String dirPath) {
        if (dirPath == null || dirPath.isEmpty()) {
            LOGGER.warn("Empty dir path {}", dirPath);
            return null;
        }
        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {
            LOGGER.error("Not found {}", dirPath);
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null) {
            LOGGER.warn("Not found rate files.");
            return null;
        }

        List<FileVo> fileVoList = new ArrayList<>();
        for (File f : files) {
            fileVoList.add(new FileVo(f));
        }
        Collections.sort(fileVoList);

        List<File> result = new ArrayList<>();
        for (FileVo vo : fileVoList) {
            result.add(vo.getFile());
        }
        return result;
    }

    /**
     * @return \sfs-parent
     */
    public static String getHomeDirPath() {
        String lfsParentHome = System.getenv(SFS_PARENT_HOME);
        if (lfsParentHome == null || lfsParentHome.trim().isEmpty()) {
            throw new RuntimeException("Not found SFS_PARENT_HOME, please set it.");
        }
        return new File(lfsParentHome).getAbsolutePath();
    }

    /**
     * @param fileName
     * @return data\fileName
     */
    public static String getDataDirPath(String fileName) {
        String homeDir = new File(DIR_DATA).getAbsolutePath();
        return new StringBuilder().append(homeDir).append(File.separator).append(fileName).toString();
    }

    /**
     * @param fileName
     * @return data\shanghai.ebk or data\shenzhen.ebk
     */
    public static String getStockCodeFilePath(String fileName) {
        String homeDir = new File(DIR_DATA).getAbsolutePath();
        return new StringBuilder().append(homeDir).append(File.separator).append(fileName).toString();
    }

    /**
     * @param fileName
     * @return data\shareholder\filename
     */
    public static String getShareholderFilePath(String fileName) {
        String homeDir = new File(DIR_DATA).getAbsolutePath();
        return new StringBuilder().append(homeDir).append(File.separator).append(DIR_SHAREHOLDER)
                .append(File.separator).append(fileName).toString();
    }

    /**
     * @param fileName
     * @return
     */
    public static String getConfFilePath(String fileName) {
        String homeDir = getHomeDirPath();
        return new StringBuilder().append(homeDir).append(File.separator).append(DIR_CONF).append(File.separator)
                .append(fileName).toString();
    }

    private static final String BASE_URL = "http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?";

    /**
     * @param league
     * @param turnIndex
     * @return http://f10.eastmoney.com/f10_v2/ShareholderResearch.aspx?code=sh600000
     */
    public static String getShareholderResearchUrl(String code) {
        StringBuilder ret = new StringBuilder(BASE_URL).append("code=").append(code);
        return ret.toString();
    }
}
