package com.example.loginscreen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.loginscreen.R;
import com.example.loginscreen.configuration.ConfiguracaoFirebase;
import com.example.loginscreen.configuration.UsuarioFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView textNome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //configurações iniciais
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuthReference();
        textNome = findViewById(R.id.textNome_main);

        String nomeUsuario = UsuarioFirebase.getUsuarioAtual().getDisplayName();
        textNome.setText(nomeUsuario);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_sair){
            deslogar();
            abrirLoginActivity();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogar(){
        try {
            firebaseAuth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void abrirLoginActivity(){
        //chama a main activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
