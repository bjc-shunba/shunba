package com.baidu.shunba.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

@Slf4j
public class ObjectUtils {
    public static String getString(Object obj) {
        String str = null;
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }

    public static String getDoubleWithFormat(double data) {
        String db = "0";
        DecimalFormat df = new DecimalFormat("#,###.00");
        try {
            db = df.format(data);
        } catch (Exception e) {
        }
        return db;
    }

    public static String getString(Object obj, String defaultVal) {
        String str = defaultVal;
        if (obj != null) {
            str = obj.toString();
        }
        return str;
    }

    public static Short getByte(Object obj) {
        /* 如果传入字符串5.00，则光后面的返回null */
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }

        Short integer = null;
        try {
            integer = Short.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return integer;
    }

    public static Short getShort(Object obj) {
        /* 如果传入字符串5.00，则光后面的返回null */
        if (obj instanceof Number) {
            return ((Number) obj).shortValue();
        }

        Short integer = null;
        try {
            integer = Short.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return integer;
    }

    public static Integer getInteger(Object obj) {
        /* 如果传入字符串5.00，则光后面的返回null */
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }

        Integer integer = null;
        try {
            integer = Integer.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return integer;
    }

    public static int getIntValue(Object obj) {
        return getIntValue(obj, 0);
    }

    public static int getIntValue(Object obj, int defaultValue) {
        int integer = defaultValue;
        try {
            /* 如果传入字符串5.00，则光后面的返回null */
            if (obj instanceof Number) {
                return ((Number) obj).intValue();
            }

            integer = Integer.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return integer;
    }

    public static float getFloatValue(Object obj, float defaultValue) {
        float db = defaultValue;
        try {
            db = Float.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return db;
    }

    public static Float getFloat(Object obj) {
        Float db = null;
        try {
            db = Float.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return db;
    }

    public static float getFloatValue(Object obj) {
        return getFloatValue(obj, 0.0f);
    }

    public static Double getDouble(Object obj) {
        Double db = null;
        try {
            db = Double.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return db;
    }

    public static double getDoubleValue(Object obj) {
        return getDoubleValue(obj, 0.0000d);
    }

    public static double format(Double d) {
        if (d == null) {
            return 0.00d;
        }
        DecimalFormat df = new DecimalFormat("#######.##");
        return getDoubleValue(df.format(d.doubleValue()));
    }

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return format(b1.add(b2).doubleValue());
    }

    public static double add(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return format(b1.add(b2).doubleValue());
    }

    public static double add(Double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return format(b1.add(b2).doubleValue());
    }

    public static double mul(Double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(ObjectUtils.getDoubleValue(v1)));
        BigDecimal b2 = new BigDecimal(Double.toString(ObjectUtils.getDoubleValue(v2)));
        return b1.multiply(b2).doubleValue();
    }

    public static double decimal5(Double v1) {
        if (v1 == null) {
            return 0.00d;
        }
        DecimalFormat df = new DecimalFormat("#######.#####");
        return getDoubleValue(df.format(v1.doubleValue()));
    }

    public static double getDoubleValue(Object obj, double defaultValue) {
        double db = defaultValue;
        try {
            if (obj instanceof Number) {
                db = ((Number) obj).doubleValue();
            } else {
                db = Double.valueOf(obj.toString());
            }
        } catch (Exception e) {
        }
        DecimalFormat df = new DecimalFormat("#######.####");
        try {
            db = Double.valueOf(df.format(db));
        } catch (Exception e) {
        }
        return db;
    }


    public static Double convert(double d) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        return getDoubleValue(d1);
    }

    public static double getDoubleValueToTwoDecimal(Object obj, double defaultValue) {
        double db = defaultValue;
        try {
            if (obj instanceof Number) {
                db = ((Number) obj).doubleValue();
            } else {
                db = Double.valueOf(obj.toString());
            }
        } catch (Exception e) {
        }
        DecimalFormat df = new DecimalFormat("#######.##");
        try {
            db = Double.valueOf(df.format(db));
        } catch (Exception e) {
        }
        return db;
    }

    public static double getDoubleValueToSix(Object obj, double defaultValue) {
        double db = defaultValue;
        try {
            if (obj instanceof Number) {
                db = ((Number) obj).doubleValue();
            } else {
                db = Double.valueOf(obj.toString());
            }
        } catch (Exception e) {
        }
        DecimalFormat df = new DecimalFormat("#######.######");
        try {
            db = Double.valueOf(df.format(db));
        } catch (Exception e) {
        }
        return db;
    }


    public static double decimal(Double v1) {
        if (v1 == null) {
            v1 = 0d;
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        return format(b1.doubleValue());
    }

    public static Long getLong(Object obj) {
        Long lg = null;
        try {
            lg = Long.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return lg;
    }

    public static BigInteger getBigInteger(Object obj) {
        BigInteger decimal = null;
        try {
            decimal = new BigInteger(getString(obj));
        } catch (Exception e) {
        }
        return decimal;
    }

    public static BigDecimal getBigDecimal(Object obj) {
        BigDecimal decimal = null;
        try {
            decimal = BigDecimal.valueOf(getDouble(obj));
        } catch (Exception e) {
        }
        return decimal;
    }

    public static BigDecimal getBigDecimal(Object obj, int scale) {
        BigDecimal decimal = null;
        try {
            decimal = BigDecimal.valueOf(getDouble(obj));
            decimal = decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
        }
        return decimal;
    }

    public static long getLongValue(Object obj) {
        return getLongValue(obj, 0l);
    }

    public static long getLongValue(Object obj, long defaultValue) {
        long lg = defaultValue;
        try {
            lg = Long.valueOf(obj.toString());
        } catch (Exception e) {
        }
        return lg;
    }

    public static boolean isStringTrue(String str) {
        try {
            if (str != null) {
                str = str.trim();
                if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("yes") || str.equalsIgnoreCase("y")) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    public static double addDoubles(Object... doubles) {
        double value = 0.0;
        if (doubles != null) {
            for (Object dou : doubles) {
                value += getDoubleValue(dou);
            }
        }
        return value;
    }

    public static double sumDoubles(Object... doubles) {
        BigDecimal value = new BigDecimal(0.0);
        if (doubles != null) {
            for (Object dou : doubles) {
                value = value.add(new BigDecimal(getDoubleValue(dou)));
            }
        }
        return value.doubleValue();
    }


    public static double getPriceValue(final Object price) {
        BigDecimal b = new BigDecimal(getDoubleValue(price));
        return b.setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        //return Math.round(price * 100) / 100.0;
    }

    public static double getPriceYuanValue(final Object price) {
        return Math.round(getDoubleValue(price));
    }

    public static void updateObjectWithPriceWithoutKeys(Object object, String... withoutParams) {
        Field[] fs = object.getClass().getDeclaredFields();
        for (Field f : fs) {
            try {
                if (Modifier.isFinal(f.getModifiers())) {
                    continue;
                }
                if (f.getType() != double.class && f.getType() != Double.class) {
                    continue;
                }
                try {
                    if (withoutParams != null) {
                        String fn = f.getName();
                        for (String params : withoutParams) {
                            if (params.equals(fn)) {
                                continue;
                            }
                        }
                    }
                } catch (Exception e) {
                }
                boolean acc = f.isAccessible();
                try {
                    f.setAccessible(true);
                    f.set(object, getPriceValue(getDoubleValue(f.get(object))));
                } finally {
                    f.setAccessible(acc);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回hibernate的主键id，Integer越界了怎么搞？
     *
     * @param obj
     * @return
     */
    public static Serializable getHibernateId(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        } else {
            try {
                return Integer.valueOf(obj.toString());
            } catch (Exception e) {
                log.error("parse {} to id error: {}", obj, e);
            }

            return (Serializable) obj;
        }
    }

    public static boolean getBooleanValue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return isStringTrue(obj.toString());
    }

    /**
     * n1 + n2
     *
     * @param n1
     * @param n2
     * @return
     */
    public static Integer addInteger(Integer n1, Integer n2) {
        if (n1 != null) {
            return n2 != null ? n1 + n2 : n1;
        } else {
            return n2;
        }
    }

    /**
     * n2 - n1
     *
     * @param n2
     * @param n1
     * @return
     */
    public static Integer subtractInteger(Integer n2, Integer n1) {
        if (n2 != null) {
            return n1 != null ? n2 - n1 : n2;
        } else {
            return n1 != null ? 0 - n1 : null;
        }
    }

    public static boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }

        if (obj1 == null && obj2 != null) {
            return false;
        }

        if (obj1 != null && obj2 == null) {
            return false;
        }

        if (obj1 == obj2) {
            return true;
        }

        if (obj1.equals(obj2)) {
            return true;
        }

        return false;
    }

    public static boolean sameWidth(Integer var1, int var2) {
        return var1 != null && var1 == var2;
    }

    public static boolean sameWithOrNull(Integer var1, int var2) {
        return var1 == null || var1 == var2;
    }
}
