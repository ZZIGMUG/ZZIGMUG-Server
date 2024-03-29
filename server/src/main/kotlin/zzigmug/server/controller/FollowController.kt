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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.UserFollowingResponseDto
import zzigmug.server.service.FollowService
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage
import javax.servlet.http.HttpServletRequest

@Tag(name = "follow", description = "팔로우/팔로잉 API")
@RestController
@RequestMapping("/follow")
class FollowController(
    private val followService: FollowService
) {
    @Operation(summary = "팔로우 API", description = "파라미터로 넘어온 ID의 회원을 팔로우합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "401", description = "이미 상대방을 팔로우하고 있습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PostMapping("/{followingId}")
    fun followUser(
        @Parameter(description = "팔로우할 유저 ID") @PathVariable followingId: Long,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val userEmail = request.userPrincipal.name
        followService.followUser(followingId, userEmail)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }

    @Operation(summary = "언팔로우 API", description = "파라미터로 넘어온 ID의 회원을 언팔로우합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 유저를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "상대방을 팔로우하고 있지 않습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @DeleteMapping("/{followingId}")
    fun unfollowUser(
        @Parameter(description = "언팔로우할 유저 ID") @PathVariable followingId: Long,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val userEmail = request.userPrincipal.name
        followService.unfollowUser(followingId, userEmail)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }

    @Operation(summary = "팔로워 목록 조회 API", description = "나를 팔로우하는 회원 리스트를 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserFollowingResponseDto::class))))]),
            ])
    @GetMapping("/follower")
    fun readAllFollower(
        @Parameter(description = "몇 번째 페이지") @RequestParam(required = false) page: Int = 0,
        @Parameter(description = "페이지 크기") @RequestParam(required = false) size: Int = 10,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val email = request.userPrincipal.name

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            followService.readAllFollower(PageRequest.of(page, size), email)
        )
    }

    @Operation(summary = "팔로잉 목록 조회 API", description = "내가 팔로우 중인 회원 리스트를 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = UserFollowingResponseDto::class))))]),
    ])
    @GetMapping("/following")
    fun readAllFollowing(
        @Parameter(description = "몇 번째 페이지") @RequestParam(required = false) page: Int = 0,
        @Parameter(description = "페이지 크기") @RequestParam(required = false) size: Int = 10,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val email = request.userPrincipal.name

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            followService.readAllFollowing(PageRequest.of(page, size), email)
        )
    }
}