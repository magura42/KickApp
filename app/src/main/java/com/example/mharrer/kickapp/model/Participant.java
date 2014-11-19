package com.example.mharrer.kickapp.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mharrer on 11.11.14.
 */
public class Participant implements Parcelable {

    private String firstName;

    private String lastName;

    private String phonenumber;

    private Bitmap photo;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Participant(String firstName, String lastName, String phonenumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phonenumber = phonenumber;
    }

    public Participant(String firstName, String lastName, String phonenumber, long id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phonenumber = phonenumber;
        this.id = id;
    }

    protected Participant(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        firstName = in.readString();
        lastName = in.readString();
        phonenumber = in.readString();
        photo = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }



    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(id);
        }
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(phonenumber);
        dest.writeValue(photo);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        @Override
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        @Override
        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };
}