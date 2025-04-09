package com.welcommu.modulecommon.util;

public class FileUtil {
    public static String getExtensionFromContentType(String contentType) {
        switch (contentType) {
            case "image/jpeg": return ".jpg";
            case "image/png": return ".png";
            case "image/gif": return ".gif";
            case "image/webp": return ".webp";
            case "image/svg+xml": return ".svg";
            case "image/bmp": return ".bmp";
            case "application/pdf": return ".pdf";
            case "application/rtf": return ".rtf";
            case "text/plain": return ".txt";
            case "application/msword": return ".doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document": return ".docx";
            case "application/vnd.ms-excel": return ".xls";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet": return ".xlsx";
            case "application/vnd.ms-powerpoint": return ".ppt";
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation": return ".pptx";
            case "application/zip": return ".zip";
            case "application/x-rar-compressed": return ".rar";
            case "application/x-7z-compressed": return ".7z";
            case "application/gzip": return ".gz";
            case "application/json": return ".json";
            case "application/xml": return ".xml";
            case "text/html": return ".html";
            case "text/css": return ".css";
            case "application/javascript": return ".js";
            case "application/octet-stream": return "";

            default: return "";
        }
    }
}
