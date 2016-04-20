package iamnp.musicguide;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Fragment that holds detail singer info.
 */
public class SingerDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private boolean mUseAppBar;
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

        // Whether we have appbar or not
        mUseAppBar = activity instanceof SingerDetailActivity;

        if (mCurrentSinger != null) {
            if (mUseAppBar) {
                // Set appbar title and hide name TextView
                ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(mCurrentSinger.name);
                }
                rootView.findViewById(R.id.singer_detail_name_textView).setVisibility(View.GONE);
            } else {
                // Set name TextView text
                ((TextView) rootView.findViewById(R.id.singer_detail_name_textView))
                        .setText(mCurrentSinger.name);
            }

            // Set imageView height to 5/12 of the screen height
            ImageView imageView = (ImageView) rootView.findViewById(R.id.singer_detail_imageView);
            float heightDp = getResources().getDisplayMetrics().heightPixels * (5.0f/12.0f);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            lp.height = (int) heightDp;

            Picasso.with(this.getContext()).load(mCurrentSinger.cover.big).into(imageView);
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
        // Show or hide fab depending on presence of singer url
        if (floatingActionButtonVisible) {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mCurrentSinger.link)));
                }
            });
            ((MyFAB) fab).setAnimationEnabled(true);
        } else {
            fab.hide();
            ((MyFAB) fab).setAnimationEnabled(false);
        }

        return rootView;
    }
}
