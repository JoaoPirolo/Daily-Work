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

import java.util.Locale;
import java.util.concurrent.Executors;

@Database(entities = { Servicos.class}, version = 1)
public abstract class ServicosDatabase extends RoomDatabase{

    public abstract ServicosDao servicosDao();

    public static ServicosDatabase instance;

    public abstract CategoriaDeServicos categoriaDeServicoDao();

public static ServicosDatabase getDatabase(final Context context) {

   if(instance == null){
       synchronized (ServicosDatabase.class){
           if(instance == null){
               Builder builder = Room.databaseBuilder(context, ServicosDatabase.class, "servicos.db");
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
               instance = (ServicosDatabase) builder.build();
           }
       }
   }
   return instance;
}
private static void mostrarCategoriasDeServicos(final Context context){
    String[] categoriasDeServico = context.getResources().getStringArray(R.array.categoriaDeServicos);
    for(String categoriaDeServicos: categoriasDeServico){
        CategoriaDeServicos categoriadeservicos = new CategoriaDeServicos(categoriaDeServicos);
        instance.categoriaDeServicoDao().insert(categoriaDeServicos);
    }
    }
}

