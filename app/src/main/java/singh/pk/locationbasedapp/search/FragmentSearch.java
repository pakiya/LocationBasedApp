package singh.pk.locationbasedapp.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import singh.pk.locationbasedapp.R;

public class FragmentSearch extends Fragment implements SearchContrect.Views{

    @BindView(R.id.current_location_img_search_fragment) ImageView currentLocationImg;
    @BindView(R.id.search_place_edit) EditText namePlace;
    @BindView(R.id.search_location_img_search_fragment) ImageView searchLocationImg;

    @BindView(R.id.detail_recycler_view_search_fragment)
    RecyclerView details;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
    }

    private void init() {

    }
}
