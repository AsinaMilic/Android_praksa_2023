package com.example.feedcraft

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar

class SplashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val intentMainAct = Intent(this, MainActivity::class.java)

        val proBar = findViewById<ProgressBar>(R.id.pbSplash)

        val time: Long = resources.getInteger(R.integer.countdownTimer).toLong() * 1000  //ms
        proBar.max = time.toInt()
        object : CountDownTimer(time, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                proBar.setProgress((time/millisUntilFinished).toInt()*1000, true)
            }

            override fun onFinish() {
                startActivityForResult(intentMainAct,1001)
            }
        }.start()


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK){
            val value = data?.getStringExtra("main_extra")
            Log.i("SplashActivity", "rezultat je $value")
//            if(requestCode == null)

        }
    }


}