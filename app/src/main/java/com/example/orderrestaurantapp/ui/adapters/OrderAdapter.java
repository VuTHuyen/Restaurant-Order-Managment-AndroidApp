package com.example.orderrestaurantapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.ui.fragments.OrderFragment;
import com.example.orderrestaurantapp.ui.fragments.SearchMenuFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private final List<Order> orders;
    public Context context;
    String TableID;
    FragmentActivity activity;
    android.content.res.Resources resources;
    public OrderAdapter (Context context, List<Order> orders, String TableId, FragmentActivity activity, android.content.res.Resources resources){
        this.orders = orders;
        this.context = context;
        this.TableID = TableId;
        this.activity = activity;
        this.resources = resources;
    }


    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_order_of_table, parent,false);

        return new OrderAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.nameOfOrder.setText(order.getOrdername());
        holder.numberOfOrder.setText(""+order.getNumber());
        holder.priceOfOrder.setText(order.getPrice());
        holder.totalPrice.setText(order.getTotalPrice());
        holder.wish.setText(order.getwishfood_typdrink());
        if (holder.removable){
            order.setRemoveable(true);
        }
        if(!order.getPrinted()){
            holder.removeOrder.setImageResource(R.drawable.delete___icon);
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView numberOfOrder;

        TextView nameOfOrder;
        TextView priceOfOrder;
        TextView totalPrice;
        ImageButton removeOrder;
        TextView wish;
        boolean removable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfOrder = itemView.findViewById(R.id.name_of_order);
            wish = itemView.findViewById(R.id.special_wish_order);
            numberOfOrder = itemView.findViewById(R.id.number_of_order);
            priceOfOrder = itemView.findViewById(R.id.price_of_order);
            totalPrice = itemView.findViewById(R.id.total_price_of_order);
            removeOrder = itemView.findViewById(R.id.delete_order_button);

            removeOrder.setOnClickListener(
                    v -> {
                        String tableId = OrderAdapter.this.TableID;

                        // Access the position of the current order in the adapter
                        int position = getAdapterPosition();
                        System.out.println("---------position removed order------" + position);

                        // Ensure the position is valid
                        if (position != RecyclerView.NO_POSITION) {
                            // Get the order at the current position
                            Order order = orders.get(position);
                            System.out.println("--------- removed order------" + order.getOrdername());

                            // Delete the order from Firestore using the tableId and order details
                            deleteOrderFromFirestore(tableId, order);

                            // Remove the order from the local orders list
                            orders.remove(position);

                            // Notify the adapter of the data change
                            notifyItemRemoved(position);
                            notifyDataSetChanged();

                        }
                    }
            );


        }
        private void deleteOrderFromFirestore(String tableId, Order order) {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(tableId)
                    .document(order.getDocumentOrderID())
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Database.setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.remove_order_sucess));
                                // this method is called when the task is success
                                // after deleting we are starting our MainActivity.
                                OrderFragment fragment = OrderFragment.newInstance();
                                Bundle bundle = new Bundle();
                                bundle.putString("TableID", tableId);
                                fragment.setArguments(bundle);
                                ((AppCompatActivity) context)
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .setReorderingAllowed(true)
                                        .replace(R.id.containerView, fragment, null)
                                        .addToBackStack(null)
                                        .commit();
                            } else {
                                Database.setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.remove_order_fail));

                                // if the delete operation is failed
                                // we are displaying a toast message.
                            }
                        }
                    });
        }


    }

}
