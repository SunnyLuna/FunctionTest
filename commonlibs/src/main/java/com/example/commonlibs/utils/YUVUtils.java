package com.example.commonlibs.utils;

/**
 * YUV工具类
 * 处理Camera原始YUV数据的旋转，转化
 *
 * @author ZJ
 * created at 2019/11/25 17:54
 */
public class YUVUtils {

    /**
     * 旋转90
     *
     * @return
     */

    public static byte[] rotateYUVDegree90(byte[] src, int width, int height) {
        byte[] dest = new byte[src.length];
        int index = 0;
        for (int i = 0; i < width; i++) {
            for (int j = height - 1; j >= 0; j--) {
                dest[index++] = src[j * width + i];
            }
        }
        int uHeader = index;
        int uHeight = height / 2;
        for (int i = 0; i < width; i += 2) {
            for (int j = uHeight - 1; j >= 0; j--) {
                dest[index++] = src[uHeader + j * width + i];
                dest[index++] = src[uHeader + j * width + i + 1];
            }
        }
        return dest;
    }

    /**
     * 旋转270
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUVDegree270(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }// Rotate the U and V color components
        i = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }

    /**
     * 旋转270+镜像
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUVDegree270AndMirror(byte[] data, int imageWidth, int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate and mirror the Y luma
        int i = 0;
        int maxY = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            maxY = imageWidth * (imageHeight - 1) + x * 2;
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[maxY - (y * imageWidth + x)];
                i++;
            }
        }
        // Rotate and mirror the U and V color components
        int uvSize = imageWidth * imageHeight;
        i = uvSize;
        int maxUV = 0;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            maxUV = imageWidth * (imageHeight / 2 - 1) + x * 2 + uvSize;
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[maxUV - 2 - (y * imageWidth + x - 1)];
                i++;
                yuv[i] = data[maxUV - (y * imageWidth + x)];
                i++;
            }
        }
        return yuv;
    }


    /**
     * 旋转180
     *
     * @param data
     * @param imageWidth
     * @param imageHeight
     * @return
     */
    public static byte[] rotateYUVDegree180(byte[] data, int imageWidth, int imageHeight) {

        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        int i = 0;
        int count = 0;
        for (i = imageWidth * imageHeight - 1; i >= 0; i--) {
            yuv[count] = data[i];
            count++;
        }
        i = imageWidth * imageHeight * 3 / 2 - 1;
        for (i = imageWidth * imageHeight * 3 / 2 - 1; i >= imageWidth * imageHeight; i -= 2) {
            yuv[count++] = data[i - 1];
            yuv[count++] = data[i];
        }
        return yuv;
    }


    /***
     * YUV420 转化成 RGB
     */
    public static int[] decodeYUV420SP(byte[] yuv420sp, int width, int height) {
        final int frameSize = width * height;
        int rgb[] = new int[frameSize];
        for (int j = 0, yp = 0; j < height; j++) {
            int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }
                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);
                if (r < 0)
                    r = 0;
                else if (r > 262143)
                    r = 262143;
                if (g < 0)
                    g = 0;
                else if (g > 262143)
                    g = 262143;
                if (b < 0)
                    b = 0;
                else if (b > 262143)
                    b = 262143;
                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000)
                        | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
        return rgb;
    }


    public static byte[] mirror(byte[] datas, int width, int height) {
        byte[] yuvs = new byte[width * height * 3 / 2];
        int size = width * height * 3 / 2;
        int y = 0;
        //Y
        for (int i = 0; i < height; i++) {
            for (int j = width - 1; j >= 0; j--) {
                yuvs[y++] = datas[size + (i * width + j)];
            }
        }
        int u = size + width * height;
        for (int i = 0; i < height / 2; i++) {
            for (int j = width / 2 - 1; j >= 0; j--) {
                yuvs[y++] = datas[u + (i * width / 2 + j)];
            }
        }
        int v = u + width * height / 4;
        for (int i = height / 2 - 1; i >= 0; i--) {
            for (int j = width / 2 - 1; j >= 0; j--) {
                yuvs[y++] = datas[v + (i * width / 2 + j)];
            }
        }
        return yuvs;

    }


    /**
     * 镜像处理
     *
     * @param src
     * @param w
     * @param h
     */
    public static void Mirror(byte[] src, int w, int h) { //src是原始yuv数组
        int i;
        int index;
        byte temp;
        int a, b;
        //mirror y
        for (i = 0; i < h; i++) {
            a = i * w;
            b = (i + 1) * w - 1;
            while (a < b) {
                temp = src[a];
                src[a] = src[b];
                src[b] = temp;
                a++;
                b--;
            }
        }

        // mirror u and v
        index = w * h;
        for (i = 0; i < h / 2; i++) {
            a = i * w;
            b = (i + 1) * w - 2;
            while (a < b) {
                temp = src[a + index];
                src[a + index] = src[b + index];
                src[b + index] = temp;

                temp = src[a + index + 1];
                src[a + index + 1] = src[b + index + 1];
                src[b + index + 1] = temp;
                a += 2;
                b -= 2;
            }
        }
    }

    public static byte[] NV21_mirror(byte[] nv21_data, int width, int height) {
        int i;
        int left, right;
        byte temp;
        int startPos = 0; // mirror Y 　
        for (i = 0; i < height; i++) {
            left = startPos;
            right = startPos + width - 1;
            while (left < right) {
                temp = nv21_data[left];
                nv21_data[left] = nv21_data[right];
                nv21_data[right] = temp;
                left++;
                right--;
            }
            startPos += width;
        } // mirror U and V 　　
        int offset = width * height;
        startPos = 0;
        for (i = 0; i < height / 2; i++) {
            left = offset + startPos;
            right = offset + startPos + width - 2;
            while (left < right) {
                temp = nv21_data[left];
                nv21_data[left] = nv21_data[right];
                nv21_data[right] = temp;
                left++;
                right--;
                temp = nv21_data[left];
                nv21_data[left] = nv21_data[right];
                nv21_data[right] = temp;
                left++;
                right--;
            }
            startPos += width;
        }
        return nv21_data;
    }

    public static void NV21toI420SemiPlanar(byte[] nv21bytes, byte[] i420bytes, int width, int height) {
        System.arraycopy(nv21bytes, 0, i420bytes, 0, width * height);
        for (int i = width * height; i < nv21bytes.length; i += 2) {
            i420bytes[i] = nv21bytes[i + 1];
            i420bytes[i + 1] = nv21bytes[i];
        }
    }

}
