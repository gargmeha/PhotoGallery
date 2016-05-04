package mehagarg.android.gifygallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import mehagarg.android.gifygallery.model.GalleryItem;
import mehagarg.android.gifygallery.receiver.VisibleFragment;

/**
 * Created by meha on 4/25/16.
 */
public class DetailFragment extends VisibleFragment {
    public static final String GALLERYITEM = "galleryItem";
    GalleryItem item;
    private ImageView imageView;
    private TextView textView;

    public static DetailFragment newInstance(GalleryItem item) {
        Bundle args = new Bundle();
        args.putParcelable(GALLERYITEM, item);
        DetailFragment fragment = new DetailFragment();
        fragment.item = item;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_view, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageItemDetail);
        textView = (TextView) v.findViewById(R.id.captionDetail);
//        item = getArguments().getParcelable(GALLERYITEM);
        if (item != null) {


            Glide.with(getActivity())
                    .load(item.getUrl())
                    .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                            super.onResourceReady(resource, animation);
                            //check isRefreshing
                        }
                    });

            textView.setText(item.getCaption().toString());
        }
        return v;
    }



}
