package com.example.orderrestaurantapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderrestaurantapp.R;
import com.example.orderrestaurantapp.menu.Database;
import com.example.orderrestaurantapp.menu.Drink;
import com.example.orderrestaurantapp.menu.DrinkWithoutVarity;
import com.example.orderrestaurantapp.menu.Food;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.menu.OrderInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDrinkWithoutVarityAdapter extends RecyclerView.Adapter<MenuDrinkWithoutVarityAdapter.ViewHolder> {
    private final List<DrinkWithoutVarity> drinks;
    private List<DrinkWithoutVarity> drinksfull;
    public Context context;
    private List<Order> orders = new ArrayList<>();
    String TableID;
    FragmentActivity activity;
    android.content.res.Resources resources;
    public MenuDrinkWithoutVarityAdapter (Context context, List<DrinkWithoutVarity> drinks, String TableId,FragmentActivity activity, android.content.res.Resources resources){
        this.drinks = drinks;
        this.context = context;
        this.TableID = TableId;
        this.activity = activity;
        this.resources = resources;
        drinksfull = new ArrayList<>(drinks);
    }
    private HashMap<Integer, OrderInfo> orderInfoMap = new HashMap<>();


    @NonNull
    @Override
    public MenuDrinkWithoutVarityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_food_menu, parent,false);

        return new MenuDrinkWithoutVarityAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuDrinkWithoutVarityAdapter.ViewHolder holder, int position) {
        DrinkWithoutVarity table = drinks.get(position);
        holder.nameOfDrink.setText(table.getDrinkName());
        holder.price.setText(table.getPrice());
        int number = orderInfoMap.containsKey(position) ? orderInfoMap.get(position).getNumber() : 0;
        holder.numberPicker.setValue(number);
        String wish = orderInfoMap.containsKey(position) ? orderInfoMap.get(position).getWishType() : "";
        holder.wish.setText(wish);
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }
    public void query_filter_all(String searchQuery) {
        if (searchQuery.isEmpty()) {
            drinks.clear();
            drinks.addAll(drinksfull);
        } else {
            drinks.clear();
            for (DrinkWithoutVarity drink : drinksfull) {
                if (drink.getDrinkName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    drinks.add(drink);
                }
            }
        }
        notifyDataSetChanged();
    }
    public List<Order> getOrders(){
        return this.orders;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameOfDrink;
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
            nameOfDrink = itemView.findViewById(R.id.name_of_food);
            price = itemView.findViewById(R.id.price_of_food);
            wish = itemView.findViewById(R.id.special_wish_text);

            numberPicker = itemView.findViewById(R.id.quantity_food);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(20);
            addFood = itemView.findViewById(R.id.checkbox_food);


            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    int position = getAdapterPosition();
                    System.out.println("-----position---drink--------" + position);
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
                        String tableId = MenuDrinkWithoutVarityAdapter.this.TableID;
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        if(getOrder().getNumber()>0) {
                            Database.putDataInFirebase(database, getOrder(), tableId, activity, resources);
                        } else Database.setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.order_add_no_number));
                        // Ensure the position is valid
                        notifyDataSetChanged();
                    }
            );


        }

        private void updateOrder(int position) {
            if (position != RecyclerView.NO_POSITION) {
                // Get the order at the current position
                OrderInfo orderInfo = orderInfoMap.get(position);
                System.out.println("---------order infor----" + orderInfo.getNumber() +"---" + orderInfo.getWishType() );
                if (orderInfo.getNumber() > 0) {
                    DrinkWithoutVarity food = drinks.get(position);
                    System.out.println("---------order ----" + food.getDrinkName() );

                    Order checked_order = new Order(food.getDrinkName(),
                            orderInfo.getNumber(),
                            food.getPrice(),
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

}
