package com.foodemporium.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.foodemporium.R;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.RestaurantModel;


import java.util.List;

/**
 * Created by Upendranath on 11/24/2017.
 */

public class ResturantsAdapter extends RecyclerView.Adapter<ResturantsAdapter.ViewHolder> {

    List<RestaurantModel> restaurantModelList;
    private RecyclerViewClickListener mListener;

    Context context;
    int selectedPosition = -1;


    public ResturantsAdapter(List<RestaurantModel> restaurantModelList, Context context) {
        this.restaurantModelList = restaurantModelList;
        this.mListener = mListener;
        this.context = context;
    }

    public void reloadAllContent(List<RestaurantModel> restaurantModelList) {
        this.restaurantModelList.clear();
        this.restaurantModelList.addAll(restaurantModelList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ResturantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.restaurantitem, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ResturantsAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.txtName.setText(restaurantModelList.get(position).businessName);

        viewHolder.txtAddress.setText(restaurantModelList.get(position).location);

        viewHolder.txtTelNumber.setText(restaurantModelList.get(position).contactNumber);

        float ratingValue = 0;

        try {
            ratingValue = Float.parseFloat(restaurantModelList.get(position).rating);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            ratingValue = 0;
        }
        viewHolder.ratingBar.setRating(ratingValue);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = viewHolder.getAdapterPosition();
                notifyDataSetChanged();

            }
        });

        Glide.with(context).
                load(restaurantModelList.get(position).merchantImagePath).
                placeholder(R.mipmap.ic_launcher_round).
                into(viewHolder.merchantImage);
    }

    @Override
    public int getItemCount() {
        return restaurantModelList.isEmpty() ? 0 : restaurantModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtAddress, txtTelNumber;// init the item view's
        RelativeLayout relativeLayout;
        RatingBar ratingBar;
        ImageView merchantImage;


        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtTelNumber = (TextView) itemView.findViewById(R.id.txtTelNumber);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rat_adapt_ratingbar);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
            merchantImage = (ImageView) itemView.findViewById(R.id.imgView);
        }
    }

}
