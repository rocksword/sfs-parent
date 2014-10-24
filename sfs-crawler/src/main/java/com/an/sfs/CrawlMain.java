package com.an.sfs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.sfs.po.ShareholderPo;
import com.an.sfs.po.StockPo;

public class CrawlMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlMain.class);
    private static final boolean INIT_DIR = false;
    private static final boolean INIT_STOCK = false;
    private static final boolean DOWNLOAD_SHAREHOLDER = true;// Download shareholder
    private static final boolean PARSE_SHAREHOLDER = false;// Parse shareholder
    private static final boolean DO_ALL = false;// All step

    private static SussDao dao;

    public static void main(String[] args) {
        init();
        dao = AppContext.getDao();
        List<StockPo> stockList = dao.getStock();
        if (DOWNLOAD_SHAREHOLDER || DO_ALL) {
            downloadShareholder(stockList);
        }
        if (PARSE_SHAREHOLDER || DO_ALL) {
            parseShareholder(stockList);
        }
        if (INIT_DIR) {
            initDirs();
        }
        if (INIT_STOCK) {
            dao.clearTable(StockPo.TABLE_NAME);
            initStock("shanghai.ebk");
            initStock("shenzhen.ebk");
        }
    }

    private static void parseShareholder(List<StockPo> stockList) {
        for (StockPo stock : stockList) {
            String code = stock.getCode();

            String shareholderFilePath = SfsUtil.getShareholderFilePath(code + ".txt");
            File f = new File(shareholderFilePath);
            if (!f.exists()) {
                LOGGER.debug("Not found {}.", shareholderFilePath);
                continue;
            }

            // Clear table shareholder
            dao.clearTable(ShareholderPo.TABLE_NAME, code);

            // Add new data
            List<String> timeList = new ArrayList<>();
            List<Float> holderNumList = new ArrayList<>();
            List<Float> stockNumList = new ArrayList<>();
            List<Float> priceList = new ArrayList<>();
            List<Float> moneyList = new ArrayList<>();
            List<Float> top10List = new ArrayList<>();
            try (FileLineIterator iter = new FileLineIterator(shareholderFilePath)) {
                String line = null;
                boolean begin = false;
                while ((line = iter.nextLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    if (begin) {
                        if (line.startsWith(FLAG_TIME)) {
                            Matcher m = PATTERN_TIME.matcher(line);
                            while (m.find()) {
                                timeList.add(m.group());
                            }
                        } else if (line.contains(FLAG_HOLDERNUM)) {
                            Matcher m = PATTERN_HOLDERNUM.matcher(line);
                            while (m.find()) {
                                holderNumList.add(Float.parseFloat(m.group()));
                            }
                        } else if (line.contains(FLAG_STOCKNUM)) {
                            Matcher m = PATTERN_STOCKNUM.matcher(line);
                            while (m.find()) {
                                stockNumList.add(Float.parseFloat(m.group()));
                            }
                        } else if (line.contains(FLAG_PRICE)) {
                            Matcher m = PATTERN_PRICE.matcher(line);
                            while (m.find()) {
                                priceList.add(Float.parseFloat(m.group()));
                            }
                        } else if (line.contains(FLAG_MONEY)) {
                            Matcher m = PATTERN_MONEY.matcher(line);
                            while (m.find()) {
                                moneyList.add(Float.parseFloat(m.group()));
                            }
                        } else if (line.contains(FLAG_TOP10)) {
                            Matcher m = PATTERN_TOP10.matcher(line);
                            while (m.find()) {
                                top10List.add(Float.parseFloat(m.group()));
                            }
                        }
                    } else {
                        if (line.startsWith(FLAG_SHAREHOLDER)) {
                            begin = true;
                        } else if (line.trim().contains(FLAG_ENDSHAREHOLDER)) {
                            begin = false;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error: ", e);
                e.printStackTrace();
            }

            int cnt = timeList.size();
            if (cnt == 0 || cnt != holderNumList.size() || cnt != stockNumList.size() || cnt != priceList.size()
                    || cnt != moneyList.size() || cnt != top10List.size()) {
                LOGGER.error("Invalid shareholder data {}", code);
                return;
            }

            List<TableAddRowItf> shareholderList = new ArrayList<>();
            for (int i = 0; i < cnt; i++) {
                ShareholderPo po = new ShareholderPo();
                po.setCode(code);
                po.setTime(timeList.get(i));
                po.setHolderNum(holderNumList.get(i));
                po.setStockNum(stockNumList.get(i));
                po.setPrice(priceList.get(i));
                po.setMoney(moneyList.get(i));
                po.setTop10(top10List.get(i));
                shareholderList.add(po);
            }

            Collections.reverse(shareholderList);
            LOGGER.info("Add shareholderList size {} ", shareholderList.size());
            dao.addRowList(shareholderList);
        }
    }

    private static void downloadShareholder(List<StockPo> stockList) {
        for (StockPo po : stockList) {
            String code = po.getCode();
            String shareholderFilePath = SfsUtil.getShareholderFilePath(code + ".txt");
            File f = new File(shareholderFilePath);
            if (f.exists()) {
                LOGGER.debug("Found existing {}.", shareholderFilePath);
            } else {
                if (code.startsWith("6")) {
                    code = "sh" + code;
                } else {
                    code = "sz" + code;
                }
                String urlStr = SfsUtil.getShareholderUrl(code);
                String content = new WebPageReader().read(urlStr);
                if (!content.isEmpty()) {
                    FileUtil.writeFile(shareholderFilePath, content);
                }
            }
        }
    }

    private static void initStock(String fileName) {
        LOGGER.info("Initialize stock code.");
        String shFilePath = SfsUtil.getStockCodeFilePath(fileName);
        List<StockPo> list = new ArrayList<>();
        try (FileLineIterator iter = new FileLineIterator(shFilePath)) {
            String line = null;
            while ((line = iter.nextLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String code = line.trim().substring(1, line.trim().length());
                    if (!code.startsWith("300")) {
                        StockPo po = new StockPo();
                        po.setCode(code);
                        list.add(po);
                    }
                }
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

        LOGGER.info("Add stockList size {} ", stockList.size());
        dao.addRowList(stockList);
    }

    private static void initDirs() {
        LOGGER.info("Initialize directories.");
        String dirPath = SfsUtil.getDirPathInData("shareholder");
        FileUtil.createDir(dirPath);
    }

    private static void init() {
        AppContext.init();
    }

    // <title>利物浦 VS 斯托克城_欧赔对比-中国足彩网</title>
    private static final String FLAG_SHAREHOLDER = "<table id=\"Table0\">";
    private static final String FLAG_ENDSHAREHOLDER = "<strong>十大流通股东</strong>";
    private static final String FLAG_TIME = "<tr><th class=\"tips-colnameL\"></th>";
    private static final Pattern PATTERN_TIME = Pattern.compile("\\d+-\\d+-\\d+");
    private static final String FLAG_HOLDERNUM = "股东人数(户)";
    private static final Pattern PATTERN_HOLDERNUM = Pattern.compile("\\d+.\\d+");
    private static final String FLAG_STOCKNUM = "人均流通股(股)";
    private static final Pattern PATTERN_STOCKNUM = Pattern.compile("\\d+.\\d+");
    private static final String FLAG_PRICE = "股价(元)";
    private static final Pattern PATTERN_PRICE = Pattern.compile("\\d+.\\d+");
    private static final String FLAG_MONEY = "人均持股金额(元)";
    private static final Pattern PATTERN_MONEY = Pattern.compile("\\d+.\\d+");
    private static final String FLAG_TOP10 = "前十大流通股东持股合计(%)";
    private static final Pattern PATTERN_TOP10 = Pattern.compile("\\d+.\\d+");
}