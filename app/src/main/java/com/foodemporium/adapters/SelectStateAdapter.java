package com.foodemporium.adapters;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foodemporium.R;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.StateModel;

import java.util.List;

/**
 * Created by Upendranath on 11/24/2017.
 */

public class SelectStateAdapter extends RecyclerView.Adapter<SelectStateAdapter.ViewHolder> {

    List<StateModel> stateModelList;
    private RecyclerViewClickListener mListener;

    Context context;
    int selectedPosition = -1;


    public SelectStateAdapter(List<StateModel> stateModels, Context context) {
        this.stateModelList = stateModels;
        this.mListener = mListener;
        this.context = context;
    }

    public void reloadAllContent(List<StateModel> stateModels) {
        this.stateModelList.clear();
        this.stateModelList.addAll(stateModels);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public SelectStateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stateadapter, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
//        vh.textView.setBackgroundColor(Color.parseColor(uiSettingsModel.getAppButtonBgColor()));
//        vh.textView.setTextColor(Color.parseColor(uiSettingsModel.getAppButtonTextColor()));
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SelectStateAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.textView.setText(stateModelList.get(position).stateName);

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
        return stateModelList.isEmpty() ? 0 : stateModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;// init the item view's
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.txtState);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
        }
    }

}
