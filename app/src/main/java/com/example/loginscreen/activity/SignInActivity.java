package com.example.loginscreen.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.loginscreen.R;
import com.example.loginscreen.configuration.ConfiguracaoFirebase;
import com.example.loginscreen.configuration.UsuarioFirebase;
import com.example.loginscreen.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText editNome, editEmail, editSenha;
    private Button botaoRegistrar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        configuracoesIniciais();

        botaoRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCadastro(v);
            }
        });
    }
    private void configuracoesIniciais(){
        editNome = findViewById(R.id.textInputNome_signIn);
        editEmail = findViewById(R.id.textInputEmail_signIn);
        editSenha = findViewById(R.id.textInputSenha_signIn);
        botaoRegistrar = findViewById(R.id.buttonRegistrar_signIn);
    }

    public void validarCadastro(View view){
        String textNome = editNome.getText().toString();
        String textEmail = editEmail.getText().toString();
        String textSenha = editSenha.getText().toString();

        if(!textNome.isEmpty()){
            if(!textEmail.isEmpty()){
                if(!textSenha.isEmpty()){
                    User user = new User();
                    user.setNome(textNome);
                    user.setEmail(textEmail);
                    user.setSenha(textSenha);

                    cadastrarUsuario(user);
                }else{
                    Toast.makeText(SignInActivity.this, "Preencha todos os campos para continuar", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(SignInActivity.this, "Preencha todos os campos para continuar", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(SignInActivity.this, "Preencha todos os campos para continuar", Toast.LENGTH_SHORT).show();
        }
    }
    public void cadastrarUsuario(User user){
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuthReference();
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //recupera o uid do usuario
                            String id = task.getResult().getUser().getUid();
                            user.setId(id);

                            //salva o usuario no firebase database
                            user.salvarNoFirebase();

                            //salva o nome do usuario no profile do firebaseAuth
                            UsuarioFirebase.atualizaNomeUsuario(user.getNome());


                            Log.i("AUTH", "Cadastro de usuário completo");
                            Toast.makeText(SignInActivity.this, "Cadastro completo com sucesso", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }else{
                            String exception;
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e){
                                exception = "Digite uma senha mais forte";
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                exception = "Digite um email válido";
                            }catch (FirebaseAuthUserCollisionException e){
                                exception = "Conta já cadastrada";
                            }catch (Exception e){
                                exception = "Erro ao cadastrar usuario: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(SignInActivity.this, exception, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
