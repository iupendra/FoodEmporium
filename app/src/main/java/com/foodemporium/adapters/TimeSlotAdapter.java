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
import com.foodemporium.models.AreaModel;
import com.foodemporium.models.TimeModel;

import java.util.List;

/**
 * Created by Upendranath on 11/24/2017.
 */

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    List<TimeModel> timeModelList;
    private RecyclerViewClickListener mListener;

    Context context;
    int selectedPosition = -1;


    public TimeSlotAdapter(List<TimeModel> timeModelList, Context context) {
        this.timeModelList = timeModelList;
        this.mListener = mListener;
        this.context = context;
    }

    public void reloadAllContent(List<TimeModel> timeModels) {
        this.timeModelList.clear();
        this.timeModelList.addAll(timeModels);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.timeslotitem, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final TimeSlotAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.txtTimeSlot.setText(timeModelList.get(position).displayTime);
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
        return timeModelList.isEmpty() ? 0 : timeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTimeSlot;// init the item view's
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTimeSlot = (TextView) itemView.findViewById(R.id.txtTimeSlot);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
        }
    }

}
