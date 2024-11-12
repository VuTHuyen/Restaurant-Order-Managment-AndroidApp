package com.example.orderrestaurantapp.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Food;
import com.example.orderrestaurantapp.menu.Table;
import com.example.orderrestaurantapp.ui.fragments.OrderFragment;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class TableManagerAdapter extends RecyclerView.Adapter<TableManagerAdapter.ViewHolder> {
        private final List<Table> tables;
        public Context context;
        private final List<Table> tablesfull;
    public TableManagerAdapter (Context context, List<Table> tables){
            this.tables = tables;
            this.context = context;
            tablesfull = new ArrayList<>(tables);
        }


        @NonNull
        @Override
        public TableManagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.list_item_table, parent,false);

            return new TableManagerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Table table = tables.get(position);
            holder.nameOfTable.setText(table.getNameOfTable());
            if (table.getStatusOfTable()){
                holder.statusOfTable.setBackgroundResource(R.drawable.ic_reserved_svgrepo_com__1_);
            } else holder.statusOfTable.setBackgroundResource(R.drawable.ic_free_2_svgrepo_com);

        }
    public void query_filter_all(String searchQuery) {
        if (searchQuery.isEmpty()) {
            tables.clear();
            tables.addAll(tablesfull);
        } else {
            tables.clear();
            for (Table food : tablesfull) {
                if (food.getNameOfTable().toLowerCase().contains(searchQuery.toLowerCase())) {
                    tables.add(food);
                }
            }
        }
        notifyDataSetChanged();
    }
        @Override
        public int getItemCount() {
            return tables.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            TextView nameOfTable;
            ImageView statusOfTable;
            MaterialCardView order_table;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                nameOfTable = itemView.findViewById(R.id.table_number);
                statusOfTable = itemView.findViewById(R.id.status_of_table);
                order_table = itemView.findViewById(R.id.card_table);

                order_table.setOnClickListener(
                        v -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("TableID", nameOfTable.getText().toString());
                            OrderFragment fragment = OrderFragment.newInstance();
                            fragment.setArguments(bundle);
                            /**
                            ((AppCompatActivity) v.getContext())
                                    .getSupportFragmentManager()
                                    .beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.containerView, fragment, null)
                                    .addToBackStack(null)
                                    .commit();
                             */
                            FragmentActivity activity = (FragmentActivity)(v.getContext());
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .setReorderingAllowed(true)
                                    .replace(R.id.containerView, fragment, null)
                                    .addToBackStack(null)
                                    .commit();
                        }
                );
            }
        }
    }



