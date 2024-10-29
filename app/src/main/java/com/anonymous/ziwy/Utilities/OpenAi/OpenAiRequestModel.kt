package com.anonymous.ziwy.Utilities.OpenAi

import kotlinx.serialization.Serializable

@Serializable
data class OpenAiRequestModel(
    val model: String,
    val messages: List<Message>,
    val max_tokens: Int,
    val response_format: ResponseFormat
)

@Serializable
data class Message(
    val role: String,
    val content: List<Content>
)

@Serializable
data class Content(
    val type: String,
    val image_url: ImageUrl? = null,
    val text: String? = null
)

@Serializable
data class ImageUrl(
    val url: String
)

@Serializable
data class ResponseFormat(
    val type: String
)

/*
const payload = {
      model: 'gpt-4o',  // Verify model name
      messages: [
        {
          role: 'user',
          content: [
            {
              type: 'image_url',
              image_url: {
                url: base64,  // Send image as base64
              }
            },
            {
              type: 'text',
              text: PROMPT,

            }
          ],
        }
      ],
      max_tokens: 300,
    response_format: {
        type: "json_object"
    }
};
*/
