package singh.pk.locationbasedapp.search.di;

import dagger.Component;
import singh.pk.locationbasedapp.search.FragmentSearch;

@Component (modules = SearchModule.class)
public interface SearchComponet {

    void injectSearchFragment(FragmentSearch fragmentSearch);
}
