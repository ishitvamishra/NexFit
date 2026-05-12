package com.example.demounit2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.demounit2.ui.theme.DemoUnit2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoUnit2Theme {
                // Yahan hum NexFitRoot ko call kar rahe hain
                // Taaki app khulte hi aapka project start ho jaye
                NexFitRoot()
            }
        }
    }
}