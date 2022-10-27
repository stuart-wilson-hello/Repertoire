package com.stuart.repetoire_v1_0;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Recipe implements Parcelable, Serializable {
    String name;
    String link;

    public Recipe(String Name, String Link)
    {
        name=Name;
        link=Link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[] {this.name, this.link});
    }

    public static final Parcelable.Creator<Recipe> CREATOR
            = new Parcelable.Creator<Recipe>() {
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    private Recipe(Parcel in) {
        String[] data = new String[2];

        in.readStringArray(data);
        this.name=data[0];
        this.link=data[1];
    }
}
