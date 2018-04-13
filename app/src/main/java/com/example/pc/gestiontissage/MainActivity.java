package com.example.pc.gestiontissage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.gestiontissage.Models.Article;
import com.example.pc.gestiontissage.Models.Employe;
import com.example.pc.gestiontissage.Retrofit.ConfigRetrofit;
import com.example.pc.gestiontissage.Retrofit.IRetro;
import com.example.pc.gestiontissage.Views.Gestion;
import com.example.pc.gestiontissage.Views.ModifyArticle;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button ges;
    private EditText etMat;
    private EditText etArticle;
    private EditText etMetrage;
    private EditText etHtrv;
    private EditText etArret;
    public double productionEmp;
    public int vts;
    public int mg;
    private TextView tvMg;

    private TextView tvProd;
    private Button btCalc;

    ConfigRetrofit config = new ConfigRetrofit();
    Retrofit retrofit = config.getConfig();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ges= (Button) findViewById(R.id.btGes);
        etMat= (EditText) findViewById(R.id.EtMat);
        etArret= (EditText) findViewById(R.id.EtArr);
        etArticle= (EditText) findViewById(R.id.EtArt);
        etMetrage= (EditText) findViewById(R.id.EtMet);
        etHtrv= (EditText) findViewById(R.id.etHtv);

        tvMg= (TextView) findViewById(R.id.TvMg);
        tvProd= (TextView) findViewById(R.id.TvProd);

        ges.setOnClickListener(this);

        btCalc = (Button) findViewById(R.id.btCalc);

        btCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String article = etArticle.getText().toString();
                String mat = etMat.getText().toString();
                int arret = Integer.parseInt(etArret.getText().toString());
                int htrv = Integer.parseInt(etHtrv.getText().toString());
                int metrage = Integer.parseInt(etMetrage.getText().toString());

                IRetro retro = retrofit.create(IRetro.class);
                Call<Employe> callEmp = retro.getEmployeeByMatricule(mat);
                Call<Article> callArticle = retro.getArticleByCode(article);
                callEmp.enqueue(new Callback<Employe>() {
                    @Override
                    public void onResponse(Call<Employe> call, Response<Employe> response) {
                        if(response.body().getMat() != null)
                        {
                            productionEmp = response.body().getProduction();

                            double prod = productionEmp;
                        }
                    }

                    @Override
                    public void onFailure(Call<Employe> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                callArticle.enqueue(new Callback<Article>() {
                    @Override
                    public void onResponse(Call<Article> call, Response<Article> response) {
                        if(response.body().getCode() != null)
                        {
                            vts = response.body().getVts();

                            int vt = vts;
                        }
                    }

                    @Override
                    public void onFailure(Call<Article> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Erreur : " + t.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

                mg = (metrage * vts)/100;
                productionEmp = productionEmp + ((((mg / (htrv - arret))*100) - 100));

                tvMg.setText(String.valueOf(mg));
                tvProd.setText(String.valueOf(productionEmp));
            }
        });



    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent( MainActivity.this,Gestion.class);
        startActivity(i);
    }
}
