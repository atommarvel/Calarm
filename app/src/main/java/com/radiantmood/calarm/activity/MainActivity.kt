package com.radiantmood.calarm.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.App
import com.radiantmood.calarm.compose.render

class MainActivity : AppCompatActivity() {
    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            // TODO: how do I re-render when the user brings the app back from the background?
            render {
                App(this@MainActivity)
            }
        }
    }
}