package com.galpotechsolutions.popularmovies;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    /** Cntext of the app*/
    private Context mContext;

    /**
     * Create a new {@link SimpleFragmentPagerAdapter} object.
     *
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AnimationFragment();
        } else if(position == 1){
            return new HorrorFragment();
        } else if (position == 2){
            return new ActionFragment();
        } else {
            return new DramaFragment();
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 4;
    }

    /**
     *  Return the title of the pages
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_animation);
        } else if (position == 1){
            return  mContext.getString(R.string.category_horror);
        } else if (position == 2){
            return mContext.getString(R.string.category_action);
        } else {
            return mContext.getString(R.string.category_drama);
        }
    }
}
