package com.sjy.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtil {
	/**  
     * 分转换为元.  
     *   
     * @param fen  
     *            分  
     * @return 元  
     */  
    public static String fromFenToYuan(final String fen) {  
        String yuan = "";  
        final int MULTIPLIER = 100;  
        Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");  
        Matcher matcher = pattern.matcher(fen);  
        if (matcher.matches()) {  
            yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();  
        } else {  
            System.out.println("参数格式不正确!");  
        }  
        return yuan;  
    }  
}
