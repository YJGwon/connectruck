package com.connectruck.foodtruck.common.constant;

public class FormatText {

    public static final String TIME = "HH:mm";
    public static final String PASSWORD = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$";
    public static final String PASSWORD_DESCRIPTION = "영대소문자, 숫자, 특수문자를 각 1개 이상 포함한 8-30자";
    public static final String PHONE = "^01(?:0|1|[6-9])(\\d{3}|\\d{4})(\\d{4})$";
}
