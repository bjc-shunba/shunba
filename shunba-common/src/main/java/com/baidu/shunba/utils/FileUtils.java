package com.baidu.shunba.utils;

import java.io.*;
import java.nio.channels.FileChannel;

public class FileUtils {

    public static final String fileExtendSplit = ".";
    public static final char pathSplit = '/';

    public static String getFileType(Object filePath) {
        try {
            String path = filePath.toString();
            String typeSplit = ".";
            String type = path.substring(path.lastIndexOf(typeSplit) + typeSplit.length());
            return type.toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFileNameWithExtend(final String url) {
        String fileName = url.substring(url.lastIndexOf(pathSplit) + 1);
        return fileName;
    }

    public static String getFileNameWithoutExtend(final String url) {
        final String fullFile = getFileNameWithExtend(url);
        try {
            return fullFile.substring(0, fullFile.lastIndexOf(fileExtendSplit));
        } catch (Exception ex) {
            return fullFile;
        }
    }

    //获取后缀名
    public static String getExtend(String filepath) {
        if (filepath != null && filepath.lastIndexOf('.') > 0) {
            return filepath.substring(filepath.lastIndexOf('.') + 1, filepath.length());
        }
        return "";
    }

    public static void copy(File source, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            close(inputChannel);
            close(outputChannel);
        }
    }

    public static void copy(FileInputStream input, File dest) {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            inputChannel = input.getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (IOException e) {
            close(inputChannel);
            close(outputChannel);
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
