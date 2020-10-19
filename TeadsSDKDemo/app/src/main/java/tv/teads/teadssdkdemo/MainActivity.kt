package tv.teads.teadssdkdemo

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mopub.common.MoPub
import com.mopub.common.SdkConfiguration
import kotlinx.android.synthetic.main.activity_main.*
import tv.teads.teadssdkdemo.format.mediation.identifier.MoPubIdentifier
import tv.teads.teadssdkdemo.utils.BaseFragment


class MainActivity : AppCompatActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (!TextUtils.isEmpty(intent.getStringExtra(INTENT_EXTRA_PID))) {
            PreferenceManager
                    .getDefaultSharedPreferences(this@MainActivity)
                    .edit()
                    .putInt(
                            SHAREDPREF_PID,
                            Integer.parseInt(intent.getStringExtra(INTENT_EXTRA_PID)))
                    .apply()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MoPub.initializeSdk(this, SdkConfiguration.Builder(MoPubIdentifier.MOPUB_ID).build()) {}

        if (!TextUtils.isEmpty(intent.getStringExtra(INTENT_EXTRA_PID))) {
            PreferenceManager
                    .getDefaultSharedPreferences(this@MainActivity)
                    .edit()
                    .putInt(
                            SHAREDPREF_PID,
                            Integer.parseInt(intent.getStringExtra(INTENT_EXTRA_PID)))
                    .apply()
        }

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = MainFragment()
            transaction.replace(R.id.fragment_container, fragment, MainFragment::class.java.simpleName)
            transaction.commit()
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.title = ""

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.white)
        }
        setSupportActionBar(toolbar)
    }

    /**
     * Return the pid, if not one is set, the default one
     *
     * @param context current context
     * @return pid
     */
    fun getPid(context: Context): Int {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getInt(
                        SHAREDPREF_PID, SHAREDPREF_PID_DEFAULT)
    }

    /**
     * Return the Webview url, if not one is set, the default one
     *
     * @param context current context
     * @return an url
     */
    fun getWebViewUrl(context: Context): String {
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(SHAREDPREF_WEBVIEWURL, SHAREDPREF_WEBVIEW_DEFAULT) ?: return SHAREDPREF_WEBVIEW_DEFAULT
    }

    fun changeFragment(frag: BaseFragment) {
        if ((supportFragmentManager.findFragmentById(R.id.fragment_container) as Fragment).javaClass == frag.javaClass) {
            return
        }

        val backStateName = (frag as Any).javaClass.name

        try {
            val manager = supportFragmentManager
            //fragment not in back stack, create it.
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fragment_container, frag, (frag as Any).javaClass.name)
            transaction.detach(frag)
            transaction.attach(frag)
            transaction.addToBackStack(backStateName)
            transaction.commit()
            setToolBar(false, frag.getTitle())
        } catch (exception: IllegalStateException) {
            Log.e(LOG_TAG, "Unable to commit fragment, could be activity as been killed in background. $exception")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun changePidDialog() {
        val view = layoutInflater.inflate(R.layout.dialog_pid_content, null)
        val input = view.findViewById<EditText>(R.id.pidEditText)
        input.setText(getPid(this).toString())
        input.setLines(1)
        input.setSingleLine(true)

        AlertDialog.Builder(this)
                .setTitle("Change default PID")
                .setView(view)
                .setPositiveButton("Save") { _, _ ->
                    val pidString = input.text.toString()
                    val pid: Int
                    pid = if (pidString.isEmpty()) {
                        Toast.makeText(this@MainActivity, "Setting default PID", Toast.LENGTH_SHORT).show()
                        SHAREDPREF_PID_DEFAULT
                    } else {
                        Toast.makeText(this@MainActivity, "Setting PID is only working for DIRECT provider", Toast.LENGTH_SHORT).show()
                        Integer.parseInt(pidString)
                    }
                    PreferenceManager
                            .getDefaultSharedPreferences(this@MainActivity)
                            .edit()
                            .putInt(
                                    SHAREDPREF_PID,
                                    pid)
                            .apply()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    // Do nothing.
                }.show()
    }

    private fun setToolBar(isMainFragment: Boolean, title: String = "") {
        supportActionBar?.setDisplayHomeAsUpEnabled(!isMainFragment)
        toolbar.title = title
        toolbar_logo.visibility = if (isMainFragment) View.VISIBLE else View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_temp, menu);

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                supportFragmentManager.popBackStack()
                setToolBar(true)
            }
            R.id.action_pid -> {
                changePidDialog()
            }
        }

        return super.onOptionsItemSelected(item);
    }

    override fun onBackPressed() {
        supportFragmentManager.popBackStack()
        setToolBar(true)
    }

    companion object {
        private const val LOG_TAG = "MainActivity"
        private const val INTENT_EXTRA_PID = "ext_pid"
        private const val SHAREDPREF_PID = "sp_pid"
        private const val SHAREDPREF_WEBVIEWURL = "sp_wvurl"
        private const val SHAREDPREF_PID_DEFAULT = 84242
        private const val SHAREDPREF_WEBVIEW_DEFAULT = "file:///android_asset/demo.html"
    }
}
