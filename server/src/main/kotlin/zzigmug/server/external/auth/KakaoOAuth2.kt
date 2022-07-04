package zzigmug.server.external.auth

import org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import zzigmug.server.data.property.KakaoProperty

@Component
class KakaoOAuth2(
    private val restTemplate: RestTemplate,
    private val kakaoProperty: KakaoProperty,
) {
    fun getUserInfo(authorizedCode: String): KakaoUserInfo {
        val accessToken = getAccessToken(authorizedCode)
        return getUserInfoByAccessToken(accessToken)
    }

    fun getAccessToken(authorizedCode: String): String {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val params = LinkedMultiValueMap<String, String>()
        params.add("grant_type", "authorization_code")
        params.add("client_id", kakaoProperty.restApiKey)
        params.add("redirect_uri", kakaoProperty.redirectUri)
        params.add("code", authorizedCode)

        val httpEntity = HttpEntity<MultiValueMap<String, String>>(params, headers)

        val response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )

        val jsonObject = JSONObject(response.body)
        return jsonObject.getString("access_token")
    }

    private fun getUserInfoByAccessToken(accessToken: String): KakaoUserInfo {
        val headers = HttpHeaders()
        headers.setBearerAuth(accessToken)
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val httpEntity = HttpEntity<MultiValueMap<String, String>>(headers)
        val response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            httpEntity,
            String::class.java
        )

        val body = JSONObject(response.body)
        val id = body.getLong("id")
        val email = body.getJSONObject("kakao_account").getString("email")

        return KakaoUserInfo(id, email)
    }
}

data class KakaoUserInfo(
    val id: Long,
    val email: String,
)