package com.welcommu.modulecommon.util;

public class FileUtil {
    public static String getExtensionFromContentType(String contentType) {
        switch (contentType) {
            case "image/jpeg": return ".jpg";
            case "image/png": return ".png";
            case "application/pdf": return ".pdf";
            case "text/plain": return ".txt";
            default: return "";
        }
    }
}
