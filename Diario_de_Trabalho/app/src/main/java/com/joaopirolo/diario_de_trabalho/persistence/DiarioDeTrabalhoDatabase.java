package com.joaopirolo.diario_de_trabalho.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.joaopirolo.diario_de_trabalho.R;
import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;
import com.joaopirolo.diario_de_trabalho.model.Servicos;

import java.util.concurrent.Executors;

@Database(entities = { Servicos.class, CategoriaDeServicos.class}, version = 1)
public abstract class DiarioDeTrabalhoDatabase extends RoomDatabase{

    public abstract ServicosDao servicosDao();

    public static DiarioDeTrabalhoDatabase instance;

    public abstract CategoriaDeServicos categoriaDeServicoDao();

public static DiarioDeTrabalhoDatabase getDatabase(final Context context) {

   if(instance == null){
       synchronized (DiarioDeTrabalhoDatabase.class){
           if(instance == null){
               Builder builder = Room.databaseBuilder(context, DiarioDeTrabalhoDatabase.class, "servicos.db");
               builder.addCallback(new Callback() {
                   @Override
                   public void onCreate(@NonNull SupportSQLiteDatabase db){
                       super.onCreate(db);
                       Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                           @Override
                           public void run() {
                               mostrarCategoriasDeServicos(context);
                           }
                       });
                   }
               });
               instance = (DiarioDeTrabalhoDatabase) builder.build();
           }
       }
   }
   return instance;
}
private static void mostrarCategoriasDeServicos(final Context context){
    String[] tipos_servico = context.getResources().getStringArray(R.array.tipos_servicos);
    for(String categoriaServicos: tipos_servico){
        CategoriaDeServicos categoriaDeServicos= new CategoriaDeServicos(categoriaServicos);
        instance.categoriaDeServicoDao().insert(categoriaDeServicos);
    }
    }
}

