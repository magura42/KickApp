package com.example.mharrer.kickapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Team implements Parcelable {

    private String name;

    private Coach coach;

    private Long id;

    private List<Participant> teammembers = new ArrayList<Participant>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Participant> getTeammembers() {
        return teammembers;
    }

    public Long getId() {
        return id;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTeammembers(List<Participant> teammembers) {
        this.teammembers = teammembers;
    }

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addTeammember(Participant participant) {
        this.teammembers.add(participant);
    }

    public void removeTeammember(Participant participant) {
        this.teammembers.remove(participant);
    }

    protected Team(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readLong();
        name = in.readString();
        coach = (Coach) in.readValue(Coach.class.getClassLoader());
        if (in.readByte() == 0x01) {
            teammembers = new ArrayList<Participant>();
            in.readList(teammembers, Participant.class.getClassLoader());
        } else {
            teammembers = null;
        }
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
        dest.writeValue(coach);
        if (teammembers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(teammembers);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Team> CREATOR = new Parcelable.Creator<Team>() {
        @Override
        public Team createFromParcel(Parcel in) {
            return new Team(in);
        }

        @Override
        public Team[] newArray(int size) {
            return new Team[size];
        }
    };
}