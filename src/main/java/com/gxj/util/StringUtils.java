package com.gxj.util;

import java.util.regex.Pattern;

/**
 * @version 1.0
 * @description:
 * @author: guanxingjiang
 * @time: 2018-09-20 19:29
 */
public class StringUtils {

    public StringUtils() {
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static  String delete(String str,int length){
        int count = 0;
        int offset = 0;
        char[] c = str.toCharArray();
        int size = getLength(str);
        if(size >= length){
            for (int i = 0; i < c.length; i++) {
                if (c[i] > 256) {
                    offset = 2;
                    count += 2;
                } else {
                    offset = 1;
                    count++;
                }
                if (count == length) {
                    return str.substring(0, i + 1);
                }
                if ((count == length + 1 && offset == 2)) {
                    return str.substring(0, i);
                }
            }
        }else{
            return str;
        }
        return "";
    }

    /**
     * 获取字符串字节长度
     * @param str
     * @return
     */
    public static int getLength(String str){
        if(isBlank(str)){
            return 0;
        }
        char[] c = str.toCharArray();
        int size = c.length;
        int length = 0;
        for(int i=0;i<size;i++){
            if (c[i] > 256) {
                length+=2;
            }else {
                length+=1;
            }
        }
        return length;
    }
    /**
     * 判断是否数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
