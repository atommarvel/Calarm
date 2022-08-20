package com.radiantmood.calarm.core

import android.app.Activity
import android.app.Application
import android.os.Bundle

open class SimpleActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        // no-op
    }

    override fun onActivityStarted(p0: Activity) {
        // no-op
    }

    override fun onActivityResumed(p0: Activity) {
        // no-op
    }

    override fun onActivityPaused(p0: Activity) {
        // no-op
    }

    override fun onActivityStopped(p0: Activity) {
        // no-op
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        // no-op
    }

    override fun onActivityDestroyed(p0: Activity) {
        // no-op
    }
}