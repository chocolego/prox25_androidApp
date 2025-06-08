package proxecto25.prox25_mcl.vista.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import proxecto25.prox25_mcl.R;

public class ImaxenAdapter  extends BaseAdapter {

    private final Context context;
    private final List<Bitmap> imaxes;

    public ImaxenAdapter(Context context, List<Bitmap> images) {
        this.context = context;
        this.imaxes = images;
    }

    @Override
    public int getCount() { return imaxes.size(); }

    @Override
    public Object getItem(int position) { return imaxes.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.elemento_imagen, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.imagen_item);
        imageView.setImageBitmap(imaxes.get(position));

        return convertView;
    }

}

