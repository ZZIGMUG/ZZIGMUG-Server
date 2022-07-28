package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.UserInfo
import zzigmug.server.data.UserPage
import zzigmug.server.service.UserService
import zzigmug.server.utils.exception.ErrorResponse
import zzigmug.server.utils.exception.ResponseMessage
import javax.servlet.http.HttpServletRequest

@Tag(name = "user", description = "회원정보 API")
@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "ID로 회원 조회 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserInfo::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @GetMapping("/{userId}")
    fun readUserInfo(@Parameter(description = "유저 ID") @PathVariable userId: Long): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(userService.readUserById(userId))
    }

    @Operation(summary = "회원 리스트 조회 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserPage::class))))]),
    ])
    @GetMapping("/list")
    fun searchUser(
        @Parameter(description = "검색 조건") @RequestParam params: MutableMap<String, String>
    ): ResponseEntity<Any> {
        val page = params["page"]?.toInt() ?: 0
        val size = params["size"]?.toInt() ?: 10

        return ResponseEntity
            .ok()
            .body(userService.readAll(PageRequest.of(page, size), params))
    }

    @Operation(summary = "닉네임 수정 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PatchMapping("/nickname")
    fun editNickname(
        @Parameter(description = "새로운 닉네임") @RequestParam nickname: String,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        val email = request.userPrincipal.name
        userService.editNickname(email, nickname)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }
}