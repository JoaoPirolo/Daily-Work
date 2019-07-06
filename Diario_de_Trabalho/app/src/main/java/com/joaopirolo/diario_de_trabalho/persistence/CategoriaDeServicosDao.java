package com.joaopirolo.diario_de_trabalho.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;

import java.util.List;
@Dao
public interface CategoriaDeServicosDao {
    @Insert
    long insert(CategoriaDeServicos categoriaDeServicos);
    @Delete
    void delete(CategoriaDeServicos categoriaDeServicos);
    @Update
    void update(CategoriaDeServicos categoriaDeServicos);
    @Query("SELECT * FROM categoriasdeservicos WHERE id= :id")
    CategoriaDeServicos queryForId(long id);
    @Query("SELECT * FROM categoriasdeservicos ORDER BY tipoDeServico ASC")
    List<CategoriaDeServicos> queryAll();
}
