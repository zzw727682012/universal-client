package com.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Code20220715 {

    /**
     * 给两个整数数组nums1和nums2，返回 两个数组中 公共的 、长度最长的子数组的长度。
     * <p>
     * 示例 1：
     * 输入：nums1 = [1,2,3,2,1], nums2 = [3,2,1,4,7]
     * 输出：3
     * 解释：长度最长的公共子数组是 [3,2,1] 。
     * <p>
     * 示例 2：
     * 输入：nums1 = [0,0,0,0,0], nums2 = [0,0,0,0,0]
     * 输出：5
     * <p>
     * 提示：
     * 1 <= nums1.length, nums2.length <= 1000
     * 0 <= nums1[i], nums2[i] <= 100
     * <p>
     * 链接：https://leetcode.cn/problems/maximum-length-of-repeated-subarray
     * <p>
     * <p>
     * [70,39,25,40,7]
     * [52,20,67,5,31]
     *
     * @param nums1
     * @param nums2
     * @return
     */
    public static int findLength(int[] nums1, int[] nums2) {
        int[] a = new int[nums2.length + 1];
        int result = 0;
        for (int i = 1; i <= nums1.length; i++) {
            for (int j =1;j<= nums2.length; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    a[j] = a[j - 1] + 1;
                } else {
                    a[j] = 0;
                }
                result = Math.max(result, a[j]);
            }
        }
        return result;
    }
}
