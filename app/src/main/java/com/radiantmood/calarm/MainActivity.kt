package com.radiantmood.calarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.radiantmood.calarm.compose.render

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenResumed {
            // TODO: how do I re-render when the user brings the app back from the background?
            render {
                App(this@MainActivity)
            }
        }
    }
}