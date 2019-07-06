package com.joaopirolo.diario_de_trabalho;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joaopirolo.diario_de_trabalho.model.Servicos;
import java.util.List;

public class ServicosAdapter extends ArrayAdapter<Servicos> {
    public ServicosAdapter(Context context, List<Servicos> servicos){
        super(context, 0, servicos);
    }
    @Override
    public View getView(int position, View newView, ViewGroup parent){
        Servicos servicos = getItem(position);

        if(newView == null){
            newView = LayoutInflater.from(getContext()).inflate(R.layout.servicos_adapter, parent, false);

            TextView textViewNumBa = (TextView) newView.findViewById(R.id.textViewNumBa);
            TextView textViewCity = (TextView) newView.findViewById(R.id.textViewCity);
            TextView textViewArd = (TextView) newView.findViewById(R.id.textViewArd);

            textViewNumBa.setText(servicos.getNumberBa());
            textViewCity.setText(servicos.getCity());
            textViewArd.setText(servicos.getArd());



        }
        return newView;
    }
}
