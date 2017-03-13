package cc.androidios.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import cc.androidios.glideimageshape.R;

/**
 * Created by yezhou on 2017/3/12.
 */

public class GlideShapeTransform extends BitmapTransformation {

    private Context mContext;
    private int mShapeResId; // 形状的drawable资源
    private static Paint mPaint;

    public GlideShapeTransform(Context context, int shapeResId) {
        super(context);
        this.mContext = context;
        this.mShapeResId = shapeResId;
        // 实例化Paint对象，并设置Xfermode为SRC_IN
        this.mPaint = new Paint();
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return shapeCrop(pool, toTransform);
    }

    private Bitmap shapeCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        // 获取到形状资源的Drawable对象
        Drawable shape = ContextCompat.getDrawable(mContext, mShapeResId);
        float shapeWidth = shape.getMinimumWidth(); // 形状的宽
        float shapeHeight = shape.getMinimumHeight(); // 形状的高

        int width = source.getWidth(); // 图片的宽
        int height = source.getHeight(); // 图片的高

        if (width > height) {
            // 如果图片的宽大于高，则以高为基准，以形状的宽高比重新设置宽度
            width = (int) (height * (shapeWidth / shapeHeight));
        } else {
            // 如果图片的宽小于等于高，则以宽为基准，以形状的宽高比重新设置高度度
            height = (int) (width * (shapeHeight / shapeWidth));
        }

        // 居中裁剪图片，调用Glide库中TransformationUtils类的centerCrop()方法完成裁剪，保证图片居中且填满
        final Bitmap toReuse = pool.get(width, height, source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888);
        Bitmap transformed = TransformationUtils.centerCrop(toReuse, source, width, height);
        if (toReuse != null && toReuse != transformed && !pool.put(toReuse)) {
            toReuse.recycle();
        }

        // 根据算出的宽高新建Bitmap对象并设置到画布上
        Bitmap result = pool.get(width, height, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        // 设置形状的大小与图片的大小一致
        shape.setBounds(0, 0, width, height);
        mPaint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        mPaint.setAntiAlias(true);
        // 将图片画到画布上
        shape.draw(canvas);
        // 将裁剪后的图片画得画布上
        canvas.drawBitmap(transformed, 0, 0, mPaint);
        return result;
    }

    @Override
    public String getId() {
        // 识别缓存
        return getClass().getName() + mShapeResId;
    }
}
