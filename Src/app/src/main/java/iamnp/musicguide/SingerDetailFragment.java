package iamnp.musicguide;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Singer detail screen.
 * This fragment is either contained in a {@link SingerListActivity}
 * in two-pane mode (on tablets) or a {@link SingerDetailActivity}
 * on handsets.
 */
public class SingerDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Singer mItem;

    private boolean twoPane = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SingerDetailFragment() {
    }

    private CollapsingToolbarLayout appBarLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = SingerListActivity.singers.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.id);
                Picasso.with(this.getContext()).load(mItem.cover.big).into(((ImageView) appBarLayout.findViewById(R.id.detail_imageView)));
            } else {
                twoPane = true;
            }
        }

        View rootView = inflater.inflate(R.layout.singer_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.singer_detail_textView)).setText(mItem.description);
            if (twoPane) {
                Picasso.with(this.getContext()).load(mItem.cover.big).into((ImageView) rootView.findViewById(R.id.singer_detail_imageView));
            } else {
                rootView.findViewById(R.id.singer_detail_imageView).setVisibility(View.GONE);
            }
        }

        return rootView;
    }
}
