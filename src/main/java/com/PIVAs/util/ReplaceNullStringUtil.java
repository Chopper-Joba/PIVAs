package com.PIVAs.util;

public class ReplaceNullStringUtil {
    public static String rReplaceNullString(String value){
        if (value==null||value.equals("null")){
            return "";
        }else {
            return value;
        }
    }
}
