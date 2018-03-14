package hefu.robotphone.uilibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import hefu.robotphone.uilibrary.R;


/**
 * Created by sunxx on 2016/5/30.
 */
public class GlideUtils {
    private static Context mContext;

    public static void init(Context initContext) {
        mContext = initContext;
    }



    public static void loadCircular(final ImageView img, String url) {
        Glide.with(mContext)
                .load(url)
                .asBitmap()
                .error(R.drawable.cartoon_head)
                .into(new BitmapImageViewTarget(img) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(
                                        mContext.getResources(),
                                        resource);
                        circularBitmapDrawable.setCircular(true);
                        img.setImageDrawable(circularBitmapDrawable);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(
                                        mContext.getResources(),
                                        ((BitmapDrawable) errorDrawable).getBitmap());
                        circularBitmapDrawable.setCircular(true);
                        img.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    ;

}
