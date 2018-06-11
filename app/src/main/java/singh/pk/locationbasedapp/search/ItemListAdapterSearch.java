package singh.pk.locationbasedapp.search;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import singh.pk.locationbasedapp.R;
import singh.pk.locationbasedapp.search.pojo_search_place.Venue;

public class ItemListAdapterSearch extends RecyclerView.Adapter<ItemListAdapterSearch.ViewHolder> {

    public List<Venue> placeInfo = new ArrayList<>(0);
    FragmentSearch fragmentSearch;

    @Inject
    public ItemListAdapterSearch(FragmentSearch fragmentSearch) {
        this.fragmentSearch = fragmentSearch;
    }

    public void updatePlaceInfo(List<Venue> placeInfo){
        this.placeInfo.clear();
        this.placeInfo = placeInfo;
        notifyDataSetChanged();
    }

    public void searchFromName(List<Venue> placeInfo){
        this.placeInfo = placeInfo;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(placeInfo.get(position), position);
    }

    @Override
    public int getItemCount() {
        return placeInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.place_image)
        ImageView placeImage;
        @BindView(R.id.place_name)
        TextView placeName;
        @BindView(R.id.item_list_relative_layout)
        RelativeLayout itemListRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bind(final Venue item, final int position) {

            try {
                String url_1 = item.getCategories().get(0).getIcon().getPrefix();
                String url_2 = item.getCategories().get(0).getIcon().getSuffix();
                Picasso.get().load(url_1+"64"+url_2).placeholder(R.drawable.default_icon).into(placeImage);
            } catch (IndexOutOfBoundsException i){
                Log.i("FragmentSearch :" , i.getMessage());
            }
            placeName.setText(item.getName());

            itemListRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(fragmentSearch.getContext(), ""+position, Toast.LENGTH_SHORT).show();
                    fragmentSearch.getImageUser(item);

                }
            });
        }
    }
}
