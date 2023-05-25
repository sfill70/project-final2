package com.javarush.jira.bugtracking;

import com.javarush.jira.common.util.JsonUtil;

public class TaskTestData {
    public static final String USER_MAIL = "admin@gmail.com";
    public static final String SUMMARY_URL = "/summary/4";
    public static final String EMPTY_SUMMARY_URL = "/summary/4";

    public static <T> String jsonWithPassword(T user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}
