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
import android.widget.ListView;

import com.joaopirolo.diario_de_trabalho.model.Servicos;
import com.joaopirolo.diario_de_trabalho.persistence.DiarioDeTrabalhoDatabase;
import com.joaopirolo.diario_de_trabalho.utils.UtilsAviso;

import java.util.List;

public class ListaServicosActivity extends AppCompatActivity {
    private static final int REQUEST_NEW_SERVICE = 1;
    private static final int REQUEST_ALTER_SERVICE = 2;

    private ListView listViewServicos;
    private ServicosAdapter listaAdapter;
    private List<Servicos> listaDeServicos;

    private ActionMode actionMode;
    private int posSel = -1;
    private View viewSelect;


    private ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_servicos);
        setTitle("Serviços");

        listViewServicos = findViewById(R.id.listViewServicos);
        layout = findViewById(R.id.listaServicosLayout);
        listViewServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posSel = position;
                Servicos servicos = (Servicos) listViewServicos.getItemAtPosition(posSel);
                ServicosActivity.alterarServico(ListaServicosActivity.this, REQUEST_ALTER_SERVICE, servicos);
            }
        });
        listViewServicos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewServicos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                if(actionMode!=null){
                    return false;
                }
                posSel = position;
                view.setBackgroundColor(Color.CYAN);
                viewSelect = view;
                listViewServicos.setEnabled(false);
                actionMode = startSupportActionMode(mActionModeCallback);
                return true;

            }
        });
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        mostrarServicos();
        registerForContextMenu(listViewServicos);
        carregaCor();
    }



    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflate = mode.getMenuInflater();
            inflate.inflate(R.menu.menu_acao_contextual_servicos,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
           Servicos servicos = (Servicos) listViewServicos.getItemAtPosition(posSel);
           switch (item.getItemId()){
               case R.id.editar_servicos:
                   ServicosActivity.alterarServico(ListaServicosActivity.this, REQUEST_ALTER_SERVICE, servicos);
                   mode.finish();
                   return true;
               case R.id.deletar_servicos:
                   excluirServicos(servicos);
                   mode.finish();
                   return true;
                default:
                    return false;
           }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if(viewSelect != null){
                viewSelect.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelect = null;
            listViewServicos.setEnabled(true);
        }
    };


    private void mostrarServicos(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ListaServicosActivity.this);

                listaDeServicos = base.servicosDao().queryAll();

                ListaServicosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaAdapter = new ServicosAdapter( ListaServicosActivity.this, listaDeServicos);
                        listViewServicos.setAdapter(listaAdapter);
                    }
                });
            }
        });
    }
    private void carregaCor(){
        layout.setBackgroundColor(PrincipalActivity.selecionaCor);
    }

    private void excluirServicos(final Servicos servicos){
        String mensagem = "Tem certeza que deseja apagar?" + "\n" + servicos.getNumberBa();

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int which){
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                DiarioDeTrabalhoDatabase base = DiarioDeTrabalhoDatabase.getDatabase(ListaServicosActivity.this);
                                base.servicosDao().delete(servicos);
                                ListaServicosActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listaAdapter.remove(servicos);
                                    }
                                });
                            }
                        });
                        break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                }
            }
        };
        UtilsAviso.validaAcao(this,mensagem,listener);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if((requestCode == REQUEST_NEW_SERVICE || requestCode == REQUEST_ALTER_SERVICE) && resultCode == Activity.RESULT_OK){
            mostrarServicos();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflate = getMenuInflater();
        inflate.inflate(R.menu.menu_lista_servicos, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.addServico:
                ServicosActivity.novoServico(this,REQUEST_NEW_SERVICE);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

}
