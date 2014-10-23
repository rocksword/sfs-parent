package com.an.sfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileLineIterator implements AutoCloseable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileLineIterator.class);
    private String filepath = null;
    private BufferedReader br = null;
    private static final String DEFAULT_CHARTSET = "UTF-8";

    /**
     * @param filepath
     *            absolute path
     */
    public FileLineIterator(String filepath) {
        this.filepath = filepath;
    }

    public String nextLine() {
        File file = new File(filepath);
        if (!file.exists()) {
            LOGGER.warn("Not found {}", file.getName());
            return null;
        }

        String line = null;
        try {
            if (br == null) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(filepath), DEFAULT_CHARTSET);
                br = new BufferedReader(read);
            }
            line = br.readLine();
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
        return line;
    }

    @Override
    public void close() throws Exception {
        if (br != null) {
            try {
                LOGGER.debug("Close BufferedReader " + br);
                br.close();
            } catch (Exception e) {
                LOGGER.error("Error: ", e);
                e.printStackTrace();
            }
        }
    }
}