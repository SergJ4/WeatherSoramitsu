package com.soramitsu.test.repository.datasource.api

import android.content.Context
import com.soramitsu.test.domain.exceptions.NetworkErrorException
import com.soramitsu.test.domain.exceptions.SimpleMessageException
import com.soramitsu.test.domain.extensions.hasNetwork
import com.soramitsu.test.repository.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Перехватчик для определения наличия сетевого подключения на девайсе
 * и наличие ошибок диапазона 400-599 в ответе.
 * Все сгенерированные здесь исключения далее по цепочке попадают
 * в [ErrorsInterceptor]
 */
internal class ConnectivityInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //Проверяем наличие сети
        if (!context.hasNetwork()) {
            throw NetworkErrorException()
        }

        val builder = chain.request().newBuilder()
        try {
            val response = chain.proceed(builder.build())
            //Проверяем код ответа на наличие ошибок
            if ((response.code() >= 400) and (response.code() < 600)) {
                throw NetworkErrorException()
            }
            return response
        } catch (e: Exception) {
            when {
                (e is UnknownHostException || e is SSLException) -> throw NetworkErrorException()
                else -> throw SimpleMessageException(0, context.getString(R.string.unknown_error))
            }
        }
    }
}
