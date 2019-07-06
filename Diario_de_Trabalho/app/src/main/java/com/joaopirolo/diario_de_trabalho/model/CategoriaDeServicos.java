package com.joaopirolo.diario_de_trabalho.model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
@Entity(tableName = "categoriasdeservicos", indices = @Index(value = {"tipoDeServico"}, unique = true))
public class CategoriaDeServicos {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String tipoDeServico;

    public CategoriaDeServicos(@NonNull String tipoDeServico){
        setTipoDeServico(tipoDeServico);
    }
    public void setId(int id){
        this.id = id;

    }
    public void setTipoDeServico(@NonNull String tipoDeServico){
        this.tipoDeServico = tipoDeServico;
    }
    public int getId(){
        return this.id;
    }
    @NonNull public String getTipoDeServico(){
        return this.tipoDeServico;
    }
    @Override
    public String toString(){
        return getTipoDeServico();
    }
}
