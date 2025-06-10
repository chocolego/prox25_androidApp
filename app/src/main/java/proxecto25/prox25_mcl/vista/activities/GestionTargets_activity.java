package proxecto25.prox25_mcl.vista.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import proxecto25.prox25_mcl.R;
import proxecto25.prox25_mcl.vista.fragments.asignarTarget_fragment;
import proxecto25.prox25_mcl.vista.fragments.crearTarget_fragment;
import proxecto25.prox25_mcl.vista.fragments.listaDispositivos_fragment;

public class GestionTargets_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestion_targets);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnCrearTarget = findViewById(R.id.btn_creartarget);
        btnCrearTarget.setOnClickListener(v -> {
            loadCrearTargetFragment();
        });

        Button btn_asignar = findViewById(R.id.btn_asignarTargets);
        btn_asignar.setOnClickListener(v -> {
            loadAsignarTargetFragment(); //mostrar dispositivos libres y targets libres, disponibles para ser enlazados

        });

        Button btn_volver = findViewById(R.id.btn_atras);
        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void loadCrearTargetFragment() {

        crearTarget_fragment crear_fragment = crearTarget_fragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentCrearTarget, crear_fragment);
        transaction.commit();
    }
    private void loadAsignarTargetFragment() {

        asignarTarget_fragment asignar_fragemnt = new asignarTarget_fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentCrearTarget, asignar_fragemnt);
        transaction.commit();
    }
}