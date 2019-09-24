package org.smartregister.cbhc.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.cbhc.R;
import org.smartregister.cbhc.util.ImageUtils;
import org.smartregister.cbhc.util.Utils;
import org.smartregister.domain.Photo;
import org.smartregister.util.OpenSRPImageLoader;
import org.smartregister.view.activity.DrishtiApplication;

/**
 * Created by ndegwamartin on 13/07/2018.
 */
public class ImageRenderHelper {

    private static final String TAG = ImageRenderHelper.class.getCanonicalName();

    private Context context;

    public ImageRenderHelper(Context context) {
        this.context = context;
    }

    public void refreshProfileImage(String clientBaseEntityId, ImageView profileImageView) {

        Photo photo = ImageUtils.profilePhotoByClientID(clientBaseEntityId);

        if (StringUtils.isNotBlank(photo.getFilePath())) {
            try {
                Bitmap myBitmap = BitmapFactory.decodeFile(photo.getFilePath());
                profileImageView.setImageBitmap(myBitmap);
            } catch (Exception e) {
                Utils.appendLog(getClass().getName(), e);
                Log.e(TAG, e.getMessage());

                int backgroundResource = R.drawable.ic_woman_with_baby;
                profileImageView.setImageDrawable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(backgroundResource) : ContextCompat.getDrawable(context, backgroundResource));

            }
        } else {
            int backgroundResource = photo.getResourceId();
            profileImageView.setImageDrawable(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? context.getDrawable(backgroundResource) : ContextCompat.getDrawable(context, backgroundResource));


        }
        profileImageView.setTag(org.smartregister.R.id.entity_id, clientBaseEntityId);
        DrishtiApplication.getCachedImageLoaderInstance().getImageByClientId(clientBaseEntityId, OpenSRPImageLoader.getStaticImageListener(profileImageView, 0, 0));

    }
}
