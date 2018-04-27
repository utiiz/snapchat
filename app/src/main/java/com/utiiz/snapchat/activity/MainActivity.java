package com.utiiz.snapchat.activity;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.utiiz.snapchat.R;
import com.utiiz.snapchat.adapter.FragmentAdapter;
import com.utiiz.snapchat.fragment.CameraFragment;
import com.utiiz.snapchat.fragment.FriendFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @NonNull @BindView(R.id.view_pager) public ViewPager mViewPager;
    @NonNull @BindView(R.id.background) public View mBackground;

    ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    Integer[] COLORS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        COLORS = new Integer[] {getApplicationContext().getColor(R.color.colorPrimaryBlue), getApplicationContext().getColor(android.R.color.transparent), getApplicationContext().getColor(R.color.colorPrimaryPurple)};

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = getWindow().getDecorView().getSystemUiVisibility();
            // flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            getWindow().getDecorView().setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ArrayList<Class<? extends Fragment>> pages = new ArrayList<>();
        pages.add(FriendFragment.class);
        pages.add(CameraFragment.class);
        pages.add(FriendFragment.class);
        Integer mMainPageIndex = 1; //pages.indexOf(FriendFragment.class);
        final FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), getApplicationContext(), pages);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(pages.size());
        //mHorizontalPager.setPageTransformer(false, new SnapPageTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position < (adapter.getCount() -1) && position < (COLORS.length - 1)) {
                    mBackground.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset, COLORS[position], COLORS[position + 1]));
                } else {
                    mBackground.setBackgroundColor(COLORS[COLORS.length - 1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(mMainPageIndex);

    }

}
