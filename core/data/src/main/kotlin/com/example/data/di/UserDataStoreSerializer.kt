package com.example.data.di


import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.example.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject


class UserPreferencesSerializer @Inject constructor() : Serializer<User> {

    override val defaultValue: User = User()

    override suspend fun readFrom(input: InputStream): User {
        try {
            return Json.decodeFromString(
                User.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Error User Preferences", serialization)
        }
    }

    override suspend fun writeTo(t: User, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(User.serializer(), t).encodeToByteArray())
        }
    }
}