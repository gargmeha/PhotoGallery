package mehagarg.android.gifygallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import mehagarg.android.gifygallery.model.GalleryItem;


public class PhotoGalleryActivity extends SingleFragmentActivity implements PhotoGalleryFragment.Callbacks{

    public static Intent newIntent(Context context) {
        return new Intent(context, PhotoGalleryActivity.class);
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_master_detail;
    }

    @Override
    public void onImageSelected(GalleryItem item) {
        if (findViewById(R.id.fragment_container_detail) == null) {
            Intent intent = DetailActivity.newIntent(this, item);
            startActivity(intent);
        } else {
            Fragment newDetail = DetailFragment.newInstance(item);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail, newDetail).commit();
        }
    }
}
