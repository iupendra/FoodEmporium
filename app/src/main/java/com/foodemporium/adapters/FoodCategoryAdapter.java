package com.foodemporium.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.foodemporium.R;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.FoodCategoryModel;

import java.util.List;

/**
 * Created by Upendranath on 11/24/2017.
 **/

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.ViewHolder> {

    private List<FoodCategoryModel> foodCategoryModelList;
    private RecyclerViewClickListener mListener;

    Context context;
    int selectedPosition = -1;


    public FoodCategoryAdapter(List<FoodCategoryModel> foodCategoryModelList, Context context) {
        this.foodCategoryModelList = foodCategoryModelList;
        this.mListener = mListener;
        this.context = context;
    }

    public void reloadAllContent(List<FoodCategoryModel> foodModels) {
        this.foodCategoryModelList.clear();
        this.foodCategoryModelList.addAll(foodModels);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public FoodCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fooditem, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodCategoryAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.textView.setText(foodCategoryModelList.get(position).foodCategory);

        viewHolder.elegantNumberButton.setVisibility(View.GONE);

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
        return foodCategoryModelList.isEmpty() ? 0 : foodCategoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;// init the item view's
        RelativeLayout relativeLayout;
        ElegantNumberButton elegantNumberButton;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtTitle);
            elegantNumberButton = (ElegantNumberButton) itemView.findViewById(R.id.elegantButton);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
        }
    }

}
