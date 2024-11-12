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
import com.example.orderrestaurantapp.menu.DrinkThreeFive;
import com.example.orderrestaurantapp.menu.DrinkVarietyTwoFour;
import com.example.orderrestaurantapp.menu.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MenuDrinkThreeFiveAdapter extends RecyclerView.Adapter<MenuDrinkThreeFiveAdapter.ViewHolder> {
    private final List<DrinkThreeFive> drinks;
    private List<DrinkThreeFive> drinksfull;
    public Context context;
    String TableID;
    FragmentActivity activity;
    android.content.res.Resources resources;
    public MenuDrinkThreeFiveAdapter (Context context, List<DrinkThreeFive> drinks, String TableId, FragmentActivity activity, android.content.res.Resources resources){
        this.drinks = drinks;
        this.context = context;
        this.TableID = TableId;
        this.activity = activity;
        this.resources = resources;
        drinksfull = new ArrayList<>(drinks);
    }


    @NonNull
    @Override
    public MenuDrinkThreeFiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_drink_with_varity, parent,false);

        return new MenuDrinkThreeFiveAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MenuDrinkThreeFiveAdapter.ViewHolder holder, int position) {
        DrinkThreeFive table = drinks.get(position);
        holder.nameOfDrink.setText(table.getDrinkName());
        holder.priceBig.setText(table.getPriceVarietyZeroFive());
        holder.priceSmall.setText(table.getPriceVarietyZeroThree());
        holder.smallSize.setText("0,3L");
        holder.bigSize.setText("0,5L");
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
            for (DrinkThreeFive drink : drinksfull) {
                if (drink.getDrinkName().toLowerCase().contains(searchQuery.toLowerCase())) {
                    drinks.add(drink);
                }
            }
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameOfDrink;
        RadioButton smallSize;
        ImageButton checkBox;
        RadioButton bigSize;
        TextView priceSmall;
        TextView priceBig;
        NumberPicker numberPicker;
        int number;
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
            numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    number = newVal;
                    updateOrder();
                }
            });
            smallSize.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        updateOrder();
                    }
                }
            });
            checkBox = itemView.findViewById(R.id.checkbox_drink);

            checkBox.setOnClickListener(
                    v -> {
                        String tableId = MenuDrinkThreeFiveAdapter.this.TableID;
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        String wish = getOrder().getwishfood_typdrink();
                        if(getOrder().getNumber()>0 && (wish.equals("0,3l") || wish.equals("0,5l"))) {
                            Database.putDataInFirebase(database, getOrder(), tableId, activity, resources);
                        } else Database.setSnackbar(activity.findViewById(R.id.container), resources.getString(R.string.order_add_no_number));
                        // Ensure the position is valid
                        notifyDataSetChanged();
                    }
            );

        }

        private void updateOrder() {
            String selectedPrice = smallSize.isChecked() ? priceSmall.getText().toString() :
                    bigSize.isChecked()? priceBig.getText().toString() : "Error";
            String typ = smallSize.isChecked() ? "0,3l" : bigSize.isChecked()? "0,5l" : "No Typ";

            if (number > 0 && !(selectedPrice.equals("Error"))) {
                Order checked_order = new Order(nameOfDrink.getText().toString(),
                        number,
                        selectedPrice,
                        typ);
                checked_order.setTotalPrice(checked_order.getPrice(), checked_order.getNumber());
                // You might want to remove any existing order for this drink item before adding a new one
                setOrder(new Order(checked_order.getOrdername(),
                        checked_order.getNumber(),
                        checked_order.getPrice(),
                        checked_order.getTotalPrice(),
                        checked_order.getwishfood_typdrink(),
                        false,
                        false));
            }
            else {
                Order checked_order = new Order(nameOfDrink.getText().toString(),
                        0,
                        "Error",
                        "no wish");
                setOrder(checked_order);
            }
        }


    }

}
