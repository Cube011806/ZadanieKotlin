package com.kk.zadaniekotlin.ui.basket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kk.zadaniekotlin.MainActivity

class BasketReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("openBasket", true)
        }
        context.startActivity(openIntent)
    }
}
