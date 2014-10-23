package com.an.sfs.vo;

import java.io.File;

import com.an.sfs.FileUtil;

/**
 * Used to sort files
 * 
 * @author Anthony
 * 
 */
public class FileVo implements Comparable<FileVo> {
    private File file;

    public FileVo() {
    }

    /**
     * @param filePath
     */
    public FileVo(File file) {
        this.file = file;
    }

    @Override
    public int compareTo(FileVo o) {
        int turn = Integer.valueOf(FileUtil.getFileNameWithoutSuffix(file.toString()));
        int oTurn = Integer.valueOf(FileUtil.getFileNameWithoutSuffix(o.getFile().toString()));
        return (turn - oTurn);
    }

    @Override
    public String toString() {
        return "FileVo [file=" + file + "]";
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
