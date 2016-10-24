/*
 * Expander: Text Expansion Application
 * Copyright (C) 2016 Brett Huber
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.wanderfar.expander.Services



import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.TargetApi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.*
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import com.wanderfar.expander.Models.MacroStore
import io.paperdb.Paper
import java.util.*
import com.wanderfar.expander.R



class MacroAccessibilityService : AccessibilityService(), MacroAccessibilityServiceView {


    lateinit var windowManager : WindowManager
    lateinit var floatingUI: FrameLayout
    lateinit var gestureDetector : GestureDetector
    lateinit var source : AccessibilityNodeInfo

    private val INTERVAL: Long = 3000

    lateinit var stopTask : TimerTask
    lateinit var timer : Timer

    //Create the presenter
    private val mPresenter : MacroAccessibilityServicePresenter by lazy {
        MacroAccessibilityServicePresenterImpl(this)
    }

    override fun onCreate(){
        super.onCreate()

        initFloatingUIElements()
    }


    override fun onServiceConnected(){

        val info = AccessibilityServiceInfo()

        // Set the type of events that this service wants to listen to.  Others
        // won't be passed to this service.
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED


        // Set the type of feedback your service will provide.
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN

        // Default services are invoked only if no package-specific ones are present
        // for the type of AccessibilityEvent generated.  This service *is*
        // application-specific, so the flag isn't necessary.  If this was a
        // general-purpose service, it would be worth considering setting the
        // DEFAULT flag.

        info.flags = AccessibilityServiceInfo.DEFAULT

        info.notificationTimeout = 100

        info.packageNames = getAllowedApplications()


        //info.settingsActivityName = "com.apps.wanderfar.expander.Settings.SettingsActivity"

        println("Macro service was just connected!")

        serviceInfo = info



    }




    override fun onInterrupt() {

        hideFloatingUI()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {


        println("Text just changed, checking to see if we have a match")

        //get the text from the event
        //sub string it so it removes the brackets
        var text : String = ""
        try {
            text = event!!.text.toString().substring( 1, event.text.toString().length - 1)
        } catch(e : IndexOutOfBoundsException) {
            Log.e("", e.toString())
        }

        //get the source
        source = event!!.source


        //get necessary permission
        val isExpanderEnabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isExpanderEnabled", true)

        //If the text isn't empty and we have necessary permission, check the text
        if(text.isNullOrEmpty().not() && isExpanderEnabled){
            Paper.init(this)
            mPresenter.onAccessibilityEvent(MacroStore.getMacros(), text, source.textSelectionStart)
            hideFloatingUI()
        }
    }

    override fun onUnbind(intent: Intent): Boolean {

        hideFloatingUI()

        return false
    }

    override fun onDestroy() {
        hideFloatingUI()
        super.onDestroy()
    }

    override fun updateText(updatedText: String, newCursorPosition: Int) {
        val arguments = Bundle()
        arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, updatedText)


        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, newCursorPosition)
        arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, newCursorPosition)
        source.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
        source.performAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, arguments)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(isSystemAlertPermissionGranted(this)) {
                createFloatingUI()
            }
        } else {
            if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("IsFloatingUIEnabled", true)){
                createFloatingUI()
            }
        }

    }

    override fun hideFloatingUI() {

        if (floatingUI != null && floatingUI.isAttachedToWindow) {
            //timer.cancel()
            windowManager.removeView(floatingUI)
        }
    }




    fun initFloatingUIElements() {
        floatingUI = FrameLayout(this)

        windowManager =  getSystemService(Service.WINDOW_SERVICE) as WindowManager

    }

    fun createFloatingUI() {



        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.START
        params.x = 0
        params.y = 100
        params.windowAnimations = R.style.MyAnimation_Window


        if (floatingUI.isAttachedToWindow.not()){
            //windowManager.addView(floatingUI, params)

            //Get the opacity level of the UI and set it
            val radius = PreferenceManager.getDefaultSharedPreferences(this).getInt("Opacity_Value", 75)
            val opacityValue : Float = (radius.toFloat() / 100.toFloat() )
            floatingUI.alpha =  opacityValue

            //Add the view
            windowManager.addView(floatingUI, params)



            //Inflate the view layout
            val layoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    layoutInflater.inflate(com.wanderfar.expander.R.layout.floating_ui, floatingUI)

            setUITouchListener(params)

            //
            initServiceStop()
        }


    }


    //creates a task for the UI to be hidden after a certain number of seconds
    private fun initServiceStop() {
        stopTask =  object : TimerTask() {
            override fun run() {

                hideFloatingUI()
            }
        }

        timer = Timer()

        timer.schedule(stopTask, INTERVAL)
    }

    private fun setUITouchListener(params : WindowManager.LayoutParams) {

        gestureDetector = GestureDetector(this, SingleTapConfirm())

        floatingUI.setOnTouchListener(object : View.OnTouchListener {
            private var initialX: Int = 0
            private var initialY: Int = 0
            private var initialTouchX: Float = 0.toFloat()
            private var initialTouchY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                if(gestureDetector.onTouchEvent(event)){
                    //single click
                    //Undo the text
                    mPresenter.undoSetText()
                    //hide the floating UI
                    hideFloatingUI()
                    return true
                }
                else {
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params.x
                            initialY = params.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> return true
                        MotionEvent.ACTION_MOVE -> {
                            params.x = (initialX + (event.rawX - initialTouchX)).toInt()
                            params.y = (initialY + (event.rawY - initialTouchY)).toInt()
                            windowManager.updateViewLayout(floatingUI, params)
                            return true
                        }
                        MotionEvent.ACTION_BUTTON_RELEASE -> {
                            //onDestroy()
                            return true
                        }

                    }
                    return false
                }

            }
        })

    }

    private fun getAllowedApplications() : Array<String>? {

        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val isAppsAllowed = prefs.getBoolean("Application_Filter_Type", true)

        //If isAppsAllowed = true - then we need to see if any apps are blacklisted.
        //If there is an app blacklisted then we need to get a list of all apps besides the black listed app and return it
        //If not we return null

        if(isAppsAllowed){


            return getInstalledApplications(prefs.getStringSet(resources.getString(com.wanderfar.expander.R.string.key_application_list)
                    , mutableSetOf("")))

        }
        //If it is false that means all apps are blocked by default so we need to get the list of the apps the user has set to allow and return it
        else {
            val apps: MutableSet<String>? = prefs.getStringSet(resources.getString(com.wanderfar.expander.R.string.key_application_list)
                    , mutableSetOf(""))

            if (apps == null || apps.isEmpty()){
              return arrayOf("String")
            } else {
                return apps.toTypedArray()
            }

        }
    }

    private fun getInstalledApplications(excludedApps: MutableSet<String>) : Array<String>? {
        val pm = this.packageManager

        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        if (excludedApps == null || excludedApps.isEmpty()){
            return null
        } else {
            val allowedApps = mutableListOf<String>()
            for (app : ApplicationInfo in packages){
                if (excludedApps.contains(app.packageName).not()){
                    allowedApps.add(app.packageName)
                }
            }

            return allowedApps.toTypedArray()
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    fun isSystemAlertPermissionGranted(context: Context): Boolean {
        val result = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)
        return result
    }


}
