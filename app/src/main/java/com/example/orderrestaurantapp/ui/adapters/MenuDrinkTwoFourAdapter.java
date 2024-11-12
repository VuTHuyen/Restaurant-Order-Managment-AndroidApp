package com.example.orderrestaurantapp.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.example.orderrestaurantapp.menu.DrinkVarietyTwoFour;
import com.example.orderrestaurantapp.menu.DrinkWithoutVarity;
import com.example.orderrestaurantapp.menu.Food;
import com.example.orderrestaurantapp.menu.Order;
import com.example.orderrestaurantapp.menu.OrderInfo;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuDrinkTwoFourAdapter extends RecyclerView.Adapter<MenuDrinkTwoFourAdapter.ViewHolder> {
    private final List<DrinkVarietyTwoFour> drinks;
    private List<DrinkVarietyTwoFour> drinksfull;
    public Context context;
    private List<Order> orders = new ArrayList<>();
    String TableID;
    FragmentActivity activity;
    android.content.res.Resources resources;
    public MenuDrinkTwoFourAdapter (Context context, List<DrinkVarietyTwoFour> drinks, String TableId, FragmentActivity activity, android.content.res.Resources resources){
        this.drinks = drinks;
        this.context = context;
        this.TableID = TableId;
        this.activity = activity;
        this.resources = resources;
        drinksfull = new ArrayList<>(drinks);
    }
    private HashMap<Integer, OrderInfo> orderInfoMap = new HashMap<>();
    private int selectedRadioButtonPosition = RecyclerView.NO_POSITION;


    @NonNull
    @Override
    public MenuDrinkTwoFourAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_drink_with_varity, parent,false);

        return new MenuDrinkTwoFourAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuDrinkTwoFourAdapter.ViewHolder holder, int position) {
        DrinkVarietyTwoFour table = drinks.get(position);
        holder.nameOfDrink.setText(table.getDrinkName());
        holder.priceSmall.setText(table.getPriceVarietyZeroTwo());
        holder.priceBig.setText(table.getPriceVarietyZeroFour());
        int number = orderInfoMap.containsKey(position) ? orderInfoMap.get(position).getNumber() : 0;
        holder.numberPicker.setValue(number);

        // Set the checked state of the CheckBox
        holder.smallSize.setChecked(position == selectedRadioButtonPosition && orderInfoMap.get(position) != null
                && orderInfoMap.get(position).getWishType().equals("0,2l"));
        holder.bigSize.setChecked(position == selectedRadioButtonPosition && orderInfoMap.get(position) != null
                && orderInfoMap.get(position).getWishType().equals("0,4l"));

        holder.smallSize.setText("0,2L");
        holder.bigSize.setText("0,4L");

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
            for (DrinkVarietyTwoFour drink : drinksfull) {
                if (drink.getDrinkName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    drinks.add(drink);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfDrink;
        RadioButton smallSize;
        ImageButton checkBox;
        RadioButton bigSize;
        TextView priceSmall;
        TextView priceBig;
        NumberPicker numberPicker;
        Order order;

        public void setOrder(Order order){
            this.order = order;
        }
        public Order getOrder(){
            return this.order;
        }
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfDrink = itemView.findViewById(R.id.name_of_drink);
            smallSize = itemView.findViewById(R.id.size_small);
            bigSize = itemView.findViewById(R.id.size_large);
            checkBox = itemView.findViewById(R.id.checkbox_drink);
            priceSmall = itemView.findViewById(R.id.price_of_drink_small);
            priceBig = itemView.findViewById(R.id.price_of_drink_big);
            numberPicker = itemView.findViewById(R.id.quantity_drink);
            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(10);
            checkBox = itemView.findViewById(R.id.checkbox_drink);


            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    int position = getAdapterPosition();
                    selectedRadioButtonPosition = position;
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
            smallSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    selectedRadioButtonPosition = position;
                    System.out.println("-----position---drink--------" + position);
                    if (isChecked) {
                        if (position != RecyclerView.NO_POSITION) {
                            if (orderInfoMap.containsKey(position)) {
                                OrderInfo orderInfo = orderInfoMap.get(position);
                                orderInfo.setWishType("0,2l");
                                updateOrder(position);
                            } else {
                                orderInfoMap.put(position, new OrderInfo(0, "0,2l"));
                                updateOrder(position);
                            }
                        }
                    }

                }
            });
            bigSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = getAdapterPosition();
                    selectedRadioButtonPosition = position;
                    System.out.println("-----position---drink--------" + position);
                    if (isChecked) {
                        if (position != RecyclerView.NO_POSITION) {
                            if (orderInfoMap.containsKey(position)) {
                                OrderInfo orderInfo = orderInfoMap.get(position);
                                orderInfo.setWishType("0,4l");
                                updateOrder(position);
                            } else {
                                orderInfoMap.put(position, new OrderInfo(0, "0,4l"));
                                updateOrder(position);
                            }
                        }
                    }

                }
            });
            checkBox.setOnClickListener(
                    v -> {
                        String tableId = MenuDrinkTwoFourAdapter.this.TableID;
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        String wish = getOrder().getwishfood_typdrink();
                        if(getOrder().getNumber()>0 && (wish.equals("0,2l") || wish.equals("0,4l"))) {
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
                String selectedPrice = smallSize.isChecked() ? priceSmall.getText().toString() :
                        bigSize.isChecked() ? priceBig.getText().toString() : "Error";
                if (orderInfo != null && orderInfo.getNumber() > 0 && !selectedPrice.equals("Error")) {
                    DrinkVarietyTwoFour food = drinks.get(position);
                    Order checked_order = new Order(food.getDrinkName(),
                            orderInfo.getNumber(),
                            selectedPrice,
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
                else {
                    DrinkVarietyTwoFour food = drinks.get(position);
                    Order checked_order = new Order(food.getDrinkName(),
                            0,
                            "Error",
                            "no wish");
                    setOrder(checked_order);
                }
            }
        }
    }

}

