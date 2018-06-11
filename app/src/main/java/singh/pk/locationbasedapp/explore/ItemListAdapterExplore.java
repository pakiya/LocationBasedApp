package singh.pk.locationbasedapp.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import singh.pk.locationbasedapp.pojo.Item_;

public class ItemListAdapterExplore extends RecyclerView.Adapter<ItemListAdapterExplore.ViewHolder> {

    public List<Item_> placeInfo = new ArrayList<>(0);
    FragmentExplore fragmentExplore;

    @Inject
    public ItemListAdapterExplore(FragmentExplore fragmentExplore) {
        this.fragmentExplore = fragmentExplore;
    }

    public void updatePlaceInfo(List<Item_> placeInfo){
        this.placeInfo.clear();
        this.placeInfo = placeInfo;
        notifyDataSetChanged();
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

        @BindView(R.id.place_image) ImageView placeImage;
        @BindView(R.id.place_name) TextView placeName;
        @BindView(R.id.item_list_relative_layout) RelativeLayout itemListRelativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }

        public void bind(final Item_ item, final int position) {
            String url_1 = item.getVenue().getCategories().get(0).getIcon().getPrefix();
            String url_2 = item.getVenue().getCategories().get(0).getIcon().getSuffix();

            placeName.setText(item.getVenue().getName());
            Picasso.get().load(url_1+"88"+url_2).placeholder(R.drawable.default_icon).into(placeImage);

            fragmentExplore.getImageUser(item);

            itemListRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(fragmentExplore.getContext(), ""+position, Toast.LENGTH_SHORT).show();

                    fragmentExplore.getItemClickValue(item);

                }
            });

        }
    }
}
