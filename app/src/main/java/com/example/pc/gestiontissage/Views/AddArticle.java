package com.example.pc.gestiontissage.Views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.gestiontissage.Models.Article;
import com.example.pc.gestiontissage.R;
import com.example.pc.gestiontissage.Retrofit.ConfigRetrofit;
import com.example.pc.gestiontissage.Retrofit.IRetro;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by pc on 12/04/2018.
 */

public class AddArticle extends Activity {

    private EditText etCode;
    private EditText etVts;
    private Button btAdd;

    ConfigRetrofit configRetrofit = new ConfigRetrofit();
    Retrofit retrofit = configRetrofit.getConfig();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_a);

        etCode = (EditText) findViewById(R.id.EtCode);
        etVts = (EditText) findViewById(R.id.EtVts);

        btAdd = (Button) findViewById(R.id.btAdd);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codeArticle = etCode.getText().toString();

                String vts = etVts.getText().toString();



                if (!codeArticle.equals("") && !vts.equals("")) {
                    IRetro article = retrofit.create(IRetro.class);

                    int vtsArticle = Integer.parseInt(vts);

                    Article newArticle = new Article(codeArticle, vtsArticle);

                    Call<Article> call = article.addArticle(newArticle);


                    call.enqueue(new Callback<Article>() {
                        @Override
                        public void onResponse(Call<Article> call, Response<Article> response) {
                            if (response.body().getCode() != null) {
                                Toast.makeText(AddArticle.this, "Ajout effectué avec succès !", Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent(AddArticle.this, Gestion.class);
                                //startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(Call<Article> call, Throwable t) {
                            Toast.makeText(AddArticle.this, "Erreur : " + t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(AddArticle.this, "Champs vides !", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



