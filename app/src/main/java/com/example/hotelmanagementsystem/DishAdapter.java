package com.example.hotelmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private ArrayList<Dish> dishList;
    private OnDishClickListener listener;

    // Interface for selecting a dish
    public interface OnDishClickListener {
        void onDishClick(Dish dish);
    }

    public DishAdapter(
            ArrayList<Dish> dishList,
            OnDishClickListener listener) {

        this.dishList = dishList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull DishViewHolder holder,
            int position) {

        Dish dish = dishList.get(position);

        holder.tvDishName.setText(dish.getDishName());

        holder.tvDishPrice.setText(
                "₹" + String.format("%.2f", dish.getDishPrice())
        );

        // Select dish
        holder.itemView.setOnClickListener(v ->
                listener.onDishClick(dish)
        );
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    public static class DishViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvDishName;
        TextView tvDishPrice;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDishName =
                    itemView.findViewById(R.id.tvDishName);

            tvDishPrice =
                    itemView.findViewById(R.id.tvDishPrice);
        }
    }
}