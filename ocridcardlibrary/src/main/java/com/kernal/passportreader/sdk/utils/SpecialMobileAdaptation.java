package com.kernal.passportreader.sdk.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;

import java.util.HashMap;

/**
 * 用于特殊机型预览图像与正常图像不一致和获取屏幕宽高不变化的情况
 */
public class SpecialMobileAdaptation {
    private HashMap<String, SpecialMobile> specialMobileHashMap = new HashMap<>();
    public static SpecialMobileAdaptation instance = new SpecialMobileAdaptation();

    private SpecialMobileAdaptation() {
        /**
         * vivo X50 pro
         */
        specialMobileHashMap.put("vivo-V2005A", new SpecialMobile(0, 90));
        /**
         * vivo X50 pro+
         */
        specialMobileHashMap.put("vivo-V2011A", new SpecialMobile(0, 90));
        /**
         * vivo X50
         */
        specialMobileHashMap.put("vivo-V2001A", new SpecialMobile(0, 90));
        /**
         * vivo X27 (8-256g)
         */
        specialMobileHashMap.put("vivo-V1829A", new SpecialMobile(0, 90));
        /**
         * vivo X27 (8-128)
         */
        specialMobileHashMap.put("vivo-V1838A", new SpecialMobile(0, 90));
        /**
         * vivo X30 pro
         */
        specialMobileHashMap.put("vivo-V1938T", new SpecialMobile(0, 90));
    }

    /**
     * 返回特殊机型对应的旋转角度
     *
     * @param
     * @return
     */
    public int getOrientation(Context context) {
        String deviceName = getDeviceName();
        int orientation = -1;
        if (specialMobileHashMap.containsKey(deviceName)) {
            orientation = context.getResources().getConfiguration().orientation; //获取设置的配置信息;
            orientation = orientation == Configuration.ORIENTATION_LANDSCAPE ? specialMobileHashMap.get(deviceName).RotationAngle_LANDSCAPE
                    : specialMobileHashMap.get(deviceName).RotationAngle_PORTRAIT;
        }
        return orientation;
    }

    /**
     * 是否为特殊机型
     *
     * @return
     */
    public boolean isSpecialModel() {
        String deviceName = getDeviceName();
        return specialMobileHashMap.containsKey(deviceName);
    }

    private String getDeviceName() {
        return Build.BRAND + "-" + Build.MODEL;
    }

    /**
     * @param context
     * @param screenResolution
     * @return
     */
    public Point getScreenResolution(Context context, Point screenResolution) {
        /**
         * 如果没有命中特殊机型直接返回传入的参数值
         */
        if (!isSpecialModel()) return screenResolution;
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        Point point = new Point();
        /**
         *获取屏幕方向
         */
        switch (mConfiguration.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                point.x = Math.max(screenResolution.x, screenResolution.y);
                point.y = Math.min(screenResolution.x, screenResolution.y);
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                point.x = Math.min(screenResolution.x, screenResolution.y);
                point.y = Math.max(screenResolution.x, screenResolution.y);
                break;
            default:
                break;
        }
        return point;
    }

    /**
     * 特殊机型（厂商-型号）
     */
    class SpecialMobile {
        /**
         * 强制横屏情况下相机应该设置的正确预览角度
         * 强制竖屏情况下相机应该设置的正确预览角度
         */
        private int RotationAngle_LANDSCAPE, RotationAngle_PORTRAIT;

        public SpecialMobile(int RotationAngle_LANDSCAPE, int RotationAngle_PORTRAIT) {
            this.RotationAngle_LANDSCAPE = RotationAngle_LANDSCAPE;
            this.RotationAngle_PORTRAIT = RotationAngle_PORTRAIT;
        }
    }
}
