package mehagarg.android.gifygallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import mehagarg.android.gifygallery.model.GalleryItem;

/**
 * Created by meha on 4/27/16.
 */
public class DetailActivity extends SingleFragmentActivity {
    private static final String EXTRA_ITEM = "extra_item";

    public static Intent newIntent(Context context, GalleryItem item) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra(EXTRA_ITEM, item);
        return i;
    }

    @Override
    protected Fragment createFragment() {

        GalleryItem item = (GalleryItem) getIntent().getSerializableExtra(EXTRA_ITEM);
        return DetailFragment.newInstance(item);

    }
}
