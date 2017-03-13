package cc.androidios.glideimageshape;

import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import cc.androidios.shape.GlideArcTransform;
import cc.androidios.shape.GlideCircleTransform;
import cc.androidios.shape.GlideOvalTransform;
import cc.androidios.shape.GlideRoundCropTransform;
import cc.androidios.shape.GlideShapeTransform;

public class MainActivity extends AppCompatActivity {

    private static final String url = "http://yezhou.me/images/zhanghanyun.jpg";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        imageView = (ImageView) this.findViewById(R.id.image);
    }

    public void normalShape(View view) {
        Glide.with(this).load(url).into(imageView);
    }

    public void roundCropShape(View view) {
        Glide.with(this).load(url).transform(new GlideRoundCropTransform(this, 20)).into(imageView);
    }

    public void circleCropShape(View view) {
        Glide.with(this).load(url).transform(new GlideCircleTransform(this)).into(imageView);
    }

    public void circleScaleShape(View view) {
        Glide.with(this).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        });
    }

    public void ovalShape(View view) {
        Glide.with(this).load(url).transform(new GlideOvalTransform(this)).into(imageView);
    }

    public void arcShape(View view) {
        Glide.with(this).load(url).transform(new GlideArcTransform(this)).into(imageView);
    }

    public void pngShape(View view) {
        Glide.with(this).load(url).transform(new GlideShapeTransform(this, R.drawable.shape_cloud)).into(imageView);
    }

    public void xmlShape(View view) {
        Glide.with(this).load(url).transform(new GlideShapeTransform(this, R.drawable.ic_favorite_black_24dp)).into(imageView);
    }
}
