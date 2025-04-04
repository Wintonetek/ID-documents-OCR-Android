package com.kernal.passportreader.sdk.utils;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import kernal.idcard.camera.CardOcrRecogConfigure;

public final class CameraConfigurationManager {
    private static final int TEN_DESIRED_ZOOM = 27;
    private static final Pattern COMMA_PATTERN = Pattern.compile(",");
    private final Context mContext;
    private Point mScreenResolution;
    private Point mCameraResolution;
    private Point mPreviewResolution;

    public CameraConfigurationManager(Context context) {
        mContext = context;
    }

    public void initFromCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();

        if (CameraConfigurationManager.autoFocusAble(camera)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        }

        mScreenResolution = CardScreenUtil.getScreenResolution(mContext);
        Point screenResolutionForCamera = new Point();
        screenResolutionForCamera.x = mScreenResolution.x;
        screenResolutionForCamera.y = mScreenResolution.y;
        // preview size is always something like 480*320, other 320*480
        int orientation = CardScreenUtil.getScreenOrientation(mContext);
        if (orientation == CardScreenUtil.ORIENTATION_PORTRAIT) {
            screenResolutionForCamera.x = mScreenResolution.y;
            screenResolutionForCamera.y = mScreenResolution.x;
        }

        if ((float) screenResolutionForCamera.x / screenResolutionForCamera.y == 0.75) {
            screenResolutionForCamera.x = 1280;
            screenResolutionForCamera.y = 960;
            //   WriteUtil.writeLog("设置的目标分辨率1280X960");
            mPreviewResolution = getPreviewResolution(parameters, screenResolutionForCamera);
        }/* else  if((float) screenResolutionForCamera.x/screenResolutionForCamera.y<0.5625){
            screenResolutionForCamera.x=1440;
            screenResolutionForCamera.y=720;
            WriteUtil.writeLog("设置的目标分辨率1440X720");
            mPreviewResolution = getPreviewResolution(parameters, screenResolutionForCamera);
        }*/ else {
            //  WriteUtil.writeLog("设置的目标分辨率1280X720");
            if (CardOcrRecogConfigure.getInstance().nMainId == 2010) {
                /**
                 * 算法强烈要求
                 * 由于印度尼西亚身份证识别
                 * 需要高清图片所以设置成1080P
                 */
                screenResolutionForCamera.x = 1920;
                screenResolutionForCamera.y = 1080;
            } else {
                screenResolutionForCamera.x = 1280;
                screenResolutionForCamera.y = 720;
            }
            mPreviewResolution = getPreviewResolution(parameters, screenResolutionForCamera);
        }
        //  WriteUtil.writeLog("获取的预览分辨率为"+mPreviewResolution.x+"X"+mPreviewResolution.y);
        if (orientation == CardScreenUtil.ORIENTATION_PORTRAIT) {
            mCameraResolution = new Point(mPreviewResolution.y, mPreviewResolution.x);
        } else {
            mCameraResolution = mPreviewResolution;
        }

    }

    /**
     * 获取对焦方式
     *
     * @param camera
     * @return
     */
    public static boolean autoFocusAble(Camera camera) {
        List<String> supportedFocusModes = camera.getParameters().getSupportedFocusModes();
        String focusMode = findSettableValue(supportedFocusModes, Camera.Parameters.FOCUS_MODE_AUTO);
        return focusMode != null;
    }

    public Point getCameraResolution() {
        return mCameraResolution;
    }

    public Point getmPreviewResolution() {
        return mPreviewResolution;
    }

    /**
     * 设置相机的参数
     *
     * @param camera
     */
    public void setDesiredCameraParameters(Camera camera) {
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewSize(mPreviewResolution.x, mPreviewResolution.y);
        setZoom(parameters);
        camera.setDisplayOrientation(getDisplayOrientation());
        camera.setParameters(parameters);
    }

    /**
     * 开启闪光灯
     *
     * @param camera
     */
    public void openFlashlight(Camera camera) {
        doSetTorch(camera, true);
    }

    /**
     * 关闭闪光灯
     *
     * @param camera
     */
    public void closeFlashlight(Camera camera) {
        doSetTorch(camera, false);
    }

    private void doSetTorch(Camera camera, boolean newSetting) {
        Camera.Parameters parameters = camera.getParameters();
        String flashMode;
        /** 是否支持闪光灯 */
        if (newSetting) {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_ON);
        } else {
            flashMode = findSettableValue(parameters.getSupportedFlashModes(), Camera.Parameters.FLASH_MODE_OFF);
        }
        if (flashMode != null) {
            parameters.setFlashMode(flashMode);
        }
        camera.setParameters(parameters);
    }

    /**
     * @param supportedValues
     * @param desiredValues
     * @return
     */
    private static String findSettableValue(Collection<String> supportedValues, String... desiredValues) {
        String result = null;
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    result = desiredValue;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取旋转角度
     *
     * @return
     */
    public int getDisplayOrientation() {
        int result;
        result = SpecialMobileAdaptation.instance.getOrientation(mContext);
        if (result != -1) {
            /**
             * 如果命中特殊机型直接进行返回正确的旋转角度
             */
            return result;
        }
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }


        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 获取预览分辨率
     *
     * @param parameters
     * @param screenResolution
     * @return
     */
    private static Point getPreviewResolution(Camera.Parameters parameters, Point screenResolution) {
        Point previewResolution =
                findBestPreviewSizeValue(parameters.getSupportedPreviewSizes(), screenResolution);
        if (previewResolution == null) {
            previewResolution = new Point((screenResolution.x >> 3) << 3, (screenResolution.y >> 3) << 3);
        }
        return previewResolution;
    }

    /**
     * 获取合适的预览分辨率
     *
     * @param supportSizeList
     * @param screenResolution
     * @return
     */
    private static Point findBestPreviewSizeValue(List<Camera.Size> supportSizeList, Point screenResolution) {
        int bestX = 0;
        int bestY = 0;
        int diff = Integer.MAX_VALUE;
        for (Camera.Size previewSize : supportSizeList) {
            int newX = previewSize.width;
            int newY = previewSize.height;
            //  WriteUtil.writeLog("设备中预览分辨率"+newX+"X"+newY);
            int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
            if (newDiff == 0) {
                bestX = newX;
                bestY = newY;
                break;
            } else if (newDiff < diff) {
                bestX = newX;
                bestY = newY;
                diff = newDiff;
            }
        }
        if (bestX > 0 && bestY > 0) {
            return new Point(bestX, bestY);
        }
        return null;
    }

    /**
     * 获取合适的缩放值
     *
     * @param stringValues
     * @param tenDesiredZoom
     * @return
     */
    private static int findBestMotZoomValue(CharSequence stringValues, int tenDesiredZoom) {
        int tenBestValue = 0;
        for (String stringValue : COMMA_PATTERN.split(stringValues)) {
            stringValue = stringValue.trim();
            double value;
            try {
                value = Double.parseDouble(stringValue);
            } catch (NumberFormatException nfe) {
                return tenDesiredZoom;
            }
            int tenValue = (int) (10.0 * value);
            if (Math.abs(tenDesiredZoom - value) < Math.abs(tenDesiredZoom
                    - tenBestValue)) {
                tenBestValue = tenValue;
            }
        }
        return tenBestValue;
    }

    /**
     * 设置缩放
     *
     * @param parameters
     */
    private void setZoom(Camera.Parameters parameters) {
        String zoomSupportedString = parameters.get("zoom-supported");
        if (zoomSupportedString != null && !Boolean.parseBoolean(zoomSupportedString)) {
            return;
        }
        int tenDesiredZoom = TEN_DESIRED_ZOOM;

        String maxZoomString = parameters.get("max-zoom");
        if (maxZoomString != null) {
            try {
                int tenMaxZoom = (int) (10.0 * Double.parseDouble(maxZoomString));
                if (tenDesiredZoom > tenMaxZoom) {
                    tenDesiredZoom = tenMaxZoom;
                }
            } catch (NumberFormatException nfe) {
            }
        }

        String takingPictureZoomMaxString = parameters.get("taking-picture-zoom-max");
        if (takingPictureZoomMaxString != null) {
            try {
                int tenMaxZoom = Integer.parseInt(takingPictureZoomMaxString);
                if (tenDesiredZoom > tenMaxZoom) {
                    tenDesiredZoom = tenMaxZoom;
                }
            } catch (NumberFormatException nfe) {
            }
        }

        String motZoomValuesString = parameters.get("mot-zoom-values");
        if (motZoomValuesString != null) {
            tenDesiredZoom = findBestMotZoomValue(motZoomValuesString, tenDesiredZoom);
        }

        String motZoomStepString = parameters.get("mot-zoom-step");
        if (motZoomStepString != null) {
            try {
                double motZoomStep = Double.parseDouble(motZoomStepString.trim());
                int tenZoomStep = (int) (10.0 * motZoomStep);
                if (tenZoomStep > 1) {
                    tenDesiredZoom -= tenDesiredZoom % tenZoomStep;
                }
            } catch (NumberFormatException nfe) {
                // continue
            }
        }
        if (maxZoomString != null || motZoomValuesString != null) {
            parameters.set("zoom", String.valueOf(tenDesiredZoom / 10.0));
        }
        if (takingPictureZoomMaxString != null) {
            parameters.set("taking-picture-zoom", tenDesiredZoom);
        }
    }

}