package com.example.loginscreen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loginscreen.R;
import com.example.loginscreen.configuration.FirebaseConfiguration;
import com.example.loginscreen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText editEmail, editSenha;
    private TextView textCadastro;
    private Button botaoEntrar;

    private FirebaseAuth firebaseAuth = FirebaseConfiguration.getFirebaseAuthReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //configurações iniciais
        editEmail = findViewById(R.id.textInputEmail_loginScreen);
        editSenha = findViewById(R.id.textInputSenha_loginScreen);
        textCadastro = findViewById(R.id.textCadastro_loginScreen);
        botaoEntrar = findViewById(R.id.buttonEntrar_loginScreen);

        //clicklistener do botao para validar entradas
        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarAutenticacao(v);
            }
        });

        //clicklistener do texto cadastre-se
        textCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirSignInActivity(v);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //caso o usuário ja esteja logado, abre direto a main activity
        FirebaseUser usuarioAtual = firebaseAuth.getCurrentUser();
        if(usuarioAtual != null){
            abrirMainActivity();
        }
    }

    public void validarAutenticacao(View view){
        //recupera email e senha dos edittext
        String textEmail = editEmail.getText().toString();
        String textSenha = editSenha.getText().toString();

        //valida se há campos em branco
        if(!textEmail.isEmpty()){
            if(!textSenha.isEmpty()){
                //cria um usuario, e seta email e senha para fazer login
                User user = new User();
                user.setEmail(textEmail);
                user.setSenha(textSenha);
                fazerLogin(user);
            }else{
                Toast.makeText(LoginActivity.this, "Preencha todos os campos antes de continuar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this, "Preencha todos os campos antes de continuar", Toast.LENGTH_SHORT).show();
        }
    }
    public void fazerLogin(User user){
        firebaseAuth.signInWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //caso o login seja concluido, abre a main activity
                    abrirMainActivity();
                    finish();
                }else{
                    //caso contrario, retorna um erro
                    String exception;
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        exception = "Usuário não cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        exception = "Email ou senha incorreto";
                    }catch (Exception e){
                        exception = "Erro ao cadastrar usuario: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, exception, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void abrirSignInActivity(View view){
        Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
        startActivity(intent);
    }
    public void abrirMainActivity(){
        //chama a main activity
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}