package com.joaopirolo.diario_de_trabalho.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;
@Entity(tableName = "servicos", foreignKeys = @ForeignKey(entity = CategoriaDeServicos.class,
                                                          parentColumns = "id",
                                                          childColumns = "categoriaDeServicoId"))
public class Servicos implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String numberBa;

    @NonNull
    private String city;

    @NonNull
    private String ard;

    public Servicos (String numberBa, String city, String ard){
        setNumberBa(numberBa);
        setCity(city);
        setArd(ard);
    }
    @ColumnInfo(index = true)
    private int categoriaDeServicoId;
    //set
    public void setId(int id){
        this.id = id;
    }
    public void setNumberBa(@NonNull String numberBa){
        this.numberBa = numberBa;
    }
    public void setCity(@NonNull String city){
        this.city = city;
    }
    public void setArd(@NonNull String ard){

    }
    //get
    public int getId(){
        return id;
    }
    @NonNull public String getNumberBa(){
        return numberBa;
    }
    @NonNull public String getCity(){
        return city;
    }
    @NonNull public String getArd(){
        return ard;
    }

    @Override
    public String toString(){
        return getNumberBa();
    }
}
