package io.bakerystud.commonapi.providers

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import io.bakerystud.smartpit.di.providers.Tls12SocketFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.net.ssl.SSLContext


/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class OkHttpClientProvider @Inject constructor(
    private val context: Context,
    private val authenticator: Authenticator
) : Provider<OkHttpClient> {

    override fun get() = with(OkHttpClient.Builder()) {
        cache(Cache(context.cacheDir, 20 * 1024))
        connectTimeout(90, TimeUnit.SECONDS)
        readTimeout(90, TimeUnit.SECONDS)
        writeTimeout(90, TimeUnit.SECONDS)

        authenticator(authenticator)
        addNetworkInterceptor(
            HttpLoggingInterceptor(ConfinedLogger()).apply { level = HttpLoggingInterceptor.Level.BODY }
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            val specsList = getSpecsBelowLollipopMR1(this)
            if (specsList != null) {
                connectionSpecs(specsList)
            }
        }
        build()
    }

    private fun getSpecsBelowLollipopMR1(okb: OkHttpClient.Builder): List<ConnectionSpec>? {

        try {

            val sc = SSLContext.getInstance("TLSv1.2")
            sc.init(null, null, null)
            okb.sslSocketFactory(Tls12SocketFactory(sc.socketFactory))

            val cs = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .build()

            val specs = ArrayList<ConnectionSpec>()
            specs.add(cs)
            specs.add(ConnectionSpec.COMPATIBLE_TLS)

            return specs

        } catch (exc: Exception) {
            Timber.e("OkHttpTLSCompat Error while setting TLS 1.2$exc")

            return null
        }

    }

    private class ConfinedLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            val oneLineMessage = message.replace("\n", "\\n")
            val logMaxLength = 20000
            if (oneLineMessage.length > logMaxLength) {
                Timber.d(oneLineMessage.substring(0..logMaxLength))
            } else {
                Timber.d(oneLineMessage)
            }
        }
    }
}