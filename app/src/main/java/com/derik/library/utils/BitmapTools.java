package com.derik.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bitmap关于内存使用优化
 * Created by derik on 17-2-18.
 */

public class BitmapTools {
    private static final String TAG = "BitmapTools";
    private static volatile BitmapTools INSTANCE;
    /**
     * 弱引用作缓存
     */
    private Map<String, WeakReference<Bitmap>> mImageCacheWeak;
    /**
     * LruCache作缓存
     */
    private LruCache<String, Bitmap> mImageCacheLru;

    /**
     * 进行文件存储使用的线程池，线程数量
     */
    private static final int mThreadNum = 2;
    private ExecutorService mFixedThreadExecutor;

    private BitmapTools() {

    }

    public static BitmapTools getInstance() {
        if (INSTANCE == null) {
            synchronized (BitmapTools.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BitmapTools();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取一个进程允许的最大内存
     *
     * @return int 单位KB
     */
    public int getMaxMemory() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.d("TAG", "Max memory is " + maxMemory + "KB");
        return maxMemory;
    }

    /**
     * 图片缩小技术处理之一：先依据组件宽高，计算出缩小比例
     *
     * @param ctx       上下文
     * @param resId     图片的资源ID
     * @param reqWidth  指定宽度
     * @param reqHeight 指定高度
     * @return int 缩小的比例
     */
    private int calculateInSampleSize(Context ctx, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 将这个参数的inJustDecodeBounds属性设置为true时，可以让解析方法禁止为bitmap分配内存，返回值也
        // 不再是一个Bitmap对象，而是null。虽然Bitmap是null了，但是BitmapFactory.Options类
        // 对象的outWidth、outHeight和outMimeType属性都会被赋值
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
        Log.d("inSampleSize", "" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 图片缩小技术处理之一：先依据组件宽高，计算出缩小比例
     *
     * @param ctx       上下文
     * @param is        图片的数据流
     * @param reqWidth  指定宽度
     * @param reqHeight 指定高度
     * @return int 缩小的比例
     */
    private int calculateInSampleSize(Context ctx, InputStream is, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
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
        Log.d("inSampleSize", "" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 使Bitmap缩小为原来的inSampleSize分之一，降低使用内存
     *
     * @param context   上下文
     * @param resId     图片的资源ID
     * @param config    对应的配置
     * @param reqWidth  设定的宽度
     * @param reqHeight 设定的高度
     * @return Bitmap
     */
    public Bitmap inSampleSize(Context context, int resId, Bitmap.Config config, int reqWidth, int reqHeight) {
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; // 默认值为false
        if (config != null) {
            options.inPreferredConfig = Bitmap.Config.RGB_565; //降低像素使用内存
            options.inPurgeable = true; //允许系统在必要时清除，避免在UI响应要求快的地方使用
            options.inInputShareable = true; //允许保存输入数据的引用，以重建bitmap
        }

        if (reqHeight != 0 && reqWidth != 0) {
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

    /**
     * 使Bitmap缩小为原来的inSampleSize分之一，降低使用内存
     *
     * @param context   上下文
     * @param is        图片的数据流
     * @param config    对应的配置
     * @param reqWidth  设定的宽度
     * @param reqHeight 设定的高度
     * @return Bitmap 返回一个bitmap实例
     */
    public Bitmap inSampleSize(Context context, InputStream is, Bitmap.Config config, int reqWidth, int reqHeight) {
        Bitmap bmp = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = false; // 默认false

        if (config != null) {
            options.inPreferredConfig = Bitmap.Config.RGB_565; //降低像素使用内存
            options.inPurgeable = true; //允许系统在必要时清除，避免在UI响应要求快的地方使用
            options.inInputShareable = true; //允许保存输入数据的引用，以重建bitmap
        }

        if (reqHeight != 0 && reqWidth != 0) {
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
     * Creates a centered bitmap of the desired size.
     *
     * @param source original bitmap source
     * @param width  targeted width
     * @param height targeted height
     * @return Bitmap
     */
    public Bitmap extractThumbnail(Bitmap source, int width, int height) {
        // 指定较小，则缩小；较大则放大。其实质结合了BitmapFactory和Matrix
        return ThumbnailUtils.extractThumbnail(source, width, height);
    }

    /**
     * 裁剪Bitmap
     *
     * @param bitmap The bitmap we are subsetting
     * @param x      The x coordinate of the first pixel in source
     * @param y      The y coordinate of the first pixel in source
     * @param width  The number of pixels in each row
     * @param height The number of rows
     * @return A bitmap that represents the specified subset of source
     */
    public Bitmap bitmapCrop(Bitmap bitmap, int x, int y, int width, int height) {
        try {
            if (bitmap != null) {
                return Bitmap.createBitmap(bitmap, x, y, width, height, null, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 功能描述：使Bitmap从默认的ARGB-8888转为RGB-565，降低内存使用
     * ALPHA_8：每个像素占用1byte内存
     * ARGB_4444：每个像素占用2byte内存
     * ARGB_8888：每个像素占用4byte内存 （默认）
     * RGB_565：每个像素占用2byte内存
     *
     * @param context 上下文
     * @param resId   图片的资源ID
     * @return Bitmap 返回一个bitmap实例
     */
    public Bitmap getBitmap565(Context context, int resId) {
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
     *
     * @param ctx   上下文
     * @param resId 图片的资源ID
     * @return Bitmap 返回一个Bitmap实例
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

    /**
     * 文件中获取一个Bitmap实例
     *
     * @param pathName 图片文件的路径
     * @return Bitmap
     */
    public Bitmap getBitmapFromFile(String pathName) {
        if (pathName == null || pathName.equals("")) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 文件存储
     *
     * @param pathName 保存文件路径名
     * @param bitmap   待存储的图片
     */
    public synchronized void saveBitmapToFile(final String pathName, final Bitmap bitmap) {
        if (mFixedThreadExecutor == null) {
            mFixedThreadExecutor = Executors.newFixedThreadPool(mThreadNum);
        }
        mFixedThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (bitmap != null) {
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(new File(pathName));
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();
                        fos = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (fos != null) {
                            try {
                                fos.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 使用LruCache作缓存之存
     *
     * @param url    key
     * @param bitmap value
     */
    public void putBitmapToLRUCache(String url, Bitmap bitmap) {
        if (url == null || bitmap == null) {
            throw new IllegalArgumentException("Url and bitmap can not be null.");
        }
        if (mImageCacheLru == null) {
            int maxMemory = getMaxMemory();
            // 十分之一作缓存
            int maxSize = maxMemory / 10;
            mImageCacheLru = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    //单位 kb
                    return getBitmapSize(bitmap) / 1024;
                }
            };
        }
        mImageCacheLru.put(url, bitmap);
    }

    /**
     * 计算一张Bitmap的字节数
     *
     * @param bitmap bitmap对象
     * @return int 字节数
     */
    public int getBitmapSize(Bitmap bitmap) {
        int size;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            size = bitmap.getAllocationByteCount();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            size = bitmap.getByteCount();
        } else {
            size = bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
        }
        return size;
    }

    /**
     * 使用LruCache作缓存之取
     *
     * @param url key，用来获取value
     * @return Bitmap 可能为null
     */
    public Bitmap getBitmapFromLRUCache(String url) {
        return mImageCacheLru != null ? mImageCacheLru.get(url) : null;
    }

    /**
     * 弱引用作缓存之存
     *
     * @param key    弱引用对象将会存在一个HashMap中，key为对应的键。
     * @param bitmap 待存储图片
     */
    public void putBitmapToWeakRef(String key, Bitmap bitmap) {
        try {
            if (mImageCacheWeak == null) {
                mImageCacheWeak = new HashMap<>();
            }
            // 一个Bitmap的弱引用
            WeakReference<Bitmap> weakBitmap = new WeakReference<>(bitmap);
            // 添加弱引用到Map中
            mImageCacheWeak.put(key, weakBitmap);
        } catch (OutOfMemoryError error) {
            error.printStackTrace();
        }
    }

    /**
     * 弱引用作缓存之取
     *
     * @param key 键
     * @return Bitmap 可能为null
     */
    public Bitmap getBitmapFromWeakRef(String key) {
        if (mImageCacheWeak == null) {
            return null;
        }
        // 从hashMap中取出弱引用
        WeakReference<Bitmap> weakReference = mImageCacheWeak.get(key);
        // 判断是否存在弱引用
        if (weakReference == null) {
            return null;
        }
        // 取出Bitmap对象，如果由于内存不足Bitmap被回收，将取得空
        return weakReference.get();
    }

    /**
     * 使用完后复制使用此段代码，recycle bitmap
     *
     * @param bitmap 待回收照片
     */
    public void recycle(Bitmap bitmap) {
        // 先判断是否已经回收
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收，并且置为null
            bitmap.recycle();
            bitmap = null;
        }

    }

    /**
     * 销毁单实例对象
     */
    public void destroy() {
        if (mImageCacheWeak != null) {
            mImageCacheWeak.clear();
            mImageCacheWeak = null;
        }

        if (mImageCacheLru != null) {
            mImageCacheLru = null;
        }

        if (mFixedThreadExecutor != null && !mFixedThreadExecutor.isShutdown()) {
            mFixedThreadExecutor.shutdown();
            mFixedThreadExecutor = null;
        }

        if (INSTANCE != null) {
            INSTANCE = null;
        }
    }

}
