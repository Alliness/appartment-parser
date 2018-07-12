package alliness.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static String replaceIntFromString(String target) {
        return target.replaceAll("\\d+", "");
    }

    /**
     * return string with erased values
     *
     * @param target {@link String}
     * @param values {@link String[]} - wanted to erase values
     * @return string w/o values
     */
    public static String replaceFromString(String target, String... values) {

        StringBuilder pattern = new StringBuilder();
        pattern.append("(");
        for (String value : values) {
            pattern.append(value)
                   .append("|");
        }
        pattern.deleteCharAt(pattern.length() - 1);
        pattern.append(")");
        return target.replaceAll(pattern.toString(), "");
    }

    /**
     * is string contains some of values
     *
     * @param target {@link String}
     * @param values searched values
     * @return result
     */
    public static boolean containsValues(String target, String... values) {
        //        System.out.println(str.matches("(.*)(dev|demo)(.*)"));
        StringBuilder pattern = new StringBuilder();
        pattern.append("(.*)(");
        for (String value : values) {
            pattern.append(value)
                   .append("|");
        }
        pattern.deleteCharAt(pattern.length() - 1);
        pattern.append(")(.*)");
        return target.matches(pattern.toString());
    }

    /**
     * is string contains some of values
     *
     * @param target {@link String}
     * @param values searched values
     * @return result
     */
    public static List<String> getContainsValues(String target, String... values) {
        //        System.out.println(str.matches("(.*)(dev|demo)(.*)"));
        StringBuilder pattern = new StringBuilder();
        pattern.append("(.*)(");
        for (String value : values) {
            pattern.append(value)
                   .append("|");
        }
        pattern.deleteCharAt(pattern.length() - 1);
        pattern.append(")(.*)");
        Pattern      p       = Pattern.compile(pattern.toString());
        Matcher      m       = p.matcher(target);
        List<String> results = new ArrayList<>();
        while (m.find()) {
            results.add(m.group(0));
        }
        return results;
    }
}
