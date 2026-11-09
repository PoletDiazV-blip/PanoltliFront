package com.polet.panoltli.core.network

import com.polet.panoltli.feature_auth.data.remote.AuthApi
import com.polet.panoltli.feature_diccionario.data.remote.DictionaryApi
import com.polet.panoltli.feature_historial.data.remote.HistoryApi
import com.polet.panoltli.feature_perfil.data.remote.ProfileApi
import com.polet.panoltli.feature_traductor.data.remote.TranslatorApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory // <-- 1. Import necesario
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.113:8080/")
            // 2. AGREGA ESTA LÍNEA PRIMERO (Para que lea los Strings planos del registro)
            .addConverterFactory(ScalarsConverterFactory.create())
            // 3. DESPUÉS ESTA (Para los demás endpoints que usan JSON como Login y OTP)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideDictionaryApi(retrofit: Retrofit): DictionaryApi = retrofit.create(DictionaryApi::class.java)

    @Provides
    @Singleton
    fun provideHistoryApi(retrofit: Retrofit): HistoryApi = retrofit.create(HistoryApi::class.java)

    @Provides
    @Singleton
    fun provideTranslatorApi(retrofit: Retrofit): TranslatorApi = retrofit.create(TranslatorApi::class.java)

    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi = retrofit.create(ProfileApi::class.java)
}