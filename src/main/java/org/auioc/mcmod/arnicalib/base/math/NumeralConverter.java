/*
 * Copyright (C) 2022-2025 AUIOC.ORG
 *
 * This file is part of ArnicaLib, a mod made for Minecraft.
 *
 * ArnicaLib is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.auioc.mcmod.arnicalib.base.math;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WakelessSloth56
 * @version 1.0.1
 * @see <a href="https://gist.github.com/WakelessSloth56/e574b509b4bac353b86d1430584a5051">Gist: RomanNumberConverter.java</a>
 * @see <a href="https://gist.github.com/WakelessSloth56/150efeb818bdc67ce2c268dfc7b1365a">Gist: ChineseNumberConverter.java</a>
 */
public class NumeralConverter {

    private static final int[] ROMAN_VALUES = {
        /*                             */ 1000000000,
        900000000, 500000000, 400000000, 100000000,
        90000000, 50000000, 40000000, 10000000,
        9000000, 5000000, 4000000, 1000000,
        900000, 500000, 400000, 100000,
        90000, 50000, 40000, 10000,
        9000, 5000, 4000, 1000,
        900, 500, 400, 100,
        90, 50, 40, 10,
        9, 5, 4, 1
    };
    private static final String[] ROMAN_SYMBOLS = {
        /*                                          */ "M\u033F",
        "C\u033FM\u033F", "D\u033F", "C\u033FD\u033F", "C\u033F",
        "X\u033FC\u033F", "L\u033F", "X\u033FL\u033F", "X\u033F",
        "MX\u033F", "V\u033F", "MV\u033F", "M\u0305",
        "C\u0305M\u0305", "D\u0305", "C\u0305D\u0305", "C\u0305",
        "X\u0305C\u0305", "L\u0305", "X\u0305L\u0305", "X\u0305",
        "MX\u0305", "V\u0305", "MV\u0305", "M",
        "CM", "D", "CD", "C",
        "XC", "L", "XL", "X",
        "IX", "V", "IV", "I"
    };
    private static final String[] ROMAN_SYMBOLS_NO_OVERLINE = {
        /*                 */ "M:",
        "C:M:", "D:", "C:D:", "C:",
        "X:C:", "L:", "X:L:", "X:",
        "MX:", "V:", "MV:", "M.",
        "C.M.", "D.", "C.D.", "C.",
        "X.C.", "L.", "X.L.", "X.",
        "MX.", "V.", "MV.", "M",
        "CM", "D", "CD", "C",
        "XC", "L", "XL", "X",
        "IX", "V", "IV", "I"
    };

    public static String toRoman(int n, boolean noOverline) {
        if (n == 0) return "N";
        n = Math.abs(n);
        var sb = new StringBuilder();
        int i = 0;
        while (i < ROMAN_VALUES.length) {
            while (n >= ROMAN_VALUES[i]) {
                sb.append(noOverline ? ROMAN_SYMBOLS_NO_OVERLINE[i] : ROMAN_SYMBOLS[i]);
                n -= ROMAN_VALUES[i];
            }
            i++;
        }
        return sb.toString();
    }

    // ============================================================================================================== //

    private static final String[] CHINESE_NUMBERS_SMALL = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
    private static final String[] CHINESE_NUMBERS_BIG = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
    private static final String[] CHINESE_UNITS_SMALL = { "", "十", "百", "千", "万", "亿" };
    private static final String[] CHINESE_UNITS_BIG = { "", "拾", "佰", "仟", "萬", "億" };
    private static final String CHINESE_NEGATIVE_PREFIX = "负";

    private static List<Integer> toChinese(int n) {
        var a = new ArrayList<Integer>();
        int i = 0;
        while (n > 0) {
            int s = n % 10000;
            n /= 10000;
            if (i > 0) {
                a.add(i + 10 + 3);
            }
            a.addAll(sectionToChinese(s));
            if (n > 0 && s < 1000) {
                a.add(0);
            }
            i++;
        }
        return a;
    }

    private static List<Integer> sectionToChinese(int n) {
        var a = new ArrayList<Integer>();
        boolean f = true;
        int i = 0;
        while (n > 0) {
            int v = n % 10;
            n /= 10;
            if (v == 0) {
                if (!f) {
                    a.add(v);
                    f = true;
                }
            } else {
                a.add(i + 10);
                a.add(v);
                f = false;
            }
            i++;
        }
        return a;
    }

    public static String toChinese(int n, boolean big) {
        var sb = new StringBuilder();
        if (n < 0) {
            sb.append(CHINESE_NEGATIVE_PREFIX);
        }
        var a = toChinese(Math.absExact(n));
        for (int i = a.size() - 1; i >= 0; --i) {
            int v = a.get(i);
            if (v > 9) {
                sb.append(big ? CHINESE_UNITS_BIG[v - 10] : CHINESE_UNITS_SMALL[v - 10]);
            } else {
                sb.append(big ? CHINESE_NUMBERS_BIG[v] : CHINESE_NUMBERS_SMALL[v]);
            }
        }
        return sb.toString();
    }

}
