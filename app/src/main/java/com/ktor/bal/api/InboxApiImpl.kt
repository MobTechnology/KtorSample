package com.ktor.bal.api

import com.google.gson.Gson
import com.ktor.bal.core.ApiServices
import com.ktor.bal.model.InboxPostData
import com.ktor.bal.model.InboxResponseData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.http.parameters
import java.io.BufferedReader
import java.io.InputStreamReader

class InboxApiImpl(
    private val client: HttpClient
) : InboxApi {

    override suspend fun getInbox(inboxPostData: InboxPostData): InboxResponseData? {
        var ret: InboxResponseData? = null
        try {
            client.submitForm(
                url = ApiServices.BASE_URL,
                formParameters = parameters {
                    append("data", Gson().toJson(inboxPostData))
                    append(
                        "token",
                        "eyJFbXBsb3llZVNpZCI6OSwiRXhwaXJlcyI6bnVsbCwiRG9tYWluSWQiOjIsIlN0b3JlRG9tYWluSWQiOjAsIk9yZ0lkIjoyLCJJc3N1ZWRUaW1lIjoxNjg5MTQ0OTI3ODI3LCJMb2dpbk5hbWUiOiJKRE9FIiwiU2lnbmF0dXJlIjoiYjVZeGUvWXdNaXFSZm02YzlOK0xxQU4wNWZpc3k0d3hxR1lmeWllUEMvUT0iLCJUb2tlbiI6InVTTFdtMVpRRnBTRW1BV0h6alhhQmhxWUt5SFJMazRLdThiTDM2dytxT1E9In0="
                    )
                },
                encodeInQuery = false,
                block = {
                    method = HttpMethod.Post
                    headers {
                        contentType(ContentType.Application.Json)
                        header("Content-Type", "application/x-www-form-urlencoded")
                    }
                },
            ).run {
                val reader = BufferedReader(InputStreamReader(this.call.body()))
                var line: String?
                val response = StringBuffer()
                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                    response.append('\r')
                }

                ret =
                    Gson().fromJson(response.toString(), InboxResponseData::class.java)
            }
        } catch (ex: RedirectResponseException) {
            // 3xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ClientRequestException) {
            // 4xx - responses
            println("Error: ${ex.response.status.description}")
            null
        } catch (ex: ServerResponseException) {
            // 5xx - response
            println("Error: ${ex.response.status.description}")
            null
        }

        return ret
    }
}

