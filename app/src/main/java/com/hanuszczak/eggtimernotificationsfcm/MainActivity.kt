package com.hanuszczak.eggtimernotificationsfcm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hanuszczak.eggtimernotificationsfcm.ui.EggTimerFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EggTimerFragment.newInstance())
                .commitNow()
        }
    }
}