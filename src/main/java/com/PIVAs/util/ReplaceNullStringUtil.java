package com.PIVAs.util;

public class ReplaceNullStringUtil {
    public static String replaceNullString(String value){
        if (value==null||"null".equals(value)||"".equals(value)){
            return "";
        }else {
            return value;
        }
    }
}
