package io.bakerystud.commonapi.providers

import com.google.gson.*
import javax.inject.Inject
import javax.inject.Provider
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.JsonParseException
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException


class GsonProvider @Inject constructor(
) : Provider<Gson> {

    override fun get(): Gson =
        GsonBuilder()
            .create()
}

