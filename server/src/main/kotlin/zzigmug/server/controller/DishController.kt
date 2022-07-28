package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.DishRequestDto
import zzigmug.server.data.DishResponseDto
import zzigmug.server.data.DishUpdateDto
import zzigmug.server.data.LoginResponseDto
import zzigmug.server.service.DishService
import zzigmug.server.utils.exception.ErrorResponse
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage

@Tag(name = "dish", description = "식사 정보 API")
@RestController
@RequestMapping("/dish")
class DishController(
    private val dishService: DishService,
) {

    @Operation(summary = "식사 정보 추가 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = DishResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 사진을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @PostMapping
    fun createDish(
        @Parameter(description = "식사가 담긴 사진 ID") @RequestParam photoId: Long,
        @RequestBody requestDto: DishRequestDto
    ): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(dishService.createDish(photoId, requestDto))
    }

    @Operation(summary = "식사량 수정 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 식사 정보를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @PatchMapping
    fun editCount(
        @Parameter(description = "수정할 식사 ID") @RequestParam dishId: Long,
        @RequestBody requestDto: DishUpdateDto
    ): ResponseEntity<Any> {
        dishService.editDish(dishId, requestDto)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(status = HttpStatus.OK.value(), data = "사용 가능한 닉네임입니다."))
    }

    @Operation(summary = "식사 정보 삭제 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 식사 정보를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @DeleteMapping
    fun deleteDish(@Parameter(description = "삭제할 식사 ID") @RequestParam dishId: Long): ResponseEntity<Any> {
        dishService.deleteDish(dishId)
        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }
}