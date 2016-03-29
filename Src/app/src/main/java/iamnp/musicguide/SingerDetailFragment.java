package iamnp.musicguide;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SingerDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Singer singer;
    private boolean useAppBar;
    private CollapsingToolbarLayout appBarLayout;
    private SingersDb singersDb;

    public SingerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        singersDb = new SingersDb(this.getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            try {
                singer = singersDb.getSinger(getArguments().getLong(ARG_ITEM_ID));
            } catch (Exception ex) {

            }
        }

        View rootView = inflater.inflate(R.layout.singer_detail, container, false);

        if (singer != null) {
            Activity activity = this.getActivity();
            useAppBar = activity instanceof SingerDetailActivity;

            if (useAppBar) {
                appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                appBarLayout.setTitle(singer.name);
                Picasso.with(this.getContext()).load(singer.cover.big).into(((ImageView) appBarLayout.findViewById(R.id.detail_imageView)));

                rootView.findViewById(R.id.singer_detail_imageView).setVisibility(View.GONE);
                rootView.findViewById(R.id.singer_detail_name_textView).setVisibility(View.GONE);
            } else {
                Picasso.with(this.getContext()).load(singer.cover.big).into((ImageView) rootView.findViewById(R.id.singer_detail_imageView));
                ((TextView) rootView.findViewById(R.id.singer_detail_name_textView)).setText(singer.name);
            }

            ((TextView) rootView.findViewById(R.id.singer_detail_desc_textView)).setText(singer.description);
        }

        return rootView;
    }
}
