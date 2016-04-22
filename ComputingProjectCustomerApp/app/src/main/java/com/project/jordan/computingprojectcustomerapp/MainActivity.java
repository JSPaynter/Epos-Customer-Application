package com.project.jordan.computingprojectcustomerapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //array lists of meals
    ArrayList<Meal> allMeals = new ArrayList<>();
    ArrayList<Meal> nonAlcoholic = new ArrayList<>();
    ArrayList<Meal> alcoholic = new ArrayList<>();
    ArrayList<Meal> starter = new ArrayList<>();
    ArrayList<Meal> main = new ArrayList<>();
    ArrayList<Meal> dessert = new ArrayList<>();
    ArrayList<Meal> side = new ArrayList<>();
    ArrayList<Meal> snack = new ArrayList<>();
    ArrayList<BillOrder> newItems = new ArrayList<>();
    ArrayList<String> orderArray = new ArrayList<>();

    ArrayAdapter<String> orderAdapter;

    ScrollView mealView;
    ListView orderList;
    TextView txtTotal;
    TextView txtTable;

    int course = 0;
    Bill currentBill;
    int tableNo = 1111;
    int index = 0;

    String[] staffOptions = new String[4];
    String password = "123";
    LinearLayout layout;

    //attributes on page
    ToggleButton btnTogStart, btnTogMain, btnTogDessert;
    Button btnSplitPerson, btnSplitItem, btnConfirm, btnRemove, btnStaff, btnNonAlcoholic, btnAlcoholic,
        btnStarter, btnMain, btnDessert, btnSide, btnSnack;
    ListView lstOrder;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set local fields
        setLocalAssets();

        new getAllMealList().execute(new APIConnector()); //get meals, sorts in background

        //set staff option array list
        staffOptions[0] = "Reset Table";
        staffOptions[1] = "Change TableNo";
        staffOptions[2] = "Change Password";
        staffOptions[3] = "ShutDown";

        //set up page
        setUpListAdapter();
        setListeners();
        layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        mealView.addView(layout);
        setTogButtons(false);
        generateButtons(nonAlcoholic);
        startUpTableSetup();
    }

    private void startUpTableSetup() {
        AlertDialog.Builder dialogTableNo = new AlertDialog.Builder(MainActivity.this);
        dialogTableNo.setTitle("New TableNo");
        dialogTableNo.setMessage("Please enter a table number.");

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogTableNo.setView(input);

        dialogTableNo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newTableNo;
                newTableNo = Integer.parseInt(input.getText().toString());
                tableNo = newTableNo;
                currentBill = new Bill(tableNo);
                setLabelTableNo();
                Toast.makeText(MainActivity.this, "Table No set", Toast.LENGTH_SHORT).show();
                new addBill().execute(new APIConnector());
            }
        });
        dialogTableNo.show();
    }

    private void setUpListAdapter() {
        orderAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.list_view_text, orderArray);
        orderArray.add("----------New Items Below ----------");
        lstOrder.setAdapter(orderAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setListeners() {
        btnNonAlcoholic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(nonAlcoholic);
                setTogButtons(false);
                btnTogStart.setChecked(false);
                btnTogMain.setChecked(false);
                btnTogDessert.setChecked(false);
                course = 0;
            }
        });
        btnAlcoholic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(alcoholic);
                setTogButtons(false);
                btnTogStart.setChecked(false);
                btnTogMain.setChecked(false);
                btnTogDessert.setChecked(false);
                course = 0;
            }
        });
        btnStarter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(starter);
                setTogButtons(true);
                btnTogStart.setChecked(true);
                btnTogMain.setChecked(false);
                btnTogDessert.setChecked(false);
                course = 1;
            }
        });
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(main);
                setTogButtons(false);
                btnTogMain.setEnabled(true);
                btnTogStart.setChecked(false);
                btnTogMain.setChecked(true);
                btnTogDessert.setChecked(false);
                course = 2;
            }
        });
        btnDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(dessert);
                setTogButtons(true);
                btnTogStart.setChecked(false);
                btnTogMain.setChecked(false);
                btnTogDessert.setChecked(true);
                course = 3;
            }
        });
        btnSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(side);
                setTogButtons(true);
                btnTogStart.setChecked(false);
                btnTogMain.setChecked(true);
                btnTogDessert.setChecked(false);
                if (course == 0)
                    course = 2;
            }
        });
        btnSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateButtons(snack);
                setTogButtons(true);
                btnTogStart.setChecked(false);
                btnTogMain.setChecked(true);
                btnTogDessert.setChecked(false);
                if (course == 0)
                    course = 2;
            }
        });

        btnTogStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (btnTogStart.isChecked()) {
                btnTogMain.setChecked(false);
                btnTogDessert.setChecked(false);
                course = 1;
            } else
                btnTogStart.setChecked(true);
            }
        });
        btnTogMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnTogMain.isChecked()) {
                    btnTogStart.setChecked(false);
                    btnTogDessert.setChecked(false);
                    course = 2;
                } else
                    btnTogMain.setChecked(true);
            }
        });
        btnTogDessert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnTogDessert.isChecked()) {
                    btnTogStart.setChecked(false);
                    btnTogMain.setChecked(false);
                    course = 3;
                } else
                    btnTogDessert.setChecked(true);
            }
        });
        btnSplitPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] inputText = {""};
                //build dialog question box
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Split Bill");
                builder.setMessage("How many people would you like to split between?");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //0 people = na result
                        inputText[0] = input.getText().toString();
                        Double perPerson = currentBill.getTotal() / Double.parseDouble(inputText[0]);

                        AlertDialog.Builder dialogBox = new AlertDialog.Builder(MainActivity.this);
                        dialogBox.setTitle("Split per person");
                        dialogBox.setMessage("Your bill will cost you £" + perPerson + " each.");
                        dialogBox.show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        btnSplitItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent billSplitActivity = new Intent(MainActivity.this, SplitBill.class);
                billSplitActivity.putExtra( "currentOrder", currentBill.getBillOrders());
                billSplitActivity.putExtra( "allMeals", allMeals);
                startActivity(billSplitActivity);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (BillOrder order : newItems)
                    new addBillOrder(order).execute(new APIConnector()); //send order to database
                new updateBill().execute(new APIConnector());
                currentBill.setNoOfItems(currentBill.getNoOfItems() + newItems.size());
                for (BillOrder item : newItems) //confirm to current bill
                    currentBill.getBillOrders().add(item);
                newItems.clear();
                refreshOrderList();
                Toast.makeText(MainActivity.this, "Order Placed", Toast.LENGTH_SHORT).show(); //confirm
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItemSize = currentBill.getBillOrders().size()+1;
                if (index < currentItemSize) {
                    Toast.makeText(MainActivity.this, "You can only remove new items", Toast.LENGTH_SHORT).show();
                } else {
                    double price = 0.00;
                    for (Meal meal : allMeals)
                        if (meal.getMealID() == newItems.get(index - currentItemSize).getMealID())
                            price = meal.getPrice();
                    currentBill.setTotal(currentBill.getTotal() - price);
                    setLabelTotal();
                    newItems.remove(index - currentItemSize);
                    removeFromOrderList(index);
                    index = 0;
                }
            }
        });
        btnStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] inputText = {""};
                //build dialog question box
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Staff Login");
                builder.setMessage("Please enter the password");

                // Set up the input
                final EditText input = new EditText(MainActivity.this);
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        inputText[0] = input.getText().toString();
                        if (inputText[0].equalsIgnoreCase(password)) { //hard coded password set on startup
                            staffDialogOptions();
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        lstOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
            }
        });
    }

    private void setLocalAssets() {
        mealView = (ScrollView) findViewById(R.id.scrMeal);
        orderList = (ListView) findViewById(R.id.lstOrder);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTable = (TextView) findViewById(R.id.lblTableNo);
        lstOrder = (ListView) findViewById(R.id.lstOrder);

        btnNonAlcoholic = (Button) findViewById(R.id.btnNonAlcoholic);
        btnAlcoholic = (Button) findViewById(R.id.btnAlcoholic);
        btnStarter = (Button) findViewById(R.id.btnStarters);
        btnMain = (Button) findViewById(R.id.btnMains);
        btnDessert = (Button) findViewById(R.id.btnDesserts);
        btnSide = (Button) findViewById(R.id.btnSides);
        btnSnack = (Button) findViewById(R.id.btnSnacks);

        btnTogStart = (ToggleButton) findViewById(R.id.btnToggleStarter);
        btnTogMain = (ToggleButton) findViewById(R.id.btnToggleMain);
        btnTogDessert = (ToggleButton) findViewById(R.id.btnToggleDessert);
        btnSplitPerson = (Button) findViewById(R.id.btnSplitPerson);
        btnSplitItem = (Button) findViewById(R.id.btnSplitItem);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnRemove = (Button) findViewById(R.id.btnRemove);
        btnStaff = (Button) findViewById(R.id.btnStaff);
    }

    private void staffDialogOptions() {
        AlertDialog.Builder dialogBox = new AlertDialog.Builder(MainActivity.this);
        dialogBox.setTitle("Staff Options");
        dialogBox.setItems(staffOptions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //reset table
                        staffResetTable();
                        break;
                    case 1: //change tableNo
                        staffChangeTableNo();
                        break;
                    case 2: //change password
                        staffChangePassword();
                        break;
                    case 3: // shutdown
                        android.os.Process.killProcess(Process.myPid());
                        System.exit(1);
                        break;
                }
            }
        });
        dialogBox.show();
    }

    private void staffChangePassword() {
        AlertDialog.Builder dialogPassword = new AlertDialog.Builder(MainActivity.this);
        dialogPassword.setTitle("New Password");
        dialogPassword.setMessage("Please enter a new numerical password.");

        final EditText newPassword = new EditText(MainActivity.this);
        newPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogPassword.setView(newPassword);

        dialogPassword.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                password = newPassword.getText().toString();
                Toast.makeText(MainActivity.this, "Password Changed", Toast.LENGTH_SHORT).show();
            }
        });
        dialogPassword.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogPassword.show();
    }

    private void staffChangeTableNo() {
        AlertDialog.Builder dialogTableNo = new AlertDialog.Builder(MainActivity.this);
        dialogTableNo.setTitle("New TableNo");
        dialogTableNo.setMessage("Please enter a new table number.");

        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        dialogTableNo.setView(input);

        dialogTableNo.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int newTableNo;
                newTableNo = Integer.parseInt(input.getText().toString());
                tableNo = newTableNo;
                currentBill.setTableNo(newTableNo);
                setLabelTableNo();
                new updateTableNo().execute(new APIConnector());
                Toast.makeText(MainActivity.this, "Table No changed", Toast.LENGTH_SHORT).show();

            }
        });
        dialogTableNo.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialogTableNo.show();
    }

    private void staffResetTable() {
        currentBill = new Bill(tableNo);
        new addBill().execute(new APIConnector());
        setLabelTotal();
        orderArray.clear();
        addToOrderList("----------New Items Below ----------");
        generateButtons(nonAlcoholic);
        setTogButtons(false);
        btnTogStart.setChecked(false);
        btnTogMain.setChecked(false);
        btnTogDessert.setChecked(false);
        course = 0;
    }

    private void setTogButtons(Boolean onOff) {
        btnTogStart.setEnabled(onOff);
        btnTogMain.setEnabled(onOff);
        btnTogDessert.setEnabled(onOff);
    }

    private void sortLists() {
        for (Meal meal : allMeals) {
            if (meal.getMenu() == 1) {
                switch (meal.getMealTypeID()) {
                    case 1:
                        nonAlcoholic.add(meal);
                        break;
                    case 2:
                        alcoholic.add(meal);
                        break;
                    case 3:
                        starter.add(meal);
                        break;
                    case 4:
                        main.add(meal);
                        break;
                    case 5:
                        dessert.add(meal);
                        break;
                    case 6:
                        side.add(meal);
                        break;
                    case 7:
                        snack.add(meal);
                        break;
                }
            }
        }
    }

    private void generateButtons(ArrayList<Meal> meals) {

        layout.removeAllViews();

        for (final Meal meal : meals) {

            TextView mealName = new TextView(MainActivity.this);
            mealName.setText(String.format(meal.getName() + " - £%.2f", meal.getPrice()));
            mealName.setPadding(5, 2, 0, 2);
            mealName.setTextSize(12);
            mealName.setTextColor(Color.parseColor("#000000"));

            TextView description = new TextView(MainActivity.this);
            description.setText(meal.getDescription());
            description.setPadding(5, 2, 0, 2);
            description.setTextSize(8);
            description.setTextColor(Color.parseColor("#000000"));

            Button btnAdd = new Button(MainActivity.this);
            btnAdd.setLayoutParams(new LinearLayout.LayoutParams(100,60));
            btnAdd.setMaxLines(1);
            btnAdd.setText("Order");
            btnAdd.setTextSize(8);
            btnAdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String mealType = "";
                    switch (course) {
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

                    BillOrder order = new BillOrder(currentBill.getBillID(), meal.getMealID(), 0, course);
                    newItems.add(order);
                    mealType += String.format(meal.getName() + " £%.2f", meal.getPrice());
                    addToOrderList(mealType);
                    currentBill.setTotal(currentBill.getTotal() + meal.getPrice());
                    setLabelTotal();
                }
            });
            layout.addView(mealName);
            layout.addView(description);
            layout.addView(btnAdd);
        }
    }

    private class getAllMealList extends AsyncTask<APIConnector, Long, Void> {

        @Override
        protected Void doInBackground(APIConnector... apiConnectors) {

            allMeals = apiConnectors[0].getAllMeals();
            sortLists();
            return null;
        }
    }

    private class updateTableNo extends AsyncTask<APIConnector, Long, Void> {

        @Override
        protected Void doInBackground(APIConnector... apiConnectors) {

            apiConnectors[0].updateTableNo(currentBill.getBillID(), currentBill.getTableNo());
            return null;
        }
    }

    private class addBill extends AsyncTask<APIConnector, Long, Void> {

        @Override
        protected Void doInBackground(APIConnector... apiConnectors) {

            int billID = (apiConnectors[0].addBill(currentBill.getTableNo()));
            if (billID != 0) {
                currentBill.setBillID(billID);
            }
            else
                Toast.makeText(MainActivity.this, "Failed creating new table", Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private class addBillOrder extends AsyncTask<APIConnector, Long, Void> {
        private BillOrder order;
        public addBillOrder(BillOrder order) {
            this.order = order;
        }

        @Override
        protected Void doInBackground(APIConnector... apiConnectors) {
            apiConnectors[0].addBillOrder(order);
            return null;
        }
    }

    private class updateBill extends AsyncTask<APIConnector, Long, Void> {

        @Override
        protected Void doInBackground(APIConnector... apiConnectors) {

            apiConnectors[0].updateBill(currentBill);
            return null;
        }
    }

    public void setLabelTotal() {
        txtTotal.setText(String.format("Your Total : £%.2f", currentBill.getTotal()));
    }

    public void setLabelTableNo() {
        txtTable.setText("Table No : " + tableNo);
    }


    private void refreshOrderList() {
        ArrayList<String> holder = new ArrayList<>(); //holder array
        for (String string : orderArray)
            holder.add(string);
        orderArray.clear();
        int drinks = 0, starters = 0, mains = 0, desserts = 0; //counters
        for (String item : holder) {
            char second = item.charAt(1);
            String characters = "" + second;

            switch (characters) {
                case "r": //drink
                    orderArray.add(drinks, item);
                    drinks += 1;
                    break;
                case "t": //starter
                    orderArray.add(drinks + starters, item);
                    starters += 1;
                    break;
                case "a": //main
                    orderArray.add(drinks + starters + mains, item);
                    mains += 1;
                    break;
                case "e": //dessert
                    orderArray.add(drinks + starters + mains + desserts, item);
                    desserts += 1;
                    break;
                default:
                    break;
            }
        }
        addToOrderList("----------New Items Below ----------");
    }

    private void addToOrderList(String order) {
        orderArray.add(order);
        orderAdapter.notifyDataSetChanged();
    }
    private void removeFromOrderList(int index) {
        orderArray.remove(index);
        orderAdapter.notifyDataSetChanged();
    }
}
