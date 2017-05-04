package com.cmos.pinyin;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Pattern;

/**
 * 拼音工具类
 *
 * @author lsf
 */
public class PinYinUtil {
    private static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

    static {
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public static String getPinYin(String inputString) {
        char[] input = inputString.trim().toCharArray();
        String output = "";

        try {
            for (int i = 0; i < input.length; i++) {
                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    output += temp[0];
                } else
                    output += Character.toString(input[i]);
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output;
    }

    public static String match(String src, String pinyin) {
        if (!Pattern.compile("^[a-zA-Z]+$").matcher(pinyin).matches()) return null; //确保要匹配的string是拼音
        pinyin = pinyin.toUpperCase();
        if (src.contains(pinyin)) return pinyin; //src中是拼音或有拼音，且包含要匹配的拼音，直接返回
        char[] chars = src.toCharArray(); // 拆成单字，用split会有问题，包含空串""
        String[][] pinyinArray = new String[chars.length][];
        int length = 1;
        for (int i = 0; i < chars.length; i++) { //逐字转拼音，得到一个二维数组，兼容多单字
            pinyinArray[i] = singlePinyin(chars[i]);
            length *= pinyinArray[i].length;
        }
        String[][] groupArray = new String[length][chars.length];
        int cursor[] = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            cursor[i] = 0;
        }
        for (int z = 0; z < length; z++) { // 将上面的二维数组转成新的二维数组：所有的排列组合
            for (int x = 0; x < chars.length; x++) {
                groupArray[z][x] = pinyinArray[x][cursor[x]];
            }
            if (z < length - 1) cursor = nextCursor(cursor, pinyinArray, 0);
        }
        for (int x = 0; x < length; x++) {
            StringBuilder firstSpell = new StringBuilder();
            for (int i = 0; i < chars.length; i++) {
                firstSpell.append(groupArray[x][i].charAt(0));
                for (int j = i; j < chars.length; j++) {
                    StringBuilder result = new StringBuilder();
                    for (int k = i; k < j + 1; k++) {
                        result.append(groupArray[x][k]);
                    }
                    if (result.toString().startsWith(pinyin)) {
                        return src.substring(i, j + 1);
                    }
                }
            }
            if (firstSpell.toString().contains(pinyin)) {
                int index = firstSpell.indexOf(pinyin);
                return src.substring(index, index + pinyin.length());
            }
        }
        return null;
    }

    private static int[] nextCursor(int[] cursor, String[][] consult, int x) {
        if (consult[x].length == 1) {
            return nextCursor(cursor, consult, x + 1);
        } else {
            cursor[x] = cursor[x] + 1;
            if (cursor[x] == consult[x].length) {
                cursor[x] = 0;
                x = x + 1;
                if (x == consult.length) x = 0;
                return nextCursor(cursor, consult, x);
            } else {
                return cursor;
            }
        }
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuilder builder = new StringBuilder();
        char[] arr = chinese.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], format);
                    if (temp != null) {
                        builder.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                builder.append(arr[i]);
            }
        }
        return builder.toString().replaceAll("\\W", "").trim().toUpperCase();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        StringBuilder buffer = new StringBuilder();
        char[] arr = chinese.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    buffer.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], format)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                buffer.append(arr[i]);
            }
        }
        return buffer.toString();
    }

    public static String[] singlePinyin(char chinese) {
        if (chinese > 128) {
            try {
                return PinyinHelper.toHanyuPinyinStringArray(chinese, format);
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return new String[]{String.valueOf(chinese)};
    }
}