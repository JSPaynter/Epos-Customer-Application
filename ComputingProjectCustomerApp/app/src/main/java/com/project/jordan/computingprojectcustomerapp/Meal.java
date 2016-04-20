package com.project.jordan.computingprojectcustomerapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Meal implements Parcelable{

        private int mealID, mealTypeID, menu;
        private String name, description;
        private double price;

        public Meal(int mealID, String name, double price, String description, int mealTypeID, int menu) {
            this.mealID = mealID;
            this.mealTypeID = mealTypeID;
            this.menu = menu;
            this.name = name;
            this.description = description;
            this.price = price;
        }

        public Meal(int mealTypeID, int menu, String name, String description, double price) {
            this.mealTypeID = mealTypeID;
            this.menu = menu;
            this.name = name;
            this.description = description;
            this.price = price;
        }

    protected Meal(Parcel in) {
        mealID = in.readInt();
        mealTypeID = in.readInt();
        menu = in.readInt();
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mealID);
        dest.writeInt(mealTypeID);
        dest.writeInt(menu);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
    }

    public int getMealID() {
            return mealID;
        }
        public void setMealID(int mealID) {
            this.mealID = mealID;
        }
        public int getMealTypeID() {
            return mealTypeID;
        }
        public void setMealTypeID(int mealTypeID) {
            this.mealTypeID = mealTypeID;
        }
        public int getMenu() {
            return menu;
        }
        public void setMenu(int menu) {
            this.menu = menu;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public double getPrice() {
            return price;
        }
        public void setPrice(double price) {
            this.price = price;
        }
}
