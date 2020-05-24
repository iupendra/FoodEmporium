package com.foodemporium.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.foodemporium.R;
import com.foodemporium.databaseutil.DatabaseHelper;
import com.foodemporium.intrface.RecyclerViewClickListener;
import com.foodemporium.models.FoodModel;

import java.util.List;

/**
 * Created by Upendranath on 11/24/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<FoodModel> foodModelList;
    private RecyclerViewClickListener mListener;
    private DatabaseHelper db;

    Context context;
    int selectedPosition = -1;

    private String TAG = CartAdapter.class.getSimpleName();

    public CartAdapter(List<FoodModel> foodModelList, Context context) {
        this.foodModelList = foodModelList;
        this.mListener = mListener;
        this.context = context;
        db = new DatabaseHelper(context);
    }

    public void reloadAllContent(List<FoodModel> foodModels) {
        this.foodModelList.clear();
        this.foodModelList.addAll(foodModels);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cartitem, viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CartAdapter.ViewHolder viewHolder, final int position) {

        final FoodModel foodModel = foodModelList.get(position);

        viewHolder.txtTitle.setText(foodModelList.get(position).itemName);

        viewHolder.txtPrice.setText("Rs " + foodModelList.get(position).specialPrice);

        viewHolder.txtPrice.setVisibility(View.VISIBLE);

        viewHolder.txtPrice.setVisibility(View.VISIBLE);

        viewHolder.elegantNumberButton.setVisibility(View.VISIBLE);

        viewHolder.elegantNumberButton.setNumber(foodModel.countSelected);

        viewHolder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Log.d("TAG", String.format("oldValue: %d   newValue: %d", oldValue, newValue));

                Log.d("TAG", "onClick: " + foodModelList.get(position).itemName + " selected QTY: " + newValue);
                foodModel.countSelected = "" + newValue;
                if (newValue > 0) {
                    boolean iRecrdExist = db.CheckIsDataAlreadyInDBorNot(FoodModel.TABLE_NAME, "foodItemId", foodModel.foodItemId);
                    if (iRecrdExist) {

                        db.deleteRecordsinTable(foodModel.foodItemId, FoodModel.TABLE_NAME);

                    }

                    db.injectFoodModelntoCart(foodModel);

                    notifyDataSetChanged();

                }
            }
        });

        int qtySelected = 1;

        int totalPrice = 0;


        try {
            qtySelected = Integer.parseInt(foodModel.countSelected);

            Log.d(TAG, "onBindViewHolder: " + qtySelected);

        } catch (NumberFormatException num) {
            num.printStackTrace();
        }


        try {

            totalPrice = Integer.parseInt(foodModel.specialPrice) * qtySelected;

            Log.d(TAG, "onBindViewHolder: " + totalPrice);


        } catch (NumberFormatException numberEx) {

            numberEx.printStackTrace();
        }

        viewHolder.txtTotalPrice.setText("Rs " + totalPrice);

        viewHolder.lbMultiply.setVisibility(View.VISIBLE);

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
        return foodModelList.isEmpty() ? 0 : foodModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;// init the item view's
        TextView txtPrice;
        TextView lbMultiply;
        RelativeLayout relativeLayout;
        ElegantNumberButton elegantNumberButton;
        TextView txtTotalPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            elegantNumberButton = (ElegantNumberButton) itemView.findViewById(R.id.elegantButton);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            lbMultiply = (TextView) itemView.findViewById(R.id.lbMultiply);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtTotalPrice = (TextView) itemView.findViewById(R.id.txtTotalPrice);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_layout);
        }
    }

}
