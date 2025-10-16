package com.github.georgeh1998.simplesplit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.georgeh1998.simplesplit.feature.signup.SignUpViewModel
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val signUpViewModel: SignUpViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }
    }
}
