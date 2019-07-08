package com.joaopirolo.diario_de_trabalho.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import com.joaopirolo.diario_de_trabalho.R;

public class UtilsAviso {
    public static void notificaErro(Context context, int idMensagem){
        notificaErro(context, context.getString(idMensagem));
    }
    public static void validaAcao(Context context, String mensagem, DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.confirmar);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);
        builder.setPositiveButton(R.string.sim,listener);
        builder.setNegativeButton(R.string.nao,listener);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
    public static String validaCampo(Context context, EditText editText, int msgErr) {
        String mensagem = editText.getText().toString();

        if (UtilsString.stringVazia(mensagem)) {
            UtilsAviso.notificaErro(context, msgErr);
            editText.setText(null);
            editText.requestFocus();
            return null;
        } else {
           return mensagem.trim();
        }
    }

    public static void notificaErro(Context context, String mensagem){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.notificacao);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(mensagem);
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
