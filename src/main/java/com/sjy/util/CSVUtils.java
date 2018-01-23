package com.sjy.util;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import lombok.extern.slf4j.Slf4j;  
  
/** 
 *  操作CSV文件操作 
 * @author ZHANG.TX
 *
 */
@Slf4j
public class CSVUtils {  
  
    /** 
     * 生成为CVS文件  
     * @param exportData 
     *              源数据List 
     * @param map 
     *              csv文件的列表头map 
     * @param outPutPath 
     *              文件路径 
     * @param fileName 
     *              文件名称 
     * @return 
     */  
    @SuppressWarnings("rawtypes")  
    public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath,String fileName) {  
        File csvFile = null;  
        BufferedWriter csvFileOutputStream = null;  
        try {  
            File file = new File(outPutPath);  
            if (!file.exists()) {  
                file.mkdir();  
            }  
            //定义文件名格式并创建  
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));  
            log.debug("csvFile：" + csvFile);  
            // UTF-8使正确读取分隔符","    
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(  
                csvFile), "GBK"), 1024);  
            log.debug("csvFileOutputStream：" + csvFileOutputStream);  
            // 写入文件头部    
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext();) {  
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();  
                csvFileOutputStream.write((String) propertyEntry.getValue() != null ? new String(  
                    ((String) propertyEntry.getValue()).getBytes("GBK"), "GBK") : "");  
                if (propertyIterator.hasNext()) {  
                    csvFileOutputStream.write(",");  
                }  
                log.debug(new String(((String) propertyEntry.getValue()).getBytes("GBK"),  
                    "GBK"));  
            }  
            csvFileOutputStream.write("\r\n");  
            // 写入文件内容    
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {  
                Object row = (Object) iterator.next();  
                for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator  
                    .hasNext();) {  
                    java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator  
                        .next();  
                    csvFileOutputStream.write((String) BeanUtils.getProperty(row,  
                        ((String) propertyEntry.getKey()) != null? (String) propertyEntry.getKey()  
                            : ""));  
                    if (propertyIterator.hasNext()) {  
                        csvFileOutputStream.write(",");  
                    }  
                }  
                if (iterator.hasNext()) {  
                    csvFileOutputStream.write("\r\n");  
                }  
            }  
            csvFileOutputStream.flush();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                csvFileOutputStream.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return csvFile;  
    }  
  
    /** 
     * 下载文件 
     * @param response 
     * @param csvFilePath 
     *              文件路径 
     * @param fileName 
     *              文件名称 
     * @throws IOException 
     */  
    public static void exportFile(HttpServletResponse response, String csvFilePath, String fileName) throws IOException {  
        response.setContentType("application/csv;charset=GBK");  
        response.setHeader("Content-Disposition",  
            "attachment;  filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));  
        //URLEncoder.encode(fileName, "GBK")  
  
        InputStream in = null;  
        try {  
            in = new FileInputStream(csvFilePath);  
            int len = 0;  
            byte[] buffer = new byte[1024];  
            response.setCharacterEncoding("GBK");  
            OutputStream out = response.getOutputStream();  
            while ((len = in.read(buffer)) > 0) {  
                //out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });  
                out.write(buffer, 0, len);  
            }  
        } catch (FileNotFoundException e) {  
            log.error(e.getMessage());  
        } finally {  
            if (in != null) {  
                try {  
                    in.close();  
                } catch (Exception e) {  
                    throw new RuntimeException(e);  
                }  
            }  
        }  
    }  
  
    /** 
     * 删除该目录filePath下的所有文件 
     * @param filePath 
     *            文件目录路径 
     */  
    public static void deleteFiles(String filePath) {  
        File file = new File(filePath);  
        if (file.exists()) {  
            File[] files = file.listFiles();  
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isFile()) {  
                    files[i].delete();  
                }  
            }  
        }  
    }  
  
    /** 
     * 删除单个文件 
     * @param filePath 
     *         文件目录路径 
     * @param fileName 
     *         文件名称 
     */  
    public static void deleteFile(String filePath, String fileName) {  
        File file = new File(filePath);  
        if (file.exists()) {  
            File[] files = file.listFiles();  
            for (int i = 0; i < files.length; i++) {  
                if (files[i].isFile()) {  
                    if (files[i].getName().equals(fileName)) {  
                        files[i].delete();  
                        return;  
                    }  
                }  
            }  
        }  
    }  
  
    /** 
     * 测试数据 
     * @param args 
     */  
    @SuppressWarnings({ "rawtypes", "unchecked" })  
    public static void main(String[] args) {  
        List exportData = new ArrayList<Map>();  
        Map row1 = new LinkedHashMap<String, String>();  
        row1.put("1", "11");  
        row1.put("2", "12");  
        row1.put("3", "13");  
        row1.put("4", "14");  
        exportData.add(row1);  
        row1 = new LinkedHashMap<String, String>();  
        row1.put("1", "21");  
        row1.put("2", "22");  
        row1.put("3", "23");  
        row1.put("4", "24");  
        exportData.add(row1);  
        LinkedHashMap map = new LinkedHashMap();  
        map.put("1", "第一列");  
        map.put("2", "第二列");  
        map.put("3", "第三列");  
        map.put("4", "第四列");  
  
        String path = "d:/";  
        String fileName = "文件导出";  
        File file = CSVUtils.createCSVFile(exportData, map, path, fileName);  
        String fileName2 = file.getName();  
        log.debug("文件名称：" + fileName2);  
        
//    	 String fileName = "文件导出";  
//    	 String filePath = "d:/";  
//    	List refundList = new ArrayList(); //交易数据
//        String name = "工商银行(ICBC)退款数据";  
//        List exportData = new ArrayList();  
//        LinkedHashMap datamMap = null;  
//        for (Iterator iterator = refundList.iterator(); iterator.hasNext();) {  
//              HashMap map = (HashMap) iterator.next();  
//              datamMap = new LinkedHashMap();  
//              datamMap.put("1", map.get("merOrderId"));  
//              datamMap.put("2",DateUtils.format(new Date(), DateUtils.DATE_FORMAT));  
//              BigDecimal amount = (BigDecimal) map.get("amount");  
//              String amountString = amount.divide(new BigDecimal(10)).toPlainString();  
//              datamMap.put("3", amountString);  
//              datamMap.put("4", map.get("remark") != null ? map.get("remark") : "");  
//              exportData.add(datamMap);  
//        }  
//         LinkedHashMap map = new LinkedHashMap();  
//         map.put("1", "订单号");  
//         map.put("2", "支付日期");  
//         map.put("3", "退货现金金额（整数金额 单位：分）");  
//         map.put("4", "退货原因");  
//         File file = CSVUtils.createCSVFile(exportData, map, filePath, name);//生成CSV文件  
//         fileName = file.getName();  
//         CSVUtils.exportFile(response,filePath + fileName, fileName);//下载生成的CSV文件  
    }  
}  
