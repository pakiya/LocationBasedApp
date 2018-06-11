package singh.pk.locationbasedapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    @BindView(R.id.main_tabPager) ViewPager mViewPager;
    @BindView(R.id.main_tabs) TabLayout mTabLayout;

    // Set Fragment ViewPager Adapter
    private SectionsPagerAdapter mSectionsPagerAdapter;


    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize ButterKnife.
        ButterKnife.bind(this);

        // Set Fragment of ViewPager
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        // Set Fragment Name in TabLayout Toolbar.
        mTabLayout.setupWithViewPager(mViewPager);

    }


}
