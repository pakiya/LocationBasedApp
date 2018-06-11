package singh.pk.locationbasedapp.explore.di;

import dagger.Component;
import singh.pk.locationbasedapp.explore.FragmentExplore;

@Component(modules = ExploreModule.class)
public interface ExploreComponet {

    void injectExploreFragment(FragmentExplore fragmentExplore);
}
