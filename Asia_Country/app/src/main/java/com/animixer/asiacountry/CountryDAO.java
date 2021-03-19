package com.animixer.asiacountry;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDAO {

    @Query("SELECT * FROM country")
    List<Country> getAllCountry();

    @Insert
    void insertCountry(Country... countries);

    @Delete
    void delete(Country country);

    @Query("SELECT COUNT(*) FROM country")
    int getCount();

    @Query("DELETE FROM country")
    void deleteAll();
}
