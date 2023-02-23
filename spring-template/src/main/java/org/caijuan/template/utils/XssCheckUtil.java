package org.caijuan.template.utils;

import java.util.regex.Pattern;

public class XssCheckUtil {

    /**
     * 定义script的正则表达式
     */
    private static final String REG_SCRIPT = "<script[^>]*?>[\\s\\S]*?</script>";

    /**
     * 定义style的正则表达式
     */
    private static final String REG_STYLE = "<style[^>]*?>[\\s\\S]*?</style>";

    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REG_HTML = "<[^>]+>";

    /**
     * 定义所有w标签
     */
    private static final String REG_W = "<w[^>]*?>[\\s\\S]*?</w[^>]*?>";

    // 包含javascript 将会被过滤
    private static final String REG_JAVASCRIPT = ".*javascript.*";
    // pattern
    private static final Pattern pw = Pattern.compile(REG_W, Pattern.CASE_INSENSITIVE);

    private static final Pattern script = Pattern.compile(REG_SCRIPT, Pattern.CASE_INSENSITIVE);


    private static final Pattern style = Pattern.compile(REG_STYLE, Pattern.CASE_INSENSITIVE);


    private static final Pattern htmlTag = Pattern.compile(REG_HTML, Pattern.CASE_INSENSITIVE);


    private static final Pattern javascript = Pattern.compile(REG_JAVASCRIPT, Pattern.CASE_INSENSITIVE);


    /**
     * [xssClean 过滤特殊、敏感字符]
     *
     * @param value [请求参数]
     * @return [value]
     */
    public static boolean check(String value) {
        if (value == null || "".equals(value)) {
            return false;
        }
        return pw.matcher(value).find() || script.matcher(value).find() || style.matcher(value).find() || htmlTag.matcher(value).find() || javascript.matcher(value).find();
    }

}