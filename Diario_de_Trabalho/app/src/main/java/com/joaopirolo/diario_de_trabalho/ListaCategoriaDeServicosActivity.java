package com.joaopirolo.diario_de_trabalho;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.joaopirolo.diario_de_trabalho.model.CategoriaDeServicos;
import com.joaopirolo.diario_de_trabalho.persistence.DiarioDeTrabalhoDatabase;
import com.joaopirolo.diario_de_trabalho.utils.UtilsAviso;

import java.util.List;

public class ListaCategoriaDeServicosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_de_servicos);

        setTitle("Tipos de Servico");

        listViewCategoriasDeServicos = findViewById(R.id.listViewCategoriaDeServicos);
        layout = findViewById(R.id.listaServicosLayout);
        listViewCategoriasDeServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                elementoSelecionado = position;
                CategoriaDeServicos categoriaDeServicos = (CategoriaDeServicos) listViewCategoriasDeServicos.getItemAtPosition(elementoSelecionado);
                CategoriasDeServicosActivity.editCategoria(ListaCategoriaDeServicosActivity.this,REQUEST_UPDATE_TYPE_SERVICE, categoriaDeServicos );
            }
        });
        listViewCategoriasDeServicos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewCategoriasDeServicos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(actionMode!=null){
                    return false;
                }
                elementoSelecionado = position;
                view.setBackgroundColor(Color.CYAN);
                viewutilizada = view;
                listViewCategoriasDeServicos.setEnabled(false);
                actionMode = startSupportActionMode(mActionMode);

                return true;
            }
        });
        mostrarCategoriasDeServicos();
        registerForContextMenu(listViewCategoriasDeServicos);
        carregaCor();
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private ConstraintLayout layout;
    private ListView listViewCategoriasDeServicos;
    private ArrayAdapter<CategoriaDeServicos> arrayAdapter;
    private List<CategoriaDeServicos> list;

    private ActionMode actionMode;
    private int elementoSelecionado = -1;
    private View viewutilizada;

    private static final int REQUEST_NEW_TYPE_SERVICE = 1;
    private static final int REQUEST_UPDATE_TYPE_SERVICE = 2;

    private void mostrarCategoriasDeServicos(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ListaCategoriaDeServicosActivity.this);
                list = base.categoriaDeServicosDao().queryAll();
                ListaCategoriaDeServicosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arrayAdapter = new ArrayAdapter<>(ListaCategoriaDeServicosActivity.this,
                                                            android.R.layout.simple_list_item_1,
                                                            list);
                        listViewCategoriasDeServicos.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }
    private void excluirCategoriaDeServico(final CategoriaDeServicos categoriaDeServicos){
        String msg = "Tem certeza?" + "\n" + categoriaDeServicos.getTipoDeServico();
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ListaCategoriaDeServicosActivity.this);
                                base.categoriaDeServicosDao().delete(categoriaDeServicos);

                                ListaCategoriaDeServicosActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        arrayAdapter.remove(categoriaDeServicos);
                                    }
                                });
                            }
                        });
                        break;
                        case DialogInterface.BUTTON_NEGATIVE: break;
                }
            }
        };
        UtilsAviso.validaAcao(this,msg,onClickListener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if((requestCode == REQUEST_NEW_TYPE_SERVICE || requestCode == REQUEST_UPDATE_TYPE_SERVICE) && resultCode == Activity.RESULT_OK){
            mostrarCategoriasDeServicos();
        }
    }
    private ActionMode.Callback mActionMode = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            MenuInflater menuInflater = actionMode.getMenuInflater();
            menuInflater.inflate(R.menu.menu_acao_contextual_categorias_servicos, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            CategoriaDeServicos categoriaDeServicos = (CategoriaDeServicos) listViewCategoriasDeServicos.getItemAtPosition(elementoSelecionado);
            switch (menuItem.getItemId()) {
                case R.id.editar_CategoriaServicos:
                    CategoriasDeServicosActivity.editCategoria(ListaCategoriaDeServicosActivity.this, REQUEST_UPDATE_TYPE_SERVICE, categoriaDeServicos);
                    actionMode.finish();
                    return true;
                case R.id.deletar_categoria_servicos:
                    excluirCategoriaDeServico(categoriaDeServicos);
                    actionMode.finish();
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            if (viewutilizada != null) {
                viewutilizada.setBackgroundColor(Color.TRANSPARENT);
            }
            viewutilizada = null;
            actionMode = null;
            listViewCategoriasDeServicos.setEnabled(true);

        }

    };
    private void carregaCor(){
        layout.setBackgroundColor(PrincipalActivity.selecionaCor);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_acao_contextual_categorias_servicos,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.addCategoriaServico: CategoriasDeServicosActivity.addCategoria(this,REQUEST_NEW_TYPE_SERVICE);
            return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }
}
