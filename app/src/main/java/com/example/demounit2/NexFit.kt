package com.example.demounit2.nexfit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.demounit2.NexFitRoot
import com.example.demounit2.ui.theme.DemoUnit2Theme

class NexFit : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoUnit2Theme {
                NexFitRoot()
            }
        }
    }
}