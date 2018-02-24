package example.test.phong.leafpicclone.util

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.CancellationSignal
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.orhanobut.hawk.Hawk
import example.test.phong.leafpicclone.R
import org.horaapps.liz.ThemeHelper
import org.horaapps.liz.ThemedActivity
import java.security.MessageDigest

/**
 * Created by user on 2/24/2018.
 */
class Security {
    companion object {
        val isPasswordSet: Boolean
            get() = Hawk.get<Any>("password_hash", null) != null

        var isPasswordOnHidden: Boolean
            get() = Hawk.get<Any>("password_hash", null) != null && Hawk.get("password_on_hidden", false)
            set(passwordOnHidden) {
                Hawk.put("password_on_hidden", passwordOnHidden)
            }

        val isFingerprintUsed: Boolean
            get() = Hawk.get("fingerprint_security", false)

        var isPasswordOnDelete: Boolean
            get() = Hawk.get<Any>("password_hash", null) != null && Hawk.get("password_on_delete", false)
            set(passwordOnDelete) {
                Hawk.put("password_on_delete", passwordOnDelete)
            }

        fun setFingerprintUnlock(passwordOnHidden: Boolean) {
            Hawk.put("fingerprint_security", passwordOnHidden)
        }

        private fun checkPassword(pass: String): Boolean {
            return sha256(pass) == Hawk.get<Any>("password_hash", null)
        }

        fun setPassword(newValue: String): Boolean {
            return Hawk.put("password_hash", sha256(newValue))
        }

        fun clearPassword(): Boolean {
            return Hawk.delete("password_hash")
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun authenticateUser(activity: ThemedActivity, passwordInterface: AuthCallBack) {
            val builder = AlertDialog.Builder(activity, activity.dialogStyle)
            val mCancellationSignal = CancellationSignal()

            val view = activity.layoutInflater.inflate(R.layout.dialog_password, null)
            val passwordDialogTitle = view.findViewById(R.id.password_dialog_title)
            val passwordDialogCard = view.findViewById(R.id.password_dialog_card)
            val editTextPassword = view.findViewById(R.id.password_edittxt)
            val fingerprintIcon = view.findViewById(R.id.fingerprint_icon)


            passwordDialogTitle.setBackgroundColor(activity.primaryColor)
            passwordDialogCard.setBackgroundColor(activity.cardBackgroundColor)
            ThemeHelper.setCursorColor(editTextPassword, activity.textColor)
            editTextPassword.getBackground().mutate().setColorFilter(activity.textColor, PorterDuff.Mode.SRC_ATOP)
            editTextPassword.setTextColor(activity.textColor)
            fingerprintIcon.setColor(activity.iconColor)

            builder.setView(view)

            builder.setPositiveButton(activity.getString(R.string.ok_action).toUpperCase()) { dialog, which ->
                // NOTE: set this empty, later will be overwrite to avoid the dismiss
            }

            builder.setNegativeButton(activity.getString(R.string.cancel).toUpperCase()) { dialog, which -> hideKeyboard(activity, editTextPassword.getWindowToken()) }

            val dialog = builder.create()
            dialog.show()
            showKeyboard(activity)
            editTextPassword.requestFocus()

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && Security.isFingerprintUsed) {
                val fingerprintHandler = FingerprintHandler(activity, mCancellationSignal)
                if (fingerprintHandler.isFingerprintSupported) {
                    fingerprintHandler.setOnFingerprintResult(object : FingerprintHandler.CallBack {
                        override fun onSuccess() {
                            hideKeyboard(activity, editTextPassword.getWindowToken())
                            dialog.dismiss()
                            passwordInterface.onAuthenticated()
                        }

                        override fun onError(s: String) {
                            // TODO: 9/9/17 siplaymessage
                        }
                    })

                    fingerprintHandler.startAuth()
                } else {
                    fingerprintIcon.setVisibility(View.GONE)
                }
            } else {
                fingerprintIcon.setVisibility(View.GONE)
            }

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener { v ->
                if (checkPassword(editTextPassword.getText().toString())) {
                    hideKeyboard(activity, editTextPassword.getWindowToken())
                    mCancellationSignal.cancel()
                    dialog.dismiss()
                    passwordInterface.onAuthenticated()
                } else {
                    editTextPassword.getText().clear()
                    editTextPassword.requestFocus()
                    passwordInterface.onError()
                }
            }
        }

        private fun showKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }

        private fun hideKeyboard(context: Context, token: IBinder) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(token, 0)
        }

        private fun sha256(base: String): String {
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(base.toByteArray(charset("UTF-8")))
                val hexString = StringBuilder()
                for (aHash in hash) {
                    val hex = Integer.toHexString(0xff and aHash.toInt())
                    if (hex.length == 1) hexString.append('0')
                    hexString.append(hex)
                }
                return hexString.toString()
            } catch (ex: Exception) {
                throw RuntimeException(ex)
            }
        }
    }

    interface AuthCallBack {
        fun onAuthenticated()
        fun onError()
    }
}
