package com.fih.idx.deriklibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Bitmap关于内存使用优化
 * Created by derik on 17-2-18.
 */

public class BitmapTools {

    // 软引用
    public Map<String, SoftReference<Bitmap>> imageCacheSof;
    // LruCache
    public LruCache<String, Bitmap> imageCacheLru;

    public BitmapTools(){
        imageCacheSof = new HashMap<>();
        int maxMemory = getMaxMemory(); // kb
        int maxSize = maxMemory / 10; // 十分之一作缓存
        imageCacheLru = new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap){
                return (bitmap.getRowBytes() * bitmap.getHeight()) / 1024; //单位 kb
            }
        };

    }

    // KB
    public int getMaxMemory(){
        int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        Log.d("TAG", "Max memory is " + maxMemory + "KB");
        return maxMemory;
    }

    // 图片缩小技术处理之一：先依据组件宽高，计算出缩小比例
    public int calculateInSampleSize(Context ctx, int resId, int reqWidth, int reqHeight){
        BitmapFactory.Options  options = new BitmapFactory.Options();
        // 将这个参数的inJustDecodeBounds属性设置为true就可以让解析方法禁止为bitmap分配内存，
        // 返回值也不再是一个Bitmap对象，而是null。虽然Bitmap是null了，但是
        // BitmapFactory.Options类对象的outWidth、outHeight和outMimeType属性都会被赋值
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(ctx.getResources(), resId, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int inSampleSize = 1;
        if (imageHeight > reqHeight || imageWidth > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) imageHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) imageWidth / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        Log.d("inSampleSize", ""+inSampleSize);
        return inSampleSize;
    }

    public int calculateInSampleSize(Context ctx, InputStream is, int reqWidth, int reqHeight){
        BitmapFactory.Options  options = new BitmapFactory.Options();
        // 将这个参数的inJustDecodeBounds属性设置为true就可以让解析方法禁止为bitmap分配内存，
        // 返回值也不再是一个Bitmap对象，而是null。虽然Bitmap是null了，但是
        // BitmapFactory.Options类对象的outWidth、outHeight和outMimeType属性都会被赋值
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int inSampleSize = 1;
        if (imageHeight > reqHeight || imageWidth > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) imageHeight / (float) reqHeight);
            final int widthRatio = Math.round((float) imageWidth / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        Log.d("inSampleSize", ""+inSampleSize);
        return inSampleSize;
    }

    /**
     * 使Bitmap缩小为原来的inSampleSize分之一，降低使用内存
     *
     * @param context
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public Bitmap inSampleSize(Context context, int resId, Bitmap.Config config, int reqWidth, int reqHeight) {
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // 默认false
        if (config != null) {
            options.inPreferredConfig = Bitmap.Config.RGB_565; //降低像素使用内存
            options.inPurgeable = true; //允许系统在必要时清除，避免在UI响应要求快的地方使用
            options.inInputShareable = true; //允许保存输入数据的引用，以重建bitmap
        }

        if (reqHeight != 0 && reqWidth != 0 ){
            options.inSampleSize = calculateInSampleSize(context, resId, reqWidth, reqHeight);
        }

        try {
            //解析并分配内存给bitmap对象
            bmp = BitmapFactory.decodeResource(context.getResources(), resId, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return bmp;
    }

    public Bitmap inSampleSize(Context context, InputStream is, Bitmap.Config config, int reqWidth, int reqHeight) {
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = false; // 默认false

        if (config != null){
            options.inPreferredConfig = Bitmap.Config.RGB_565; //降低像素使用内存
            options.inPurgeable = true; //允许系统在必要时清除，避免在UI响应要求快的地方使用
            options.inInputShareable = true; //允许保存输入数据的引用，以重建bitmap
        }

        if (reqHeight != 0 && reqWidth != 0 ){
            options.inSampleSize = calculateInSampleSize(context, is, reqWidth, reqHeight);
        }

        try {
            //解析并分配内存给bitmap对象
            bmp = BitmapFactory.decodeStream(is, null, options);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return bmp;
    }

    /**
     * 功能描述：使Bitmap从默认的ARGB-8888转为RGB-565，降低内存使用
     * ALPHA_8：每个像素占用1byte内存
     * ARGB_4444：每个像素占用2byte内存
     * ARGB_8888：每个像素占用4byte内存 （默认）
     * RGB_565：每个像素占用2byte内存
     *
     * @param context
     * @param resId
     * @return
     */
    public Bitmap readBitMap(Context context, int resId) {
        Bitmap bitmap = null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true; //允许系统在必要时清除，避免在UI响应要求快的地方使用
        opt.inInputShareable = true; //允许保存输入数据的引用，以重建bitmap

        //获取资源图片
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId, opt);

        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }

        return bitmap;
    }


    /**
     * 默认ARGB-8888创建Bitmap，创建Bitmap时，需要捕获内存溢出的异常
     * @param ctx
     * @param resId
     * @return
     */
    public Bitmap getBitmap(Context ctx, int resId) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(ctx.getResources(), resId);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return bitmap;
    }


    //使用LruCache作缓存之取
    public Bitmap getBitmap(String url){
        return imageCacheLru.get(url);
    }

    //使用LruCache作缓存之存
    public void putBitmap(String url, Bitmap bitmap){
        imageCacheLru.put(url, bitmap);
    }



    // 软引用作缓存之存
    public void putBitmapToCache(String path) {
        try{
            // 强引用的Bitmap对象
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            // 软引用的Bitmap对象
            SoftReference<Bitmap> softBitmap = new SoftReference<>(bitmap);
            // 添加该对象到Map中使其缓存
            imageCacheSof.put(path, softBitmap);
        } catch (OutOfMemoryError error){
            error.printStackTrace();
        }

    }

    // 软引用作缓存之取
    public Bitmap getBitmapByPath(String path) {
        // 从hashMap中取出软引用
        SoftReference<Bitmap> softReference = imageCacheSof.get(path);
        // 判断是否存在软引用
        if (softReference == null) {
            return null;
        }
        // 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空
        Bitmap bitmap = softReference.get();
        return bitmap;
    }

    /**
     * 使用完后复制使用此段代码，recycle bitmap
     */
    public void recycle(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;

        }
//        System.gc();

    }

}
