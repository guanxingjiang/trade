package com.gxj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * @version 1.0
 * @description:
 * @author: guanxingjiang
 * @time: 2019-10-25 16:29
 */
public class FileUtils {

    protected final static Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String read(File file) {
        return read(file, null);
    }

    public static String read(File file, String encoding) {
        try {
            if (file.exists()) {
                return read(new FileInputStream(file), encoding);
            }
        } catch (Exception e) {
            LOG.error("读取文件失败", e);
        }
        return null;
    }

    public static String read(InputStream is, String encoding) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            byte[] bs = new byte[1024];
            int readed = -1;
            while ((readed = is.read(bs)) != -1) {
                os.write(bs, 0, readed);
            }
            return (new String(os.toByteArray(),
                    "GBK".equalsIgnoreCase(encoding) ? "GBK" : "UTF-8"));
        } catch (Exception e) {
            LOG.error("读取文件失败", e);
        } finally {
            StreamUtils.close(is, os);
        }
        return null;
    }

    /**
     * 写入文件内容
     *
     * @param content
     * @param file
     * @return
     */
    public static boolean write(String content, File file, String encoding) {
        try {
            if (file != null && file.getName().endsWith(".sh")) {
                content = content.replace( "\r", "");// 脚本文件自动替换回车符号
            }
            InputStream in = new ByteArrayInputStream(content.getBytes("GBK"
                    .equalsIgnoreCase(encoding) ? "GBK" : "UTF-8"));
            File dir = file.getParentFile();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            return write(in, new FileOutputStream(file));
        } catch (Exception e) {
            LOG.error("写入文件流失败", e);
        }
        return false;
    }

    public static boolean write(File file, OutputStream os) {
        try {
            return write(new FileInputStream(file), os);
        } catch (Exception e) {
            LOG.error("写入文件流失败", e);
        }
        return false;
    }

    public static boolean write(File file, File dest) {
        try {
            File dir = dest.getParentFile();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            return write(new FileInputStream(file), new FileOutputStream(dest));
        } catch (Exception e) {
            LOG.error("写入文件流失败", e);
        }
        return false;
    }

    public static boolean write(InputStream is, OutputStream os) {
        try {
            byte[] bs = new byte[4096];
            int readed = -1;
            while ((readed = is.read(bs)) != -1) {
                os.write(bs, 0, readed);
            }
            os.flush();
            return true;
        } catch (Exception e) {
            LOG.error("写入文件流失败", e);
        } finally {
            StreamUtils.close(is, os);
        }
        return false;
    }

    public static File writeTmpFile(InputStream is) {
        File tmpFile = new File(System.getProperty("java.io.tmpdir"), "tmpFile/" + UUID.randomUUID());
        try {
            File dir = tmpFile.getParentFile();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            boolean res = write(is, new FileOutputStream(tmpFile));
            if (res && tmpFile.exists()) {
                return tmpFile;
            }
        } catch (Exception e) {
            LOG.error("保存临时文件失败", e);
        }
        return null;
    }

    public static File writeTmpFile(String content, String encoding) {
        File tmpFile = new File(System.getProperty("java.io.tmpdir"),
                "tmpFile/" + UUID.randomUUID());
        try {
            File dir = tmpFile.getParentFile();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            boolean res = write(content, tmpFile, encoding);
            if (res && tmpFile.exists()) {
                return tmpFile;
            }
        } catch (Exception e) {
            LOG.error("保存临时文件失败", e);
        }
        return null;
    }

    public static boolean copy(File from, File to) {
        boolean res = false;
        try {
            if (!from.exists() || !from.isFile()) {
                return false;
            }
            File dir = to.getParentFile();
            if (!dir.exists() || !dir.isDirectory()) {
                dir.mkdirs();
            }
            res = write(new FileInputStream(from), new FileOutputStream(to));
            if (res && to.exists()) {
                to.setLastModified(from.lastModified());// copy不改变时间戳
            }
        } catch (Exception e) {
            LOG.error("写入文件流失败", e);
        }
        return res;
    }

    public static void copyDir(File from, File to) {
        if (!from.exists()) {
            return;
        }
        if (from.isDirectory()) {
            for (File file : from.listFiles()) {
                copyDir(file, new File(to.getAbsolutePath(), file.getName()));
            }
        } else {
            copy(from, to);
        }
    }


    public static boolean deleteFile(File file) {
        boolean res = false;
        if (file == null || !file.exists()) {
            return res;
        }
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                res = deleteFile(f);
            }
            delEmptyDir(file);
        }
        if (file.exists()) {
            res = file.delete();
            LOG.debug("删除文件[{}]...[{}]", file.getAbsolutePath(), res);
        }
        return res;
    }

    private static void delEmptyDir(File file) {
        if (file.isDirectory()) {
            File[] list = file.listFiles();
            if (list.length == 0) {
                LOG.debug("删除空目录[{}]...[{}]", file.getAbsolutePath(),
                        file.delete());
                delEmptyDir(file.getParentFile());
            }
        }
    }


    // 计算文件大小
    public static String fileSize(long value) {
        return fileSize(value, 2);
    }

    public static String fileSize(long value, int scale) {
        if (value <= 0) {return "0B";}
        String[] unitArr = { "B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB","YB" };
        int index = new BigDecimal(Math.floor(Math.log(value) / Math.log(1024))).intValue();
        BigDecimal decimal = new BigDecimal(value / Math.pow(1024D, index));
        Double val = decimal.setScale(scale,BigDecimal.ROUND_HALF_UP).doubleValue();//设置精度
        String size = String.valueOf(val);
        if(size.endsWith(".0")) {size = size.replace(".0", "");}
        return size + unitArr[index];
    }

    /**
     * 列出文件列表
     *
     * @param dir
     *            目录
     * @param order
     *            是否排序
     * @param recursion
     *            是否递归
     * @return
     */
    public static List<File> traversal(File dir, boolean order, boolean recursion) {
        List<File> ret = new ArrayList<>();
        if (dir == null || !dir.exists() || dir.isFile()) {
            return ret;
        }
        File[] fileList = dir.listFiles();
        if (fileList == null || fileList.length <= 0) {
            return ret;
        }
        for (File file : fileList) {
            if (file.isDirectory()) {
                if (recursion) {
                    ret.addAll(traversal(file, order, recursion));
                }
            } else {
                ret.add(file);
            }
        }
        if (order) {
            Collections.sort(ret, new Comparator<File>() {
                public int compare(File file1, File file2) {
                    return file1.getAbsolutePath().compareTo(
                            file2.getAbsolutePath());
                }
            });
        }
        return ret;
    }

}
