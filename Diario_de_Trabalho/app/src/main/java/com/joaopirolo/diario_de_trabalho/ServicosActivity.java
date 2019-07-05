package com.joaopirolo.diario_de_trabalho;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;
import com.joaopirolo.diario_de_trabalho.model.Servicos;

import java.util.List;

public class ServicosActivity extends AppCompatActivity {

    private EditText editTextNumBa;
    private EditText editTextCity;
    private EditText editTextArd;

    private ArrayAdapter<CategoriaDeServicos> categoriaAdapter;

    private Spinner spinnerCategoriaDeServicos;

    private List<CategoriaDeServicos> categoriadeservicos;
    private int modo;


    private Servicos servicos;
    private CategoriaDeServicos categoriaDeServicos;
    private ConstraintLayout layout;

    public static final String MODO = "MODO";
    public static final String ID = "ID";

    public static final String Servicos = "SERVICOS"

   public static final NEW = 1;
   public static final UPDATE = 2;

    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);
    }
}
