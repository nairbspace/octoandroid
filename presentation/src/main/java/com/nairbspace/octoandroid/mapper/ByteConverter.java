package com.nairbspace.octoandroid.mapper;

import java.util.Locale;

public class ByteConverter {

    public static String toReadableString(long bytes, boolean inSiUnits, boolean inKiB) {
        int unit = inSiUnits ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exponent = (int) (Math.log(bytes) / Math.log(unit));
        String prefix;
        if (inKiB) {
            prefix = (inSiUnits ? "kMGTPE" : "KMGTPE").charAt(exponent - 1) + (inSiUnits ? "" : "i");
        } else {
            prefix = (inSiUnits ? "kMGTPE" : "KMGTPE").charAt(exponent - 1) + ("");
        }
        return String.format(Locale.US, "%.1f %sB", bytes / Math.pow(unit, exponent), prefix);
    }

    public static String toReadableString(long bytes) {
        return toReadableString(bytes, false, false);
    }
}
