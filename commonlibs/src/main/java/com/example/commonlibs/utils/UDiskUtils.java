package com.example.commonlibs.utils;

import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * U盘工具类
 * @author ZJ
 * created at 2020/3/31 18:54
 */
public class UDiskUtils {


    /**
     * 通过反射调用获取内置存储和外置sd卡根路径(通用)
     *
     * @param mContext 上下文
     * @param keyWord  Primary:主存储路径
     * @return
     * @n Removable:可移除存储路径
     * @n unRemovable:不可移除存储路径
     * @n SD:外置sd路径
     * @n USB:otgusb路径
     * @n 其他:所有已挂载路径
     */
    public static List<String> getStoragePath(Context mContext, String keyWord) {


        List<String> pathList = new ArrayList<>();
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isPrimary = storageVolumeClazz.getMethod("isPrimary");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getUserLabel = storageVolumeClazz.getMethod("getUserLabel");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                String userLabel = (String) getUserLabel.invoke(storageVolumeElement);
                boolean is_Removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                boolean is_Primary = (Boolean) isPrimary.invoke(storageVolumeElement);

                if (keyWord.equals("Primary")) {

                    if (is_Primary) {
                        pathList.add(path);
                    }


                } else if (keyWord.equals("Removable")) {

                    if (is_Removable) {
                        pathList.add(path);
                    }

                } else if (keyWord.equals("unRemovable")) {

                    if (!is_Removable) {
                        pathList.add(path);
                    }

                } else if (keyWord.equals("SD")) {

                    if (path.contains("SD") || path.contains("sd")) {
                        pathList.add(path);
                    }

                } else if (keyWord.equals("USB")) {

                    if (path.contains("usb") || path.contains("USB")) {
                        pathList.add(path);
                    }

                } else {
                    pathList.add(path);
                }
//
//                LogUtils.logger.trace("path：{} ",path);
//                LogUtils.logger.trace("userLabel：{} ",userLabel);
//                LogUtils.logger.trace("is_Removable：{} ",is_Removable);
//                LogUtils.logger.trace("is_Primary：{} ",is_Primary);

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return pathList;
    }


    public static List<String> getUSBPaths(Context con) {//反射获取路径
        String[] paths = null;
        List<String> data = new ArrayList<>();    // include sd and usb devices
        StorageManager storageManager = (StorageManager) con.getSystemService(Context.STORAGE_SERVICE);
        try {
            paths = (String[]) StorageManager.class.getMethod("getVolumePaths", String.class).invoke(storageManager, "");
            for (String path : paths) {
                String state = (String) StorageManager.class.getMethod("getVolumeState", String.class).invoke(storageManager, path);
                if (state.equals(Environment.MEDIA_MOUNTED) && !path.contains("emulated")) {
                    data.add(path);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    private static final String MOUNTS_FILE = "/proc/mounts";

    /**
     * 判断是否有U盘插入,当U盘开机之前插入使用该方法.
     *
     * @param path
     * @return
     */
    public static boolean isMounted(String path) {
        boolean blnRet = false;
        String strLine = null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(MOUNTS_FILE));

            while ((strLine = reader.readLine()) != null) {
                if (strLine.contains(path)) {
                    blnRet = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                reader = null;
            }
        }
        return blnRet;
    }

}
