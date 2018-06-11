package singh.pk.locationbasedapp.explore.di;

import dagger.Module;
import dagger.Provides;
import singh.pk.locationbasedapp.explore.FragmentExplore;

@Module
public class ExploreModule {

    private FragmentExplore fragmentExplore;

    public ExploreModule(FragmentExplore fragmentExplore) {
        this.fragmentExplore = fragmentExplore;
    }

    @Provides
    public FragmentExplore getFragmentExplore() {
        return fragmentExplore;
    }


}
