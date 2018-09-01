package uk.dioxic.mgenerate.common;

public class StringUtil {

    public static String lowerCaseFirstLetter(String s) {
        char[] chars = s.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }
}
