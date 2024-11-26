package com.roshine.matrixsphere.base.client.utils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author roshine
 * @version 1.0.0
 * @date 2019-12-16 20:13
 * @Description 用于精确计算
 */
public class ArithmeticUtils {

    /**
     * 默认除法运算精度
     */
    private static final int DEF_DIV_SCALE = 10;

    /**
     * 提供精确的加法运算
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.add(v2);
    }

    /**
     * 提供精确的加法运算
     * @param v1
     * @param v2
     * @param scale 保留scale 位小数(必须>=0)
     * @return
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.add(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的减法运算
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2);
    }

    /**
     * 提供精确的减法运算
     * @param v1
     * @param v2
     * @param scale 保留scale 位小数(必须>=0)
     * @return
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.subtract(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的乘法运算
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.multiply(v2);
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.multiply(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供(相对)精确的除法运算，当发生除不尽的情况时，默认计算到10位，以后四舍五入
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        return round(v1.divide(v2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP), scale);
    }

    /**
     * 提供(相对)精确的除法运算，当发生除不尽的情况时，计算到accuracy位，以后四舍五入
     * @param v1
     * @param v2
     * @param scale 保留scale 位小数(必须>=0)
     * @param accuracy 计算精度
     * @return
     */
    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, int accuracy) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        return round(v1.divide(v2, accuracy, BigDecimal.ROUND_HALF_UP), scale);
    }

    /**
     * 提供精确的小数位四舍五入处理
     * @param v
     * @param scale
     * @return
     */
    public static BigDecimal round(BigDecimal v, int scale) {
        if (scale < 0) {
            return v;
        }
        return v.setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 取余数
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static BigDecimal remainder(BigDecimal v1, BigDecimal v2, int scale) {
        if (scale < 0) {
            return BigDecimal.ZERO;
        }
        return v1.remainder(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 比较大小
     * @param v1
     * @param v2
     * @return 如果v1 > v2 返回true, 否则返回false
     */
    public static boolean compare(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.compareTo(v2) > 0;
    }

    /**
     * 比较大小
     * @param v1
     * @param v2
     * @return 如果v1 >= v2 返回true, 否则返回false
     */
    public static boolean compareGreater(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.compareTo(v2) >= 0;
    }

    /**
     * 比较大小
     * @param v1
     * @param v2
     * @return 如果v1 == v2 返回true, 否则返回false
     */
    public static boolean compareEqual(BigDecimal v1, BigDecimal v2) {
        if (Objects.isNull(v1)) {
            v1 = BigDecimal.ZERO;
        }
        if (Objects.isNull(v2)) {
            v2 = BigDecimal.ZERO;
        }
        return v1.compareTo(v2) == 0;
    }

    /**
     * 比较大小
     * @param v1
     * @param v2
     * @return 如果v1 == v2 返回true, 否则返回false
     */
    public static boolean compareEqual(Long v1, Long v2) {
        if (Objects.isNull(v1)) {
            v1 = 0L;
        }
        if (Objects.isNull(v2)) {
            v2 = 0L;
        }
        return v1.compareTo(v2) == 0;
    }

    /**
     * 两数相除，返回商和余
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal[] divideAndRemainder(BigDecimal v1, BigDecimal v2) {
        return v1.divideAndRemainder(v2);
    }

    /**
     * bigDecimal to String
     * @param data
     * @param scale
     * @return
     */
    public static String bigDecimalToString(BigDecimal data, int scale) {
        if (data == null) {
            return data.setScale(scale, BigDecimal.ROUND_DOWN).toPlainString();
        }
        return null;
    }
}
