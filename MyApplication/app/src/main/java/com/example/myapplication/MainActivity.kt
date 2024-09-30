package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val networkClient = NetworkClient()
    private val ipAddressUrl = "https://functions.yandexcloud.net/d4e2bt6jba6cmiekqmsv"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonFetchIp: Button = findViewById(R.id.buttonFetchIp)
        buttonFetchIp.setOnClickListener {
            animateButton(it)
            fetchAndDisplayIpAddress()
        }
    }

    private fun animateButton(button: View) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 500
        }
        button.startAnimation(fadeIn)
    }

    private fun fetchAndDisplayIpAddress() {
        lifecycleScope.launch {
            val result = networkClient.fetchIpAddress(ipAddressUrl)
            result.onSuccess { ipAddress ->
                showAlertDialog(ipAddress)
            }.onFailure {
                showAlertDialog("Failed to fetch IP address")
            }
        }
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Текущий IP адрес")
        builder.setMessage("Наш IP адрес: $message")
        builder.setPositiveButton("OK") { dialog, _ ->
            animateDialogDismiss(dialog as AlertDialog)
        }
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)
        dialog.show()
        animateDialogShow(dialog)
    }

    private fun animateDialogShow(dialog: AlertDialog) {
        val fadeIn = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 500
        }
        dialog.window?.decorView?.startAnimation(fadeIn)
    }

    private fun animateDialogDismiss(dialog: AlertDialog) {
        val fadeOut = AlphaAnimation(1.0f, 0.0f).apply {
            duration = 500
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    dialog.dismiss()
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
        dialog.window?.decorView?.startAnimation(fadeOut)
    }
}
