package com.example.commonlibs.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Horrarndoo on 2017/4/5.
 * 字符串工具类
 */
public class StringUtils {
    /**
     * 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false
     */
    public static boolean isEmpty(String value) {
        return !(value != null && !"".equalsIgnoreCase(value.trim())
                && !"null".equalsIgnoreCase(value.trim()));
    }

    /**
     * 判断字符串是否是邮箱
     *
     * @param email email
     * @return 字符串是否是邮箱
     */
    public static boolean isEmail(String email) {
//        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(" +
//                "([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        String str = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断手机号字符串是否合法
     *
     * @param phoneNumber 手机号字符串
     * @return 手机号字符串是否合法
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        String expression = "^1[3|4|5|7|8]\\d{9}$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断手机号字符串是否合法
     *
     * @param areaCode    区号
     * @param phoneNumber 手机号字符串
     * @return 手机号字符串是否合法
     */
    public static boolean isPhoneNumberValid(String areaCode, String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }

        if (phoneNumber.length() < 5) {
            return false;
        }

        if ("+86".equals(areaCode) || "86".equals(areaCode)) {
            return isPhoneNumberValid(phoneNumber);
        }

        boolean isValid = false;
        String expression = "^[0-9]*$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断字符串是否是手机号格式
     *
     * @param areaCode    区号
     * @param phoneNumber 手机号字符串
     * @return 字符串是否是手机号格式
     */
    public static boolean isPhoneFormat(String areaCode, String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }

        if (phoneNumber.length() < 7) {
            return false;
        }

        boolean isValid = false;
        String expression = "^[0-9]*$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * 判断字符串是否为纯数字
     *
     * @param str 字符串
     * @return 是否纯数字
     */
    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 正则：手机号（简单）, 1字头＋10位数字即可.
     */
    private static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    private static final Pattern PATTERN_REGEX_MOBILE_SIMPLE = Pattern.compile(REGEX_MOBILE_SIMPLE);

    /**
     * 正则：手机号（精确）, 已知3位前缀＋8位数字
     * <p>
     * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
     * </p>
     * <p>
     * 联通：130、131、132、145、155、156、175、176、185、186
     * </p>
     * <p>
     * 电信：133、153、173、177、180、181、189
     * </p>
     * <p>
     * 全球星：1349
     * </p>
     * <p>
     * 虚拟运营商：170
     * </p>
     */
    private static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    private static final Pattern PATTERN_REGEX_MOBILE_EXACT = Pattern.compile(REGEX_MOBILE_EXACT);

    /**
     * 正则：固定电话号码,可带区号,然后至少6,8位数字
     */
    private static final String REGEX_TEL = "^(\\d{3,4}-)?\\d{6,8}$";
    private static final Pattern PATTERN_REGEX_TEL = Pattern.compile(REGEX_TEL);

    /**
     * 正则：身份证号码15位, 数字且关于生日的部分必须正确
     */
    private static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    private static final Pattern PATTERN_REGEX_ID_CARD15 = Pattern.compile(REGEX_ID_CARD15);

    /**
     * 正则：身份证号码18位, 数字且关于生日的部分必须正确
     */
    private static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    private static final Pattern PATTERN_REGEX_ID_CARD18 = Pattern.compile(REGEX_ID_CARD18);

    /**
     * 正则：邮箱, 有效字符(不支持中文), 且中间必须有@,后半部分必须有.
     */
    private static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    private static final Pattern PATTERN_REGEX_EMAIL = Pattern.compile(REGEX_EMAIL);

    /**
     * 正则：URL, 必须有"://",前面必须是英文,后面不能有空格
     */
    private static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    private static final Pattern PATTERN_REGEX_URL = Pattern.compile(REGEX_URL);

    /**
     * 正则：yyyy-MM-dd格式的日期校验,已考虑平闰年
     */
    private static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    private static final Pattern PATTERN_REGEX_DATE = Pattern.compile(REGEX_DATE);

    /**
     * 正则：IP地址
     */
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final Pattern PATTERN_REGEX_IP = Pattern.compile(REGEX_IP);


    /**
     * 正则：车牌号
     */
    private static final String REGEX_CAR = "^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{3,4}[A-Z0-9挂学警港澳]{1}$";
    private static final Pattern PATTERN_REGEX_CAR = Pattern.compile(REGEX_CAR);


    /**
     * 正则：人名
     */
    private static final String REGEX_NAME = "^([\\u4e00-\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$";
    private static final Pattern PATTERN_REGEX_NAME = Pattern.compile(REGEX_NAME);

    /**
     * 正则：mac地址
     */
    private static final String REGEX_MAC = "([A-Fa-f0-9]{2}-){5}[A-Fa-f0-9]{2}";
    private static final Pattern PATTERN_REGEX_MAC = Pattern.compile(REGEX_MAC);


    //封装方法：

    /**
     * 验证手机号（简单）
     */
    public static boolean isMobileSimple(String str) {
        return isMatch(PATTERN_REGEX_MOBILE_SIMPLE, str);
    }

    /**
     * 验证手机号（精确）
     */
    public static boolean isMobileExact(String str) {
        return isMatch(PATTERN_REGEX_MOBILE_EXACT, str);
    }

    /**
     * 验证固定电话号码
     */
    public static boolean isTel(String str) {
        return isMatch(PATTERN_REGEX_TEL, str);
    }

    /**
     * 验证15或18位身份证号码
     */
    public static boolean isIdCard(String str) {
        return isMatch(PATTERN_REGEX_ID_CARD15, str) || isMatch(PATTERN_REGEX_ID_CARD18, str);
    }


    /**
     * 验证URL
     */
    public static boolean isUrl(String str) {
        return isMatch(PATTERN_REGEX_URL, str);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验,已考虑平闰年
     */
    public static boolean isDate(String str) {
        return isMatch(PATTERN_REGEX_DATE, str);
    }

    /**
     * 验证IP地址
     */
    public static boolean isIp(String str) {
        return isMatch(PATTERN_REGEX_IP, str);
    }

    /**
     * 验证车牌号
     */
    public static boolean isCar(String str) {
        return isMatch(PATTERN_REGEX_CAR, str);
    }

    /**
     * 验证人名
     */
    public static boolean isName(String str) {
        return isMatch(PATTERN_REGEX_NAME, str);
    }

    /**
     * 验证mac
     */
    public static boolean isMac(String str) {
        return isMatch(PATTERN_REGEX_MAC, str);
    }

    public static boolean isMatch(Pattern pattern, String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        return pattern.matcher(str).matches();
    }

    /**
     * 身份证验证
     *
     * @param idStr
     * @return
     */
    public static String identityCardVerification(String idStr) {
        String[] wf = {"1", "0", "x", "9", "8", "7", "6", "5", "4", "3", "2"};
        String[] checkCode = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2"};
        String iDCardNo = "";
        try {
            //判断号码的长度 15位或18位
            if (idStr.length() != 15 && idStr.length() != 18) {
                return "身份证号码长度应该为15位或18位";
            }
            if (idStr.length() == 18) {
                iDCardNo = idStr.substring(0, 17);
            } else {
                idStr.length();
                iDCardNo = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
            }
            if (!isStrNum(iDCardNo)) {
                return "身份证15位号码都应为数字;18位号码除最后一位外,都应为数字";
            }
            //判断出生年月
            String strYear = iDCardNo.substring(6, 10);// 年份
            String strMonth = iDCardNo.substring(10, 12);// 月份
            String strDay = iDCardNo.substring(12, 14);// 月份
            if (isStrDate(strYear + "-" + strMonth + "-" + strDay) == false) {
                return "身份证生日无效";
            }
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return "身份证生日不在有效范围";
            }
            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                return "身份证月份无效";
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                return "身份证日期无效";
            }
            //判断地区码
            Hashtable h = GetAreaCode();
            if (h.get(iDCardNo.substring(0, 2)) == null) {
                return "身份证地区编码错误";
            }
            //判断最后一位
            int theLastOne = 0;
            for (int i = 0; i < 17; i++) {
                theLastOne = theLastOne + Integer.parseInt(String.valueOf(iDCardNo.charAt(i))) * Integer.parseInt(checkCode[i]);
            }
            int modValue = theLastOne % 11;
            String strVerifyCode = wf[modValue];
            iDCardNo = iDCardNo + strVerifyCode;
            if (idStr.length() == 18 && !iDCardNo.equals(idStr)) {
                return "身份证无效，不是合法的身份证号码";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {
        boolean cardVerification = checkIdCard("61042419910506288X");
        System.out.println("verify   " + cardVerification);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码18位，第一位不能为0，最后一位可能是数字或字母，中间16位为数字 \d同[0-9]
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    private static boolean isStrNum(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (isNum.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否为日期格式
     *
     * @param strDate
     * @return
     */
    public static boolean isStrDate(String strDate) {
        Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 地区代码
     *
     * @return Hashtable
     */
    private static Hashtable GetAreaCode() {
        Hashtable hashtable = new Hashtable();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }

    /**
     * 位数不够左补0
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addZeroForString(String str, int strLength) {

        int strLen = str.length();
        if (strLen == strLength) {
            return str;
        }
        StringBuffer sb = new StringBuffer();
        while (strLen < strLength) {
            sb.append("0");
            strLen += 1;
        }
        sb.append(str);

        return sb.toString();
    }

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        String result = String.valueOf(strLen);
        int length = result.length();
        if (length < strLength) {
            while (length < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(result);//左补0
//			    	sb.append(str).append("0");//右补0
                result = sb.toString();
                length = result.length();
            }
        }
        return result;
    }


    /**
     * 数组结合
     *
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    /**
     * 将int转换为固定长度的String（前补0，或者后补0）
     *
     * @return String
     */
    public static String addZeroForNum(long num, int strLength) {
        String str = String.valueOf(num);
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);//左补0
//		    	sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }

    /**
     * 位数不够右补空格
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addSpaceForStringRight(String str, int strLength) {

        int strLen = str.length();
        if (strLen == strLength) {
            return str;
        }

        return String.format("%-" + strLength + "s", str);
    }


    /**
     * 位数不够左补空格
     *
     * @param str
     * @param strLength
     * @return
     */
    public static String addSpaceForStringLeft(String str, int strLength) {

        int strLen = str.length();
        if (strLen == strLength) {
            return str;
        }

        return String.format("%" + strLength + "s", str);
    }


    /**
     * 按传的参数补固定长度的空格
     *
     * @param num
     * @return
     */
    public static String setSpace(int num) {
        StringBuffer sb = new StringBuffer();
        while (true) {
            if (sb.length() == num) {
                break;
            }
            sb.append(" ");
        }
        return sb.toString();
    }
}
