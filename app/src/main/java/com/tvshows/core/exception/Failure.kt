package com.tvshows.core.exception

import com.google.gson.annotations.SerializedName

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureFailure] class.
 */
sealed class Failure {
    class NetworkConnection: Failure()
    class ServerError: Failure()

    data class APIError(
            @SerializedName("status_message")
            var statusMessage: String,
            @SerializedName("status_code")
            var statusCode: Int
    ) : Failure()

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureFailure: Failure()
}
