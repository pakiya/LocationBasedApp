package singh.pk.locationbasedapp.explore;

public class BaseRepository<T extends BasePresenter> {

    T actions;

    public void onAttach(T actions){
        this.actions = actions;
    }

    public T getActions() {
        return actions;
    }
}
