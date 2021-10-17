package com.thesohelshaikh.ytanalyser

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.thesohelshaikh.ytanalyser.RequestManager

class RequestManager private constructor(var ctx: Context) {
    private var requestQueue: RequestQueue?
    fun getRequestQueue(): RequestQueue? {
        if (requestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            requestQueue = Volley.newRequestQueue(ctx.applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>?) {
        getRequestQueue()!!.add(req)
    }

    companion object {
        private var instance: RequestManager? = null
        @JvmStatic
        @Synchronized
        fun getInstance(context: Context): RequestManager? {
            if (instance == null) {
                instance = RequestManager(context)
            }
            return instance
            // this is a comment
            // 
        }
    }

    // TODO check for memory leak
    init {
        requestQueue = getRequestQueue()
    }
}