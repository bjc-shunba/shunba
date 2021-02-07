package com.baidu.shunba.socket.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
	private static final Logger Log = LoggerFactory.getLogger(LogUtils.class);

    public static enum Level {
        NONE(0),
        VERBOSE(2),
        DEBUG(3),
        INFO(4),
        WARN(5),
        ERROR(6),
        ASSERT(7),
        ALL(Integer.MAX_VALUE),
        ;

        public final int value;
        private Level(int value) {
            this.value = value;
        }

    }

    private static boolean debug = true;

    private static Level currentLevel = Level.ALL;

    public static boolean isDebug() {
        return debug;
    }

    public static void setDebug(boolean debug_) {
        debug = debug_;
    }

    public void setDebugLevel(Level level) {
        currentLevel = level;
    }

    public static void v(String tag, String msg) {
        if (debug && currentLevel.value >= Level.VERBOSE.value) {
            Log.debug(tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable e) {
        if (debug && currentLevel.value >= Level.VERBOSE.value) {
            Log.debug(tag, msg, e);
        }
    }

    public static void d(String tag, String msg) {
        if (debug && currentLevel.value >= Level.DEBUG.value) {
            Log.debug(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable e) {
        if (debug && currentLevel.value >= Level.DEBUG.value) {
            Log.debug(tag, msg, e);
        }
    }

    public static void i(String tag, String msg) {
        if (debug && currentLevel.value >= Level.INFO.value) {
            Log.debug(tag, msg);
        }
    }

    public static void i(String tag, String msg, Throwable e) {
        if (debug && currentLevel.value >= Level.INFO.value) {
            Log.debug(tag, msg, e);
        }
    }

    public static void w(String tag, String msg) {
        if (debug && currentLevel.value >= Level.WARN.value) {
            Log.debug(tag, msg);
        }
    }

    public static void w(String tag, String msg, Throwable e) {
        if (debug && currentLevel.value >= Level.WARN.value) {
            Log.debug(tag, msg, e);
        }
    }

    public static void e(String tag, String msg) {
        if (debug && currentLevel.value >= Level.ERROR.value) {
            Log.debug(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable t) {
        if (debug && currentLevel.value >= Level.ERROR.value) {
            Log.debug(tag, msg, t);
        }
    }
}
