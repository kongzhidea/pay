package com.kk.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 仅仅打印到控制台的 log4j
 */
public class ConsoleLogger {

    private static final String formatDefaultTimestamp = "yyyy-MM-dd HH:mm:ss.SSS";

    public void info(Object message) {
        StringBuilder sb = log("INFO", message);
        System.out.println(sb.toString());
    }

    public void error(Object message) {
        StringBuilder sb = log("ERROR", message);
        System.out.println(sb.toString());
    }

    public void error(Object message, Throwable t) {

        StringBuilder sb = log("ERROR", message);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);

        System.out.println(sb.toString());
        System.out.println(sw.toString());
    }

    private StringBuilder log(String level, Object message) {
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat(formatDefaultTimestamp);
        String time = format.format(new Date());
        sb.append(time);

        sb.append(" ");
        sb.append(level);
        sb.append(" ");

        sb.append(getFileNameAndLineNumber());

        sb.append(" - ");

        sb.append(message.toString());
        return sb;
    }

    private String getFileNameAndLineNumber() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        new Throwable().printStackTrace(pw);

        String clazName = this.getClass().getName();
        String[] lines = sw.toString().split("\n");

        int idx = -1;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains(clazName)) {
                idx = i;
            }
        }
        // info 样例： at com.kk.log4j.Log4jTest.main(Log4jTest.java:30)
        String info = lines[idx + 1];
        Pattern pattern = Pattern.compile("\\((.+)\\.java:(\\d+)\\)");
        Matcher matcher = pattern.matcher(info);
        if (matcher.find()) {
            String fileName = matcher.group(1);
            String lineNumber = matcher.group(2);
            return genString(fileName, ":", lineNumber);
        }
        return "";
    }

    private String genString(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb.toString();
    }
}