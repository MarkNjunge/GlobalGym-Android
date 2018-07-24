package com.marknkamau.globalgym.utils

import com.marknkamau.globalgym.data.models.ApiResponse
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

/**
 * Created by MarkNjunge.
 * mark.kamau@outlook.com
 * https://github.com/MarkNjunge
 */

class NetworkUtilsTest {


    @Test
    fun should_parseError() {
        val responseBody = ResponseBody.create(MediaType.parse("application/json"), "{\"message\": \"Server error\"}")
        val response = Response.error<ApiResponse>(500, responseBody)
        val error = NetworkUtils.parseError(response)

        Assert.assertEquals("Server error", error.message)
    }
}