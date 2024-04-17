package com.example.beautynetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.beautynetwork.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Deklariert eine private lateinit Variable namens binding vom Typ ActivityMainBinding
    private lateinit var binding: ActivityMainBinding

    //Überschreibt die Methode onCreate des Elternobjekts.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Thread.sleep(5000)
        installSplashScreen()

        //Erstellt ein Binding-Objekt für die Aktivität mithilfe des generierten Binding-Klasse ActivityMainBinding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        //Legt die Ansicht der Aktivität auf die Wurzelansicht des Binding-Objekts fest.
        setContentView(binding.root)
    }

}





