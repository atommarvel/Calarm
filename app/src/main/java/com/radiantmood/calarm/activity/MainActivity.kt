package com.radiantmood.calarm.activity

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.App
import com.radiantmood.calarm.common.appendToFile
import com.radiantmood.calarm.compose.render
import timber.log.Timber

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.appendToFile("MainActivity.onCreate")
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            render {
                App(this@MainActivity)
            }
        }
    }
}