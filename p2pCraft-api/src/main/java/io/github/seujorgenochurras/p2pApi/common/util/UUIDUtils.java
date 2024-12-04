package io.github.seujorgenochurras.p2pApi.common.util;

public class UUIDUtils {

    private UUIDUtils() {
    }

    public static String addUUIDDashes(String rawUUID) {
        return rawUUID.substring(0, 8) + "-" + rawUUID.substring(8, 12) + "-" + rawUUID.substring(12, 16) + "-"
            + rawUUID.substring(16, 20) + "-" + rawUUID.substring(20);
    }
}
