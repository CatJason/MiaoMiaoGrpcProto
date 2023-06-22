package com.miao.protobufdemo
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.withContext
import string_service.StringServiceGrpc
import string_service.StringServiceOuterClass
import string_service.StringServiceOuterClass.StringRequest
import string_service.StringServiceOuterClass.StringResponse

class StringServiceClient {
    private val YOUR_SERVER_PORT = 0

    private val channel by lazy {
        val builder = ManagedChannelBuilder.forAddress("YOUR_SERVER_HOST", YOUR_SERVER_PORT)
        builder.usePlaintext() // 在开发环境下使用，不使用 TLS 加密
        builder.executor(Dispatchers.Default.asExecutor()) // 使用 Kotlin 协程
        builder.build()
    }

    private val stub by lazy { StringServiceGrpc.newStub(channel) }

    suspend fun getStringValue(requestValue: String): String = withContext(Dispatchers.Default) {
        val request = StringRequest.newBuilder()
            .build()

        val response = CompletableDeferred<StringServiceOuterClass.StringResponse>()

        stub.getString(request, object : StreamObserver<StringServiceOuterClass.StringResponse> {
            override fun onNext(value: StringResponse) {
                response.complete(value)
            }

            override fun onError(t: Throwable) {
                response.completeExceptionally(t)
            }

            override fun onCompleted() {}
        })

        response.await().value
    }
}
