package example.test.phong.leafpicclone.util

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Context.FINGERPRINT_SERVICE
import android.content.Context.KEYGUARD_SERVICE
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import java.io.IOException
import java.security.*
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

/**
 * Created by user on 2/24/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler(private val context: Context, internal var signal: CancellationSignal) : FingerprintManager.AuthenticationCallback() {
    private var cipher: Cipher? = null
    private var keyStore: KeyStore? = null
    private var keyGenerator: KeyGenerator? = null
    private var cryptoObject: FingerprintManager.CryptoObject? = null
    private val fingerprintManager: FingerprintManager
    private val keyguardManager: KeyguardManager
    private var fingerprintSupported = true
    private var onFingerprintResult: CallBack? = null

    //Toast.makeText(context, "Your device doesn't support fingerprint authentication", Toast.LENGTH_SHORT).show();
    //Toast.makeText(context, "Please enable the fingerprint permission", Toast.LENGTH_SHORT).show();
    //Toast.makeText(context, "No fingerprint configured. Please register at least one fingerprint in your device's Settings", Toast.LENGTH_SHORT).show();
    //Toast.makeText(context, "Please enable lockscreen security in your device's Settings", Toast.LENGTH_SHORT).show();
    val isFingerprintSupported: Boolean
        get() {

            if (!fingerprintManager.isHardwareDetected) {
                fingerprintSupported = false
            }

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                fingerprintSupported = false
            }

            if (!fingerprintManager.hasEnrolledFingerprints()) {
                fingerprintSupported = false
            }

            if (!keyguardManager.isKeyguardSecure) {
                fingerprintSupported = false
            }


            return fingerprintSupported
        }

    interface CallBack {
        fun onSuccess()

        fun onError(s: String)
    }

    init {
        keyguardManager = context.getSystemService(KEYGUARD_SERVICE) as KeyguardManager
        fingerprintManager = context.getSystemService(FINGERPRINT_SERVICE) as FingerprintManager
    }

    fun setOnFingerprintResult(fingerprintResult: CallBack) {
        this.onFingerprintResult = fingerprintResult
    }

    fun startAuth() {
        if (fingerprintSupported) {
            try {

                generateKey()
            } catch (e: FingerprintException) {
                e.printStackTrace()
            }

            if (initCipher()) {
                cryptoObject = FingerprintManager.CryptoObject(cipher!!)
                doAuth(fingerprintManager, cryptoObject!!)
            }
        }

    }

    @Throws(FingerprintException::class)
    private fun generateKey() {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore")


            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")

            keyStore!!.load(null)
            keyGenerator!!.init(KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build())

            keyGenerator!!.generateKey()

        } catch (exc: KeyStoreException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: NoSuchAlgorithmException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: NoSuchProviderException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: InvalidAlgorithmParameterException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: CertificateException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        } catch (exc: IOException) {
            exc.printStackTrace()
            throw FingerprintException(exc)
        }


    }


    fun initCipher(): Boolean {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to get Cipher", e)
        } catch (e: NoSuchPaddingException) {
            throw RuntimeException("Failed to get Cipher", e)
        }

        try {
            keyStore!!.load(null)
            val key = keyStore!!.getKey(KEY_NAME, null) as SecretKey
            cipher!!.init(Cipher.ENCRYPT_MODE, key)
            return true
        } catch (e: KeyPermanentlyInvalidatedException) {
            return false
        } catch (e: KeyStoreException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: CertificateException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: UnrecoverableKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException("Failed to init Cipher", e)
        } catch (e: InvalidKeyException) {
            throw RuntimeException("Failed to init Cipher", e)
        }

    }


    private inner class FingerprintException internal constructor(e: Exception) : Exception(e)

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        if (onFingerprintResult != null)
            onFingerprintResult!!.onError(errString.toString())
    }

    override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence) {
        super.onAuthenticationHelp(helpCode, helpString)

    }

    override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        if (onFingerprintResult != null)
            onFingerprintResult!!.onSuccess()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
    }

    fun doAuth(manager: FingerprintManager, obj: FingerprintManager.CryptoObject) {
        try {
            manager.authenticate(obj, signal, 0, this, null)
        } catch (sce: SecurityException) {
        }

    }

    companion object {

        private val KEY_NAME = "fingerprint_key"
    }
}