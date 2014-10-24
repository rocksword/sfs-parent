package com.an.sfs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPageReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebPageReader.class);
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * @param urlStr
     * @return
     */
    public String read(String urlStr) {
        StringBuffer result = new StringBuffer();
        BufferedReader reader = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String contentType = conn.getContentType();
            LOGGER.debug("contentType {}", contentType);
            String charset = getCharset(contentType);
            InputStreamReader in = new InputStreamReader(url.openStream(), charset);
            reader = new BufferedReader(in);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    if (line.contains("股东人数(户)")) {
                        // line = line.replace("<th", "\n<th");
                        line = line.replace("<tr", "\n<tr");
                        // line = line.replace("<td", "\n<td");
                        line = line.replace("<table", "\n<table");
                    }
                    result.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            LOGGER.error("urlStr {}", urlStr);
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.error("Error: ", e);
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }

    private String getCharset(String str) {
        if (str == null || str.trim().isEmpty()) {
            return DEFAULT_CHARSET;
        }
        String result = null;
        Pattern pat = Pattern.compile("charset=.*");
        Matcher mat = pat.matcher(str);
        if (mat.find()) {
            result = mat.group(0).split("charset=")[1];
        }
        if (result == null || result.trim().isEmpty()) {
            return DEFAULT_CHARSET;
        }
        return result;
    }
}
