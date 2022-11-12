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
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import zzigmug.server.data.*
import zzigmug.server.service.UserService
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage
import javax.servlet.http.HttpServletRequest

@Tag(name = "user", description = "회원정보 API")
@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {
    @Operation(summary = "ID로 회원 조회 API", description = "ID로 회원을 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @GetMapping("/{userId}")
    fun readUserInfo(@Parameter(description = "유저 ID") @PathVariable userId: Long): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            userService.readUserById(userId)
        )
    }

    @Operation(summary = "회원 리스트 조회 API", description = "키워드로 이메일과 닉네임을 검색해 회원 리스트를 조회합니다. (검색 키워드가 없을 시 전체 회원 리스트를 조회합니다.)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserPageResponseDto::class))))]),
    ])
    @GetMapping("/list")
    fun searchUser(
        @Parameter(description = "몇 번째 페이지") @RequestParam(required = false) page: Int = 0,
        @Parameter(description = "페이지 크기") @RequestParam(required = false) size: Int = 10,
        @Parameter(description = "검색 키워드") @RequestParam(required = false) keyword: String?,
        ): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            userService.readAll(PageRequest.of(page, size), keyword)
        )
    }

    @Operation(summary = "닉네임 수정 API", description = "회원의 닉네임을 수정합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PatchMapping("/nickname")
    fun editNickname(
        @Parameter(description = "새로운 닉네임") @RequestBody requestDto: UserUpdateRequestDto,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val email = request.userPrincipal.name
        userService.editNickname(email, requestDto.nickname)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }
}