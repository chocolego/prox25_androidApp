package proxecto25.prox25_mcl.vista.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.modelo.Dispositivos;

public class DispositivoAdapter extends BaseAdapter {

    private Context context;
    private List<Dispositivos> dispositivosList;

    public DispositivoAdapter(Context context, List<Dispositivos> dispositivosList) {
        this.context = context;
        this.dispositivosList = dispositivosList;
    }

    @Override
    public int getCount() {
        return dispositivosList != null? dispositivosList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return dispositivosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //View view = inflater.inflate(R.layout.elemento_dispositivo, parent, false);
        LayoutInflater inflater = LayoutInflater.from(context);
        View elemento = inflater.inflate(R.layout.elemento_dispositivo, parent, false);
//        if (view == null) {
//            view = inflater.inflate(R.layout.elemento_dispositivo, parent, false);
//        }
        TextView tvNombre = elemento.findViewById(R.id.tv_nombre);
        TextView tvModelo = elemento.findViewById(R.id.tv_modelo);


        Dispositivos dispositivo = dispositivosList.get(position);
        tvNombre.setText(dispositivo.getNombreDispositivo().toString()+"");
        tvModelo.setText(dispositivo.getModelo().toString() +"");

        return elemento;
    }


}