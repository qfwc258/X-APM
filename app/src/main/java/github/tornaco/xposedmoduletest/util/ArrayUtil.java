package github.tornaco.xposedmoduletest.util;

import java.util.ArrayList;

/**
 * Created by guohao4 on 2017/12/28.
 * Email: Tornaco@163.com
 */

public abstract class ArrayUtil {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static String[] emptyStringArray() {
        return EMPTY_STRING_ARRAY;
    }

    public static String[] convertObjectArrayToStringArray(Object[] objArr) {
        if (objArr == null || objArr.length == 0) {
            return new String[0];
        }
        String[] out = new String[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            Object o = objArr[i];
            if (o == null) continue;
            String pkg = String.valueOf(o);
            out[i] = pkg;
        }
        return out;
    }
}
