package com.zqykj.dadi.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by weifeng on 2017/11/30.
 */
public class FormatUtils {

    private static DecimalFormat df = new DecimalFormat("##0.00");

    public static Float getFloatByScale(Float ft, int scale) {
        BigDecimal db = new BigDecimal(ft);
        db.setScale(scale);
        return db.floatValue();
    }

    public static Float formatFloat(Float f) {
        String reFStr = String.format("%.2f", f);
        return Float.valueOf(reFStr);
    }

}
