package com.example.mharrer.kickapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by mharrer on 10.11.14.
 */
public class Training implements Parcelable {

    private String name;

    private String date;

    private String startTime;

    private String endTime;

    private String location;

    private Coach coach;

    private Team team;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Training(String date, String name) {
        this.name = name;
        this.date = date;
    }

    public Training(String date, String name, long id) {
        this.name = name;
        this.date = date;
        this.id = id;
    }

    protected Training(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        name = in.readString();
        date = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        location = in.readString();
        coach = (Coach) in.readValue(Coach.class.getClassLoader());
        team = (Team) in.readValue(Team.class.getClassLoader());
    }

    @Override
    public String toString() {
        return name + ", " + date;
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
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(location);
        dest.writeValue(coach);
        dest.writeValue(team);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Training> CREATOR = new Parcelable.Creator<Training>() {
        @Override
        public Training createFromParcel(Parcel in) {
            return new Training(in);
        }

        @Override
        public Training[] newArray(int size) {
            return new Training[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}