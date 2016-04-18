package com.project.jordan.computingprojectcustomerapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class APIConnector {

    URL url;
    HttpURLConnection con;
    String hostAddress = "http://10.0.2.2:8080/IntergratedProjectPHPPages/JSP_Pages/";

    //number of collums in each table for read alls
    int billCollums = 8;
    int billOrderCollums = 5;
    int ingredientCollums = 7;
    int madeIngredientCollums = 4;
    int madeProduceCollums = 3;
    int mealCollums = 6;
    int mealTypeCollums = 2;
    int mealIngredientCollums = 6;
    int orderStockCollums = 4;
    int orderCollums = 9;
    int staffCollums = 8;
    int supplierCollums = 8;

    public ArrayList<Meal> getAllMeals() {

        ArrayList<Meal> meals = new ArrayList<>();
        try {
            url = new URL(hostAddress + "MealJSP/ReadMeal.jsp");
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");

            InputStream in = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isr);
            String result = "", data;

            while ((data = reader.readLine()) != null) {
                result += data;
            }

            String[] results = stringBreaker(result);

            for (int i = 0; i < results.length / mealCollums; i++) {
                meals.add(new Meal(Integer.parseInt(results[(i * mealCollums)]), results[(i * mealCollums) + 1],
                        Double.parseDouble(results[(i * mealCollums) + 2]), results[(i * mealCollums) + 3],
                        Integer.parseInt(results[(i * mealCollums) + 4]), Integer.parseInt(results[(i * mealCollums) + 5])));
            }
            con.getResponseCode();
            con.disconnect();
            return meals;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return meals;
    }

    public int addBill(int tableNo) {
        try {
            url = new URL(hostAddress + "BillJSP/AddBill.jsp");
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");

            //2 array lists for key and values
            ArrayList<String> key = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            //add keys and values to list
            key.add("tableNo");
            values.add(String.valueOf(tableNo));
            String postData = parameterString(key, values);

            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes());
            os.close();

            InputStream in = con.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(isr);
            String result = "", data;

            while ((data = reader.readLine()) != null) {
                result += data;
            }
            con.getResponseCode();
            con.disconnect();
            String[] results = stringBreaker(result);
            return Integer.parseInt(results[0]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateTableNo(int billID, int tableNo) {
        try {
            url = new URL(hostAddress + "BillJSP/UpdateTableNo.jsp");
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");

            //2 array lists for key and values
            ArrayList<String> key = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            //add keys and values to list
            key.add("billID");
            key.add("tableNo");
            values.add(String.valueOf(billID));
            values.add(String.valueOf(tableNo));
            String postData = parameterString(key, values);

            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes());
            os.close();
            con.getResponseCode();

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBillOrder(BillOrder item) {
        try {
            url = new URL(hostAddress + "BillOrderJSP/AddBillOrder.jsp");
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");

            //2 array lists for key and values
            ArrayList<String> key = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            //add keys and values to list
            key.add("billID");
            key.add("mealID");
            key.add("complete");
            key.add("course");
            values.add(String.valueOf(item.getBillID()));
            values.add(String.valueOf(item.getMealID()));
            values.add(String.valueOf(item.getComplete()));
            values.add(String.valueOf(item.getCourse()));

            String postData = parameterString(key, values);

            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes());
            os.close();
            con.getResponseCode();

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBill(Bill currentBill) {
        try {
            url = new URL(hostAddress + "BillJSP/UpdateBill.jsp");
            con = (HttpURLConnection) url.openConnection();

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestMethod("GET");

            //2 array lists for key and values
            ArrayList<String> key = new ArrayList<>();
            ArrayList<String> values = new ArrayList<>();

            //add keys and values to list
            key.add("billID");
            key.add("noOfItems");
            key.add("total");
            key.add("cash");
            key.add("card");
            values.add(String.valueOf(currentBill.getBillID()));
            values.add(String.valueOf(currentBill.getNoOfItems()));
            values.add(String.valueOf(currentBill.getTotal()));
            values.add(String.valueOf(currentBill.getPaidCash()));
            values.add(String.valueOf(currentBill.getPaidCard()));

            String postData = parameterString(key, values);

            OutputStream os = con.getOutputStream();
            os.write(postData.getBytes());
            os.close();
            con.getResponseCode();

            con.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] stringBreaker(String inString) {
        String[] result;

        result = inString.split("break");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim().replaceAll("\\s{2,}", "");
        }

        return result;
    }

    public String parameterString(ArrayList<String> key, ArrayList<String> values) {

        String postData = "";

        for (int i = 0; i < key.size(); i++) {
            postData += key.get(i);
            postData += "=";
            postData += values.get(i);
            postData += "&";
        }

        return postData;
    }
}
