package com.an.sfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.po.StockPo;

public class CrawlMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlMain.class);
    private static final boolean INIT_DIR = false;
    private static final boolean INIT_STOCK = false;
    private static final boolean DOWNLOAD_SHAREHOLDER_RESERACH = false;// Download shareholder research
    private static final boolean MERGE_MATCH_FILES = false;// Merge and create match files each year
    private static final boolean DOWNLOAD_BJOP_FILES = false;// Download bjop files
    private static final boolean MERGE_BJOP_FILES = false;// Merge and create bjop files each year
    private static final boolean INIT_MATCH_DATA = false; // Reinit table data by parsing merged match file
    private static final boolean INIT_BJOP_DATA = false;// Reinit table data by parsing merged bjop file
    private static final boolean INIT_MATCHSUM = false;//
    private static final boolean DO_ALL = true;// All step

    private static SussDao dao;

    public static void main(String[] args) {
        init();
        dao = AppContext.getDao();

        if (INIT_DIR) {
            initDirs();
        }
        if (INIT_STOCK) {
            initStock("shanghai.ebk");
            initStock("shenzhen.ebk");
        }
        if (DOWNLOAD_SHAREHOLDER_RESERACH) {
            downloadShareholderReserach();
        }
    }

    private static void downloadShareholderReserach() {
        // SH

        // SZ
    }

    private static void initStock(String fileName) {
        LOGGER.info("Initialize stock code.");
        String shFilePath = SfsUtil.getStockCodeFilePath(fileName);
        List<StockPo> list = new ArrayList<>();
        try (FileLineIterator iter = new FileLineIterator(shFilePath)) {
            String line = null;
            while ((line = iter.nextLine()) != null) {
                if (line.trim().isEmpty()) {
                    LOGGER.debug("Ignore line {}", line);
                    continue;
                }
                int code = Integer.parseInt(line.trim().substring(1, line.trim().length()));
                StockPo po = new StockPo();
                po.setCode(code);
            }
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            e.printStackTrace();
        }
        Collections.sort(list);

        List<TableAddRowItf> stockList = new ArrayList<>();
        for (StockPo po : list) {
            stockList.add(po);
        }

        LOGGER.info("Add leagueList size {} ", stockList.size());
        dao.addRowList(stockList);
    }

    private static void initDirs() {
        LOGGER.info("Initialize directories.");
    }

    private static void init() {
        AppContext.init();
    }
}
