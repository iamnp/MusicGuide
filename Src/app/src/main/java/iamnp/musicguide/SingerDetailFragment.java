package iamnp.musicguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Fragment that holds detail singer info.
 */
public class SingerDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private boolean mUseAppBar;
    private CollapsingToolbarLayout mAppBarLayout;
    private Singer mCurrentSinger;

    public SingerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Try to get singer data from db
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            try {
                mCurrentSinger = DbSingleton.get(getContext()).getSinger(getArguments().getLong(ARG_ITEM_ID));
            } catch (Exception ex) {
                // No singer data will be shown
            }
        }

        View rootView = inflater.inflate(R.layout.singer_detail, container, false);

        Activity activity = this.getActivity();
        boolean floatingActionButtonVisible = false;

        if (mCurrentSinger != null) {
            // Whether we have appbar or not
            mUseAppBar = activity instanceof SingerDetailActivity;

            if (mUseAppBar) {
                mAppBarLayout = (CollapsingToolbarLayout) activity
                        .findViewById(R.id.toolbar_layout);
                mAppBarLayout.setTitle(mCurrentSinger.name);
                Picasso.with(this.getContext())
                        .load(mCurrentSinger.cover.big)
                        .into(((ImageView) mAppBarLayout.findViewById(R.id.detail_imageView)));

                rootView.findViewById(R.id.singer_detail_imageView).setVisibility(View.GONE);
                rootView.findViewById(R.id.singer_detail_name_textView).setVisibility(View.GONE);
            } else {
                Picasso.with(this.getContext())
                        .load(mCurrentSinger.cover.big)
                        .into((ImageView) rootView.findViewById(R.id.singer_detail_imageView));
                ((TextView) rootView.findViewById(R.id.singer_detail_name_textView))
                        .setText(mCurrentSinger.name);
            }

            ((TextView) rootView.findViewById(R.id.singer_detail_desc_textView))
                    .setText(mCurrentSinger.description);
            ((TextView) rootView.findViewById(R.id.singer_detail_genres_textView))
                    .setText(mCurrentSinger.genresAsString());
            ((TextView) rootView.findViewById(R.id.singer_detail_stats_textView))
                    .setText(mCurrentSinger.statsAsString(this.getContext()));

            if (mCurrentSinger.link != null) {
                floatingActionButtonVisible = true;
            }
        }

        FloatingActionButton fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        if (floatingActionButtonVisible) {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentSinger.link)));
                }
            });
        } else {
            fab.hide();
        }

        return rootView;
    }
}
