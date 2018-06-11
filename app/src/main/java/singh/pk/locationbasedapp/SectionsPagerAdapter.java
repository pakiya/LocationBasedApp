package singh.pk.locationbasedapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import singh.pk.locationbasedapp.explore.FragmentExplore;
import singh.pk.locationbasedapp.search.FragmentSearch;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FragmentExplore exploreFragment = new FragmentExplore();
                return exploreFragment;

            case 1:
                FragmentSearch searchFragment = new FragmentSearch();
                return searchFragment;

                default:
                    return null;

        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        super.getPageTitle(position);

        switch (position){
            case 0:
                return "Explore";

            case 1:
                return "Search";

                default:
                    return null;
        }
    }
}
