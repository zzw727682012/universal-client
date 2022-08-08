package com.code;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Code1 {
    /**
     * 给你两个整数，n 和 start 。
     * <p>
     * 数组 nums 定义为：nums[i] = start + 2*i（下标从 0 开始）且 n == nums.length 。
     * <p>
     * 请返回 nums 中所有元素按位异或（XOR）后得到的结果。
     * <p>
     * 示例 1：
     * <p>
     * 输入：n = 5, start = 0
     * 输出：8
     * 解释：数组 nums 为 [0, 2, 4, 6, 8]，其中 (0 ^ 2 ^ 4 ^ 6 ^ 8) = 8 。
     * "^" 为按位异或 XOR 运算符。
     * 示例 2：
     * <p>
     * 输入：n = 4, start = 3
     * 输出：8
     * 解释：数组 nums 为 [3, 5, 7, 9]，其中 (3 ^ 5 ^ 7 ^ 9) = 8.
     * 示例 3：
     * <p>
     * 输入：n = 1, start = 7
     * 输出：7
     * 示例 4：
     * <p>
     * 输入：n = 10, start = 5
     * 输出：2
     */

    public static int algorithm(int n, int start) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = result ^ (start + 2 * i);
        }
        return result;
    }

/**
    给你一个字符串 date ，按 YYYY-MM-DD 格式表示一个 现行公元纪年法 日期。返回该日期是当年的第几天。
*/
    public static int dateForYear(String dateStr) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        Date date = format.parse(dateStr);
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDay();

        if (month == 1) {
            return day;
        } else {
            Integer[] bigMonth = {1, 3, 5, 7, 8, 10, 12};
            List<Integer> bigMonthList = Arrays.asList(bigMonth);
            int previousDays = 0;
            boolean leapYear = year % 400 == 0 || (year % 100 != 0 && year % 4 == 0);
            for (int i = 1; i <= month - 1; i++) {
                if (bigMonthList.contains(i)) {
                    previousDays += 31;
                } else if (i == 2) {
                    previousDays += leapYear ? 29 : 28;
                } else {
                    previousDays += 30;
                }
            }
            return previousDays + day;
        }
    }

    /**
     * 将数组最大位放第一最小位放最后
     *
     * @param ints
     * @return
     */
    public static int[] swap(int[] ints) {
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < ints.length; i++) {
            int a = ints[i];
            if (max < a) {
                max = a;
                maxIndex = i;
            }
        }
        ints[maxIndex] = ints[0];
        ints[0] = max;

        int min = max;
        int minIndex = 0;
        for (int i = 0; i < ints.length; i++) {
            int a = ints[i];
            if (min > a) {
                min = a;
                minIndex = i;
            }
        }
        ints[minIndex] = ints[ints.length - 1];
        ints[ints.length - 1] = min;
        return ints;
    }

}
