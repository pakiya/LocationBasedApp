package singh.pk.locationbasedapp.search.di;

import dagger.Module;
import dagger.Provides;
import singh.pk.locationbasedapp.search.FragmentSearch;

@Module
public class SearchModule {

    private FragmentSearch fragmentSearch;

    public SearchModule(FragmentSearch fragmentSearch) {
        this.fragmentSearch = fragmentSearch;
    }

    @Provides
    public FragmentSearch getFragmentSearch() {
        return fragmentSearch;
    }
}
