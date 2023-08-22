package com.example.loginscreen.configuration;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseConfiguration {
    private static FirebaseAuth firebaseAuth;

    /*
    Classe para recuperar a referencia ao firebase
     */
    public static FirebaseAuth getFirebaseAuthReference(){
        if(firebaseAuth == null){
            firebaseAuth = FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }
}
