package com.anonymous.ziwy.Utilities.OpenAi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiResponseModel(
    var id: String? = null,
    @SerialName("object") var objectValue: String? = null,
    var created: Int? = null,
    var model: String? = null,
    var choices: ArrayList<Choices>? = null,
    var usage: Usage? = Usage(),
    var system_fingerprint: String? = null
)

@Serializable
data class ResMessage(
    var role: String? = null,
    var content: String? = null,
    var refusal: String? = null
)

@Serializable
data class Choices(
    var index: Int? = null,
    var message: ResMessage? = null,
    var logprobs: String? = null,
    var finish_reason: String? = null
)

@Serializable
data class PromptTokensDetails(
    var cached_tokens: Int? = null
)

@Serializable
data class Usage(
    var prompt_tokens: Int? = null,
    var completion_tokens: Int? = null,
    var total_tokens: Int? = null,
    var prompt_tokens_details: PromptTokensDetails? = null,
    var completion_tokens_details: CompletionTokensDetails? = null
)

@Serializable
data class CompletionTokensDetails(
    var reasoning_tokens: Int? = null
)
