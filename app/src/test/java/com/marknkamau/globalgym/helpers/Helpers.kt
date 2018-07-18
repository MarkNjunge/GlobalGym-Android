package com.marknkamau.globalgym.helpers

import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

object Helpers {
    fun createHttpErrorResponse(code: Int, message: String = "{\"message\": \"Server error\"}"): Response<HttpException> {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), message)
        return Response.error<HttpException>(code, responseBody)
    }
}