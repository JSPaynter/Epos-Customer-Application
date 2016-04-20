package com.project.jordan.computingprojectcustomerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SplitBill extends AppCompatActivity {

    ListView lstBill1, lstBill2;
    Button btnReset, btnClose;
    TextView txtTotal1, txtTotal2;
    Double total1 = 0.00, total2 = 0.00;

    ArrayList<Meal> allMeals = new ArrayList<>();
    ArrayList<BillOrder> originalList = new ArrayList<>(), orderList1 = new ArrayList<>(), orderList2 = new ArrayList<>();
    ArrayList<String> order1 = new ArrayList<>(), order2 = new ArrayList<>();
    ArrayAdapter<String> orderAdapter1, orderAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_split_bill);

        Intent extras = getIntent();
        if (extras != null) {
            allMeals = (ArrayList<Meal>) extras.getSerializableExtra("allMeals");
            originalList = (ArrayList<BillOrder>) extras.getSerializableExtra("currentOrder");
        }

        setLocalAssets();
        setAdapters();
        setListeners();

        produceFirstList();
    }

    private void setAdapters() {
        orderAdapter1 = new ArrayAdapter<>(SplitBill.this, R.layout.list_view_text, order1);
        lstBill1.setAdapter(orderAdapter1);
        orderAdapter2 = new ArrayAdapter<>(SplitBill.this, R.layout.list_view_text, order2);
        lstBill2.setAdapter(orderAdapter2);
    }

    private void setLocalAssets() {
        lstBill1 = (ListView) findViewById(R.id.lstBill1);
        lstBill2 = (ListView) findViewById(R.id.lstBill2);

        btnReset = (Button) findViewById(R.id.btnReset);
        btnClose = (Button) findViewById(R.id.btnClose);

        txtTotal1 = (TextView) findViewById(R.id.txtTotal1);
        txtTotal2 = (TextView) findViewById(R.id.txtTotal2);
    }

    private void setListeners() {
        lstBill1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    orderList2.add(orderList1.get(position));
                    addToOrder2(order1.get(position));

                    Double price = 0.00;
                    int mealID = orderList1.get(position).getMealID();
                    for (Meal meal : allMeals) {
                        if (mealID == meal.getMealID())
                            price = meal.getPrice();
                    }
                    total1 -= price;
                    total2 += price;
                    setTotalLabels();

                    orderList1.remove(position);
                    removeFromOrder1(position);
                }
            }
        });
        lstBill2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    orderList1.add(orderList2.get(position));
                    addToOrder1(order2.get(position));

                    Double price = 0.00;
                    int mealID = orderList2.get(position).getMealID();
                    for (Meal meal : allMeals) {
                        if (mealID == meal.getMealID())
                            price = meal.getPrice();
                    }
                    total1 += price;
                    total2 -= price;
                    setTotalLabels();

                    orderList2.remove(position);
                    removeFromOrder2(position);
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order1.clear();
                order2.clear();
                orderList1.clear();
                orderList2.clear();
                total1 = 0.00;
                total2 = 0.00;
                produceFirstList();
            }
        });
    }

    private void produceFirstList() {
        for (BillOrder item : originalList) {
            orderList1.add(item);
            String mealType = "";
            switch (item.getCourse()) {
                case 0:
                    mealType = "Drink - ";
                    break;
                case 1:
                    mealType = "Starter - ";
                    break;
                case 2:
                    mealType = "Main - ";
                    break;
                case 3:
                    mealType = "Dessert - ";
                    break;
            }
            for (Meal searchMeal : allMeals) {
                if (item.getMealID() == searchMeal.getMealID()) {
                    mealType += String.format(searchMeal.getName() + " £%.2f", searchMeal.getPrice());
                    total1 += searchMeal.getPrice();
                }
            }
            addToOrder1(mealType);
        }
        setTotalLabels();
    }

    private void setTotalLabels() {
        txtTotal1.setText(String.format("Bill 1 Total : £%.2f", total1));
        txtTotal2.setText(String.format("Bill 2 Total : £%.2f", total2));
    }

    private void addToOrder1(String input) {
        order1.add(input);
        orderAdapter1.notifyDataSetChanged();
    }
    private void removeFromOrder1(int index) {
        order1.remove(index);
        orderAdapter1.notifyDataSetChanged();
    }
    private void addToOrder2(String input) {
        order2.add(input);
        orderAdapter2.notifyDataSetChanged();
    }
    private void removeFromOrder2(int index) {
        order2.remove(index);
        orderAdapter2.notifyDataSetChanged();
    }
}
