package com.mlsdmitry.rozetkin_ctf_challenge

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import org.json.JSONObject
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.lang.Exception


data class Response(
    val login: String,
    val api_key: String,
    val error: Boolean,
    val message: String,
    val additional_info: String
)


data class RegisterData(
    val login: String,
    val uid: String,
    val card_number: Long
)

class RozetkinCard(
    var login: String,
    private val uid: ByteArray,
    var cardNumber: Long
) {
    public var previousUid: ByteArray = uid

    private val client = HttpClient(OkHttp) {
        engine {
            config {
                retryOnConnectionFailure(true)
            }
        }
        install(JsonFeature) {
            serializer = GsonSerializer()
            acceptContentTypes += ContentType.Application.Json

        }
    }

    //    private val BASE_URL = "http://" + Resources.getSystem().getString(R.string.server_host)
    private val BASE_URL = "http://2537ly.space:23234/"
    var apiKey: String = ""

    fun ByteArray.toHexString(): String {
        return this.joinToString("") {
            java.lang.String.format("%02x", it)
        }
    }

    suspend fun register(): Response {
        val data = JSONObject(
            mapOf(
                "login" to login,
                "card_number" to cardNumber,
                "uid" to uid.toHexString()
            )
        )
        Log.d("RozetkinCard", data.toString())
        val response = client.post<Response>("$BASE_URL/register") {
            body = FormDataContent(Parameters.build {
                append("data", data.toString())
            })
        }

//        val response = Gson().fromJson(success, Response::class.java)
        Log.d("RozetkinActivity:60", "Raw String response: $response")
//        val response = Gson().fromJson(response, Response::class.java)
        Log.d("RozetkinCard", "response: $response")

        if (!response.error) {
            apiKey = response.api_key
            return response
        } else {
            return response
        }

    }

    suspend fun getApiKey(): Response? {
        val data = JSONObject(
            mapOf(
                "login" to login,
                "card_number" to cardNumber,
                "uid" to uid.toHexString()
            )
        )
        Log.d("RozetkinCard", data.toString())
        val response: Response
        try {
            response = client.post<Response>("$BASE_URL/get_api_key") {
                body = FormDataContent(Parameters.build {
                    append("data", data.toString())
                })
            }
        } catch (e: Exception) {
            Log.d("RozetkinCard", "Could get response; Error: $e")
            return null
        }

//        val response_data = Gson().fromJson(response, Response::class.java)
        Log.d("RozetkinCard", "response: $response")
        if (!response.error) {
            Log.d("RozetkinCard:117", "Setting api key ...")
            apiKey = response.api_key
            return response
        } else {
            return response
        }

    }

    suspend fun sendVerify(error: Boolean = false): Response? {
        var response: Response?
        try {
            response = client.get<Response>("$BASE_URL/verify") {
                parameter("api_key", apiKey)
                parameter("login", login)
                parameter("error", "")
            }
        } catch (e: Exception) {
            Log.d("RozetkinCard:134", "Error: $e")
            return null
        }
//        val response_data = Gson().fromJson(response, Response::class.java)
        Log.d("RozetkinCard:140", "response: $response")
//        return response_data
        return response
    }

    companion object {
        suspend fun getInfo(lapiKey: String): Response {
            val client = HttpClient(OkHttp) {
                engine {
                    config {
                        retryOnConnectionFailure(true)
                    }
                }
                install(JsonFeature) {
                    serializer = GsonSerializer()
                    acceptContentTypes += ContentType.Application.Json

                }
            }
            // TODO repalce hardcoded ip
            val response = client.get<Response>("http://2537ly.space:23234//get_info") {
                parameter("api_key", lapiKey)
            }
//        val response = Gson().fromJson(success, Response::class.java)
            Log.d("RozetkinCard:155", "success: $response")
            return response
        }

        fun checkCardNumber(uid: ByteArray, cardNumber: Long): Boolean {
            var j = 0
            for ((i, byte) in uid.asIterable().withIndex()) {
                j += (byte.toInt() and 255) shl (i shl 3)
            }
            return "1232387$j" == cardNumber.toString()
        }
    }

    suspend fun getInfo(): Response {
        val response = client.get<Response>("$BASE_URL/get_info") {
            parameter("api_key", apiKey)
        }
//        val response = Gson().fromJson(success, Response::class.java)
        Log.d("RozetkinCard", "success: $response")
        return response
    }


    //    def calc_card_num(byte_uid: bytes):
//    j = 0
//    for i, c in enumerate(byte_uid):
//    j += (c & 255) << (i << 3)
//    return j

    fun toLInt(bytes: ByteArray): Int {
        var result = 0
        for (i in bytes.indices) {
            result = result or (bytes[i].toInt() shl 8 * i)
        }
        return result
    }

}

