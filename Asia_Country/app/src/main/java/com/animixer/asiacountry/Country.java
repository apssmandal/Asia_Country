package com.animixer.asiacountry;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Country {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "country_name")
    public String Name;

    @ColumnInfo(name = "country_capital")
    public String Capital;


    @ColumnInfo(name = "country_image")
    public String Image;

    @ColumnInfo(name = "country_language")
    public String Language;

    @ColumnInfo(name = "country_population")
    public String Population;

    @ColumnInfo(name = "country_region")
    public String Region;

    @ColumnInfo(name = "country_subregion")
    public String SubRegion;

    @ColumnInfo(name = "country_borders")
    public String Borders;

}
