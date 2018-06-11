package singh.pk.locationbasedapp.search;

public class BaseRepositorySearch<T extends BasePresenterSearch> {

    T actions;

    public void onAttach(T actions){
        this.actions = actions;
    }

    public T getActions() {
        return actions;
    }
}
