package com.project.jordan.computingprojectcustomerapp;

import android.os.Parcel;
import android.os.Parcelable;

public class BillOrder implements Parcelable{

    private int itemID, billID, mealID, complete, course;

    public BillOrder(int itemID, int billID, int mealID, int complete, int course) {
        this.itemID = itemID;
        this.billID = billID;
        this.mealID = mealID;
        this.complete = complete;
        this.course = course;
    }
    public BillOrder(int billID, int mealID, int complete, int course) {
        this.billID = billID;
        this.mealID = mealID;
        this.complete = complete;
        this.course = course;
    }

    protected BillOrder(Parcel in) {
        itemID = in.readInt();
        billID = in.readInt();
        mealID = in.readInt();
        complete = in.readInt();
        course = in.readInt();
    }

    public static final Creator<BillOrder> CREATOR = new Creator<BillOrder>() {
        @Override
        public BillOrder createFromParcel(Parcel in) {
            return new BillOrder(in);
        }

        @Override
        public BillOrder[] newArray(int size) {
            return new BillOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(itemID);
        dest.writeInt(billID);
        dest.writeInt(mealID);
        dest.writeInt(complete);
        dest.writeInt(course);
    }

    public int getItemID() {
        return itemID;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
    public int getBillID() {
        return billID;
    }
    public void setBillID(int billID) {
        this.billID = billID;
    }
    public int getMealID() {
        return mealID;
    }
    public void setMealID(int mealID) {
        this.mealID = mealID;
    }
    public int getComplete() {
        return complete;
    }
    public void setComplete(int complete) {
        this.complete = complete;
    }
    public int getCourse() {
        return course;
    }
    public void setCourse(int course) {
        this.course = course;
    }
}
