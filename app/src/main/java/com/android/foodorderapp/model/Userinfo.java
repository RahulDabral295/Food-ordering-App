package com.android.foodorderapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Userinfo implements Parcelable {
    private String  Name,Phone_no,Card_no,Card_exp,Card_cvv,Address,Pin,City,State;
    public Userinfo(){}


    public Userinfo(String name, String phone_no, String card_no, String card_exp, String card_cvv, String address, String pin, String city, String state) {
        Name = name;
        Phone_no = phone_no;
        Card_no = card_no;
        Card_exp = card_exp;
        Card_cvv = card_cvv;
        Address = address;
        Pin = pin;
        City = city;
        State = state;
    }




    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone_no() {
        return Phone_no;
    }

    public void setPhone_no(String phone_no) {
        Phone_no = phone_no;
    }

    public String getCard_no() {
        return Card_no;
    }

    public void setCard_no(String card_no) {
        Card_no = card_no;
    }

    public String getCard_exp() {
        return Card_exp;
    }

    public void setCard_exp(String card_exp) {
        Card_exp = card_exp;
    }

    public String getCard_cvv() {
        return Card_cvv;
    }

    public void setCard_cvv(String card_cvv) {
        Card_cvv = card_cvv;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public static Creator<Userinfo> getCREATOR() {
        return CREATOR;
    }



    public static final Parcelable.Creator<Userinfo> CREATOR = new Parcelable.Creator<Userinfo>() {

        @Override
        public Userinfo createFromParcel(Parcel parcel) {
            return new Userinfo(parcel);
        }

        @Override
        public Userinfo[] newArray(int size) {
            return new Userinfo[0];
        }
    };



    public Userinfo(Parcel parcel) {
        Name =parcel.readString();
        Phone_no = parcel.readString();
        Card_no =parcel.readString();
        Card_exp =parcel.readString();
        Card_cvv =parcel.readString();
        Address = parcel.readString();
        Pin = parcel.readString();
        City = parcel.readString();
        State = parcel.readString();


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(Phone_no);
        dest.writeString(Card_no);
        dest.writeString(Card_exp);
        dest.writeString(Card_cvv);
        dest.writeString(Address);
        dest.writeString(Pin);
        dest.writeString(City);
        dest.writeString(State);

    }
}
