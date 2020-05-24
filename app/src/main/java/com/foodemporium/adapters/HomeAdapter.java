package com.foodemporium.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.foodemporium.HomeActivity;
import com.foodemporium.R;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.HomeModel;

import java.util.List;

/**
 * Created by Upendranath on 11/24/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    List<HomeModel> homeModelList;
    private RecyclerViewClickListener mListener;

    Context context;
    int selectedPosition = -1;


    public HomeAdapter(List<HomeModel> homeModelList, Context context) {
        this.homeModelList = homeModelList;
        this.mListener = mListener;
        this.context = context;
    }

    public void reloadAllContent(List<HomeModel> homeModels) {
        this.homeModelList.clear();
        this.homeModelList.addAll(homeModels);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.homeitem, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.imgView.setImageDrawable(getDrawbleImage(homeModelList.get(position).cuisineTypeId));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = viewHolder.getAdapterPosition();
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeModelList.isEmpty() ? 0 : homeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;// init the item view's
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
            imgView = (ImageView) itemView.findViewById(R.id.imgView);
        }
    }


    public Drawable getDrawbleImage(int homeID) {


        switch (homeID) {
            case 1:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.thaiw, null);
            case 2:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.indianw, null);
            case 3:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.italianw, null);
            case 4:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.chinesew, null);
            case 5:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.mexicanw, null);
            case 6:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.sushiw, null);
            case 7:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.burgerw, null);
            case 8:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.other, null);
            default:
                return ResourcesCompat.getDrawable(context.getResources(), R.drawable.other, null);

        }
    }


}
