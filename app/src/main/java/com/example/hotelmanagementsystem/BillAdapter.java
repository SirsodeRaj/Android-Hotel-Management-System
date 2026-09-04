package com.example.hotelmanagementsystem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BillAdapter
        extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {

    private ArrayList<Bill> billList;

    // =====================================================
    // VIEW BILL LISTENER
    // =====================================================

    public interface OnBillClickListener {

        void onViewBill(Bill bill);
    }


    // =====================================================
    // DELETE BILL LISTENER
    // =====================================================

    public interface OnDeleteBillListener {

        void onDeleteBill(Bill bill);
    }


    private OnBillClickListener viewListener;

    private OnDeleteBillListener deleteListener;


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public BillAdapter(
            ArrayList<Bill> billList,
            OnBillClickListener viewListener,
            OnDeleteBillListener deleteListener) {

        this.billList = billList;

        this.viewListener = viewListener;

        this.deleteListener = deleteListener;
    }


    // =====================================================
    // CREATE VIEW HOLDER
    // =====================================================

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_bill,
                        parent,
                        false
                );

        return new BillViewHolder(view);
    }


    // =====================================================
    // BIND BILL DATA
    // =====================================================

    @Override
    public void onBindViewHolder(
            @NonNull BillViewHolder holder,
            int position) {

        Bill bill =
                billList.get(position);


        // =================================================
        // CUSTOMER NAME
        // =================================================

        holder.tvCustomerName.setText(
                bill.getCustomerName()
        );


        // =================================================
        // BILL NUMBER
        // =================================================

        holder.tvBillNo.setText(
                "Bill No.: " +
                        bill.getBillNo()
        );


        // =================================================
        // CUSTOMER
        // =================================================

        holder.tvCustomerNameDetails.setText(
                "Customer: " +
                        bill.getCustomerName()
        );


        // =================================================
        // DATE
        // =================================================

        holder.tvBillDate.setText(
                "Date: " +
                        bill.getBillDate()
        );


        // =================================================
        // TOTAL
        // =================================================

        holder.tvBillTotal.setText(
                "Total: ₹" +
                        String.format(
                                "%.2f",
                                bill.getTotalBill()
                        )
        );


        // =================================================
        // VIEW BILL
        // =================================================

        holder.btnViewBill.setOnClickListener(v -> {

            if (viewListener != null) {

                viewListener.onViewBill(bill);
            }
        });


        // =================================================
        // DELETE BILL
        // =================================================

        holder.btnDeleteBill.setOnClickListener(v -> {

            if (deleteListener != null) {

                deleteListener.onDeleteBill(bill);
            }
        });
    }


    // =====================================================
    // ITEM COUNT
    // =====================================================

    @Override
    public int getItemCount() {

        return billList.size();
    }


    // =====================================================
    // VIEW HOLDER
    // =====================================================

    public static class BillViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvCustomerName;

        TextView tvBillNo;

        TextView tvCustomerNameDetails;

        TextView tvBillDate;

        TextView tvBillTotal;

        Button btnViewBill;

        Button btnDeleteBill;


        public BillViewHolder(
                @NonNull View itemView) {

            super(itemView);


            // Customer heading
            tvCustomerName =
                    itemView.findViewById(
                            R.id.tvCustomerName
                    );


            // Bill number
            tvBillNo =
                    itemView.findViewById(
                            R.id.tvHistoryBillNo
                    );


            // Customer details
            tvCustomerNameDetails =
                    itemView.findViewById(
                            R.id.tvHistoryCustomer
                    );


            // Date
            tvBillDate =
                    itemView.findViewById(
                            R.id.tvHistoryDate
                    );


            // Total
            tvBillTotal =
                    itemView.findViewById(
                            R.id.tvHistoryTotal
                    );


            // View button
            btnViewBill =
                    itemView.findViewById(
                            R.id.btnViewBill
                    );


            // Delete button
            btnDeleteBill =
                    itemView.findViewById(
                            R.id.btnDeleteBill
                    );
        }
    }
}