package uk.dioxic.mgenerate.apt.util;

public class StringUtil {

    public static String lowerCaseFirstLetter(String s) {
        char[] chars = s.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }
}
