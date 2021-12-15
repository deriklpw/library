package com.sky.library.manager

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.os.Looper
import com.sky.library.utils.Log
import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean

class NsdDevicesManager private constructor(context: Context) {
    private var nsdManager: NsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    private val pendingServices: ConcurrentLinkedQueue<NsdServiceInfo> = ConcurrentLinkedQueue()

    private val resolvedMap: ConcurrentHashMap<String, NsdServiceInfo> = ConcurrentHashMap()

    private val mHandler = Handler(Looper.getMainLooper())

    private var discoveryListener: NsdManager.DiscoveryListener? = null

    private var resolverListener: NsdManager.ResolveListener? = null

    private var searchListener: OnSearchDevicesListener? = null

    private var isResolveBusy = AtomicBoolean(false)

    companion object {
        @Volatile
        @JvmStatic
        private var INSTANCE: NsdDevicesManager? = null

        fun getInstance(context: Context): NsdDevicesManager {
            if (INSTANCE == null) {
                synchronized(NsdDevicesManager::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = NsdDevicesManager(context)
                    }
                }
            }
            return INSTANCE!!
        }
    }

    private fun initDiscoverListener() {
        discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.d("ShadowManager", "onStartDiscoveryFailed type=$serviceType")
                searchListener?.onError(Throwable("Start Discovery Failed"))
                stopDiscovery()
            }

            override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
                Log.d("ShadowManager", "onStopDiscoveryFailed type=$serviceType")
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onDiscoveryStarted(serviceType: String?) {
                Log.d("ShadowManager", "onDiscoveryStarted type=$serviceType")
            }

            override fun onDiscoveryStopped(serviceType: String?) {
                Log.d("ShadowManager", "onDiscoveryStopped type=$serviceType")
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo?) {
                Log.d("ShadowManager", "onServiceFound info=$serviceInfo")
                serviceInfo?.also {
                    if (isResolveBusy.compareAndSet(false, true)) {
                        nsdManager.resolveService(it, resolverListener)
                    } else {
                        pendingServices.add(it)
                    }
                }
            }

            override fun onServiceLost(lostInfo: NsdServiceInfo?) {
                Log.d(
                    "ShadowManager",
                    "onServiceLost info=$lostInfo, isBusy=${isResolveBusy.get()}"
                )
                if (lostInfo != null) {
                    pendingServices.forEach {
                        if (it.serviceName == lostInfo.serviceName) {
                            pendingServices.remove(it)
                        }
                    }
                    if (resolvedMap.contains(lostInfo.serviceName)) {
                        resolvedMap.remove(lostInfo.serviceName)
                        updateResult()
                    }
                }
            }
        }
    }

    private val runnable = {
        if (resolvedMap.size == 0) {
            searchListener?.onTimeout()
        }
    }

    fun discoverServices(
        serviceType: String,
        timeout: Long,
        searchDevicesListener: OnSearchDevicesListener?
    ) {
        stopDiscovery()
        initDiscoverListener()
        searchListener = searchDevicesListener
        mHandler.removeCallbacks(runnable)
        mHandler.postDelayed(runnable, timeout)
        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }

    fun stopDiscovery() {
        if (discoveryListener != null) {
            try {
                nsdManager.stopServiceDiscovery(discoveryListener)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mHandler.removeCallbacks(runnable)
        pendingServices.clear()
        resolvedMap.clear()
        discoveryListener = null
        searchListener = null
    }

    private fun resolveNextInQueue() {
        val nextNsdService = pendingServices.poll()
        if (nextNsdService != null) {
            Log.d("ShadowManager", "resolveNextInQueue, service=$nextNsdService")
            nsdManager.resolveService(nextNsdService, resolverListener)
        } else {
            Log.d("ShadowManager", "resolveNextInQueue, empty")
            isResolveBusy.set(false)
        }
    }

    private fun initResolveListener() {
        resolverListener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(info: NsdServiceInfo?, errorCode: Int) {
                Log.d("ShadowManager", "onResolveFailed, code= ${errorCode}, info=$info")
                resolveNextInQueue()
            }

            override fun onServiceResolved(info: NsdServiceInfo?) {
                info?.also {
                    resolvedMap[it.serviceName] = it
                }
                Log.d("ShadowManager", "onServiceResolved, info=$info")
                updateResult()
                resolveNextInQueue()
            }
        }
    }

    private fun updateResult() {
        val list: MutableList<DeviceInfo> = mutableListOf()
        resolvedMap.values.forEach {
            list.add(
                DeviceInfo(
                    it.serviceName, it.host?.hostAddress?.substringAfter("/") ?: ""
                )
            )
        }
        mHandler.post {
            searchListener?.onUpdateDevices(list)
        }
    }

    interface OnSearchDevicesListener {
        fun onUpdateDevices(devices: Collection<DeviceInfo>)
        fun onTimeout()
        fun onError(error: Throwable)
    }

    class DeviceInfo(val name: String, val ip: String) : Serializable {
        val lastSixMac: String by lazy {
            if (this.name.length > 6) {
                this.name.takeLast(6)
            } else {
                "Default"
            }
        }
        var model: String? = null
    }
}