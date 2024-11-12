package com.example.orderrestaurantapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.Food;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.menu.OrderInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuFoodAdapter extends RecyclerView.Adapter<MenuFoodAdapter.ViewHolder> {
    private final List<Food> foods;
    private List<Food> foodsfull;
    public Context context;
    String TableID;
    FragmentActivity activity;
    android.content.res.Resources resources;
    public MenuFoodAdapter (Context context, List<Food> foods, String TableId, FragmentActivity activity, android.content.res.Resources resources){
        this.foods = foods;
        this.context = context;
        this.TableID = TableId;
        this.activity = activity;
        this.resources = resources;
        foodsfull = new ArrayList<>(foods);
    }

    private HashMap<Integer, OrderInfo> orderInfoMap = new HashMap<>();


    @NonNull
    @Override
    public MenuFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_food_menu, parent,false);
        return new MenuFoodAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuFoodAdapter.ViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Food food = foods.get(position);
        holder.nameOfFood.setText(food.getFoodName());
        holder.price.setText(food.getFoodPrice());
        int number = orderInfoMap.containsKey(position) ? orderInfoMap.get(position).getNumber() : 0;
        holder.numberPicker.setValue(number);
        String wish = orderInfoMap.containsKey(position) ? orderInfoMap.get(position).getWishType() : "";
        holder.wish.setText(wish);
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }
    public void query_filter_all(String searchQuery) {
        if (searchQuery.isEmpty()) {
            foods.clear();
            foods.addAll(foodsfull);
        } else {
            foods.clear();
            for (Food food : foodsfull) {
                if (food.getFoodName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    foods.add(food);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfFood;
        NumberPicker numberPicker;
        ImageButton addFood;
        EditText wish;
        TextView price;
        Order order;

        public void setOrder(Order order){
            this.order = order;
        }
        public Order getOrder(){
            return this.order;
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfFood = itemView.findViewById(R.id.name_of_food);
            wish = itemView.findViewById(R.id.special_wish_text);
            price = itemView.findViewById(R.id.price_of_food);

            numberPicker = itemView.findViewById(R.id.quantity_food);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(10);
            addFood = itemView.findViewById(R.id.checkbox_food);

            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    int position = getAdapterPosition();
                    System.out.println("-----position------" + position);
                    if (position != RecyclerView.NO_POSITION) {
                        if (orderInfoMap.containsKey(position)) {
                            OrderInfo orderInfo = orderInfoMap.get(position);
                            orderInfo.setNumber(newVal);
                        } else {
                            orderInfoMap.put(position, new OrderInfo(newVal, ""));
                        }
                        updateOrder(position);

                    }
                }
            });
            wish.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Do nothing
                }

                @Override
                public void afterTextChanged(Editable s) {
                    int position = getAdapterPosition();
                    System.out.println("-----position------" + position);
                    if (position != RecyclerView.NO_POSITION) {
                        if (orderInfoMap.containsKey(position)) {
                            OrderInfo orderInfo = orderInfoMap.get(position);
                            orderInfo.setWishType(wish.getText().toString());
                        } else {
                            orderInfoMap.put(position, new OrderInfo(0, ""));
                        }
                        updateOrder(position);

                    }
                }
            });
            addFood.setOnClickListener(
                    v -> {
                        String tableId = MenuFoodAdapter.this.TableID;
                        FirebaseFirestore database = FirebaseFirestore.getInstance();

                        // Ensure the position is valid
                        if(getOrder().getNumber()>0) {
                            Database.putDataInFirebase(database, getOrder(), tableId, activity, resources);
                        } else Database.setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.order_add_no_number));
                        notifyDataSetChanged();
                    }
            );
        }

            private void updateOrder (int position) {
                if (position != RecyclerView.NO_POSITION) {
                    // Get the order at the current position
                    OrderInfo orderInfo = orderInfoMap.get(position);
                    System.out.println("---------order infor----" + orderInfo.getNumber() +"---" + orderInfo.getWishType() );
                    Food food = foods.get(position);
                    System.out.println("---------order ----" + food.getFoodName() );

                    Order checked_order = new Order(food.getFoodName(),
                            orderInfo.getNumber(),
                            food.getFoodPrice(),
                            orderInfo.getWishType());
                    checked_order.setTotalPrice(checked_order.getPrice(), checked_order.getNumber());

                    setOrder(new Order(checked_order.getOrdername(),
                            checked_order.getNumber(),
                            checked_order.getPrice(),
                            checked_order.getTotalPrice(),
                            checked_order.getwishfood_typdrink(),
                            false,
                            false));



                }
            }
    }

}
