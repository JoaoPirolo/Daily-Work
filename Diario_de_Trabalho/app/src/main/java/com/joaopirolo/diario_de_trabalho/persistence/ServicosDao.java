package com.joaopirolo.diario_de_trabalho.persistence;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.joaopirolo.diario_de_trabalho.model.Servicos;

import java.util.List;

public interface ServicosDao {
    @Insert
    long insert(Servicos servicos);
    @Delete
    void delete(Servicos servicos);
    @Update
    void update(Servicos servicos);

    @Query("SELECT * FROM servicos WHERE id = :id")
    Servicos queryForId(long id);
    @Query("SELECT * FROM servicos ORDER BY numBa ASC")
    List<Servicos> queryAll();
}
