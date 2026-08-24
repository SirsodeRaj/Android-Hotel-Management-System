package com.example.hotelmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter
        extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<CartItem> cartList;

    // Selected item position
    private int selectedPosition = -1;


    public CartAdapter(ArrayList<CartItem> cartList) {
        this.cartList = cartList;
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_cart,
                        parent,
                        false
                );

        return new CartViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull CartViewHolder holder,
            int position) {

        CartItem item =
                cartList.get(position);


        // Dish name
        holder.tvCartDishName.setText(
                item.getDishName()
        );


        // Price × quantity
        holder.tvCartDetails.setText(
                "₹"
                        + String.format(
                        "%.2f",
                        item.getPrice()
                )
                        + " × "
                        + item.getQuantity()
        );


        // Amount
        holder.tvCartAmount.setText(
                "Amount: ₹"
                        + String.format(
                        "%.2f",
                        item.getAmount()
                )
        );


        // Select item
        holder.itemView.setOnClickListener(v -> {

            int currentPosition =
                    holder.getAdapterPosition();

            if (currentPosition !=
                    RecyclerView.NO_POSITION) {

                selectedPosition =
                        currentPosition;

                notifyDataSetChanged();
            }
        });


        // Visual indication for selected item
        if (position == selectedPosition) {

            holder.itemView.setAlpha(0.6f);

        } else {

            holder.itemView.setAlpha(1.0f);
        }
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }


    // Get selected item
    public int getSelectedPosition() {
        return selectedPosition;
    }


    // Clear selection
    public void clearSelection() {
        selectedPosition = -1;
    }


    // ViewHolder
    public static class CartViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvCartDishName;
        TextView tvCartDetails;
        TextView tvCartAmount;


        public CartViewHolder(
                @NonNull View itemView) {

            super(itemView);


            tvCartDishName =
                    itemView.findViewById(
                            R.id.tvCartDishName
                    );


            tvCartDetails =
                    itemView.findViewById(
                            R.id.tvCartDetails
                    );


            tvCartAmount =
                    itemView.findViewById(
                            R.id.tvCartAmount
                    );
        }
    }
}