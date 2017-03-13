package cc.androidios.shape;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by yezhou on 2017/3/12.
 */

public class GlideArcTransform extends BitmapTransformation {

    private float startAngle;
    private float sweepAngle;

    public GlideArcTransform(Context context) {
        super(context);
        startAngle = 195;
        sweepAngle = 150;
    }

    public GlideArcTransform(Context context, float startAngle, float sweepAngle) {
        super(context);
        this.startAngle = startAngle;
        this.sweepAngle = sweepAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return arcCrop(pool, toTransform);
    }

    private Bitmap arcCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight() * 2);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
        return result;
    }

    @Override
    public String getId() {
        // 识别缓存
        return getClass().getName() + startAngle + sweepAngle;
    }
}
