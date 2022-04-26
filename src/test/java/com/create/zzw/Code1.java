package com.create.zzw;

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

    public static int test(int n, int start) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            result = result ^ (start + 2 * i);
        }
        return result;
    }

    /**给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错 组成的。

    两个字符串 s 和 t 交错 的定义与过程如下，其中每个字符串都会被分割成若干 非空 子字符串：

    s = s1 + s2 + ... + sn
            t = t1 + t2 + ... + tm
|n - m| <= 1
    交错 是 s1 + t1 + s2 + t2 + s3 + t3 + ... 或者 t1 + s1 + t2 + s2 + t3 + s3 + ...
    注意：a + b 意味着字符串 a 和 b 连接。

             

    示例 1：
    输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
    输出：true
    示例 2：

    输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbbaccc"
    输出：false
    示例 3：

    输入：s1 = "", s2 = "", s3 = ""
    输出：true

    提示：
            0 <= s1.length, s2.length <= 100
            0 <= s3.length <= 200
    s1、s2、和 s3 都由小写英文字母组成
    进阶：您能否仅使用 O(s2.length) 额外的内存空间来解决它?。*/
}
