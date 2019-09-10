package com.decard.zj.founctiontest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ZhaoWeiCameraUtils {


    private String mCameraId = "0";//摄像头id（通常0代表后置摄像头，1代表前置摄像头）
    private CameraCharacteristics mCharacteristics;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private ImageReader mPreviewImageReader;
    private ImageReader mPhotoImageReader;

    private Size previewSize;

    private TextureView textureView;
    private Context mContext;
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread backgroundThread;

    /**
     * A [Handler] for running tasks in the background.
     */
    private Handler backgroundHandler;


    private int sensorOrientation = 0;
    private int MAX_PREVIEW_WIDTH = 1920;
    private int MAX_PREVIEW_HEIGHT = 1080;
    private byte[] mdatas;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }


    /**
     * 打开摄像头
     */
    public void startCamera(TextureView tv, Context context) {
        startBackgroundThread();
        this.mContext = context;
        textureView = tv;
        if (tv.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            tv.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }


    /**
     * 打开摄像头
     */
    @SuppressLint("MissingPermission")
    private void openCamera(int width, int height) {
        CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
        //设置摄像头特性
        setCameraCharacteristics(manager, width, height);
        try {
            manager.openCamera(mCameraId, mStateCallback, backgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置摄像头的参数
     */
    private void setCameraCharacteristics(CameraManager manager, int width, int height) {
        try {
            // 获取指定摄像头的特性
            mCharacteristics = manager.getCameraCharacteristics(mCameraId);
            // 获取摄像头支持的配置属性
            StreamConfigurationMap map = mCharacteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            // 获取摄像头支持的最大尺寸
            Size largestPreview = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.YUV_420_888)),
                    new CompareSizesByArea());
            // 创建一个ImageReader对象，用于获取摄像头的图像数据
            mPreviewImageReader = ImageReader.newInstance(largestPreview.getWidth(), largestPreview.getHeight(),
                    ImageFormat.YUV_420_888, 1);
            //设置获取图片的监听
            mPreviewImageReader.
                    setOnImageAvailableListener(previewAvailableListener, backgroundHandler);

            Size largestPhoto = Collections.max(
                    Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());
            // 创建一个ImageReader对象，用于获取摄像头的图像数据
            mPhotoImageReader = ImageReader.newInstance(largestPhoto.getWidth(), largestPhoto.getHeight(),
                    ImageFormat.JPEG, 2);
            //设置获取图片的监听
            mPhotoImageReader.
                    setOnImageAvailableListener(previewAvailableListener, backgroundHandler);

            // Find out if we need to swap dimension to get the preview size relative to sensor
            // coordinate.
            int displayRotation = ((WindowManager) mContext
                    .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();

            sensorOrientation = mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            boolean swappedDimensions = areDimensionsSwapped(displayRotation);

            Point displaySize = new Point();
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getSize(displaySize);
            int rotatedPreviewWidth = width;
            int rotatedPreviewHeight = height;
            int maxPreviewWidth = displaySize.x;
            int maxPreviewHeight = displaySize.y;

            if (swappedDimensions) {
                rotatedPreviewWidth = height;
                rotatedPreviewHeight = width;
                maxPreviewWidth = displaySize.y;
                maxPreviewHeight = displaySize.x;
            }

            if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                maxPreviewWidth = MAX_PREVIEW_WIDTH;
            }

            if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                maxPreviewHeight = MAX_PREVIEW_HEIGHT;
            }

            // 获取最佳的预览尺寸
            previewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    rotatedPreviewWidth, rotatedPreviewHeight,
                    maxPreviewWidth, maxPreviewHeight, largestPhoto);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Determines if the dimensions are swapped given the phone's current rotation.
     *
     * @param displayRotation The current rotation of the display
     * @return true if the dimensions are swapped, false otherwise.
     */
    private boolean areDimensionsSwapped(int displayRotation) {
        boolean swappedDimensions = false;
        switch (displayRotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    swappedDimensions = true;
                }

            case Surface.ROTATION_90:
            case Surface.ROTATION_270: {
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    swappedDimensions = true;
                }
            }
            default: {
                Log.e(TAG, "Display rotation is invalid: $displayRotation");
            }
        }
        return swappedDimensions;
    }

    /**
     * 选择最佳的预览尺寸
     *
     * @param choices
     * @param aspectRatio
     * @return
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth,
                                          int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
                    option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth &&
                        option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }

        // Pick the smallest of those big enough. If there is no one big enough, pick the
        // largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }


    // 为Size定义一个比较器Comparator

    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }

    /**
     * TextureView的监听
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            closeCamera();
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }

    };


    /**
     * 摄像头状态的监听
     */

    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            // 开始预览
            takePreview();
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(CameraDevice cameraDevice, int error) {
            cameraDevice.close();
        }

    };

    /**
     * 监听预览数据的方法
     */

    private final ImageReader.OnImageAvailableListener previewAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            // 当照片数据可用时激发该方法
            // 获取捕获的照片数据
            Image image = reader.acquireLatestImage();
            if (image.getFormat() == ImageFormat.JPEG) {

            } else {
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                mdatas = data;
                Log.d("-----------", mdatas.toString());
                image.close();
            }
        }

    };

    /**
     * 开始预览
     */
    private void takePreview() {

        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
        //设置TextureView的缓冲区大小
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
        //获取Surface显示预览数据
        Surface mSurface = new Surface(surfaceTexture);
        try {
            //创建预览请求
            CaptureRequest.Builder mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            //设置Surface作为预览数据的显示界面
            mCaptureRequestBuilder.addTarget(mSurface);
            mCaptureRequestBuilder.addTarget(mPreviewImageReader.getSurface());
            //创建相机捕获会话，第一个参数是捕获数据的输出Surface列表，第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice.createCaptureSession(Arrays.asList(mSurface, mPreviewImageReader.
                    getSurface(), mPhotoImageReader.getSurface()), new
                    CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            try {
                                //开始预览
                                mCameraCaptureSession = session;
                                // 设置自动对焦模式
                                mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
                                mCameraCaptureSession.
                                        setRepeatingRequest(mCaptureRequestBuilder.build(), mCaptureCallBack, backgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                            Log.d("---------", "开启预览会话失败");
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private CameraCaptureSession.CaptureCallback mCaptureCallBack = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
            super.onCaptureCompleted(session, request, result);
        }

        @Override
        public void onCaptureFailed(CameraCaptureSession session, CaptureRequest request, CaptureFailure failure) {
            super.onCaptureFailed(session, request, failure);
            Log.d("---------", "开启预览失败");
        }
    };

    /**
     * 停止拍照释放资源
     */
    private void closeCamera() {
        stopBackgroundThread();
        if (null != mCameraCaptureSession) {
            mCameraCaptureSession.close();
            mCameraCaptureSession = null;
        }
        if (null != mCameraDevice) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        if (null != mPhotoImageReader) {
            mPhotoImageReader.close();
            mPhotoImageReader = null;
        }
        if (null != mPreviewImageReader) {
            mPreviewImageReader.close();
            mPreviewImageReader = null;
        }

    }


    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("CameraBackground");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(TAG, e.toString());
        }
    }

}
