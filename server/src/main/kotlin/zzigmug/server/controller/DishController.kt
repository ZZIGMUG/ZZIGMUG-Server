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
import org.springframework.web.bind.annotation.*
import zzigmug.server.data.*
import zzigmug.server.service.DishService
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage

@Tag(name = "dish", description = "식사 정보 API")
@RestController
@RequestMapping("/dish")
class DishController(
    private val dishService: DishService,
) {

    @Operation(summary = "식사 정보 추가 API", description = "AI가 사진에서 발견하지 못한 음식을 사용자가 직접 등록할 수 있도록 합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = DishResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 사진을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PostMapping
    fun createDish(
        @Parameter(description = "사진 ID") @RequestParam photoId: Long,
        @Parameter(description = "추가하고 싶은 식사 정보") @RequestBody requestDto: DishRequestDto
    ): ResponseEntity<ResponseMessage> {

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            dishService.saveDish(photoId, requestDto)
        )
    }

    @Operation(summary = "식사량 수정 API", description = "식사의 양(1인분, 0.5인분 등)을 수정합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 식사 정보를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PatchMapping
    fun editCount(
        @Parameter(description = "수정할 식사 ID") @RequestParam dishId: Long,
        @Parameter(description = "수정할 식사 정보") @RequestBody requestDto: DishUpdateDto
    ): ResponseEntity<ResponseMessage> {
        dishService.editDish(dishId, requestDto)

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK
        )
    }

    @Operation(summary = "식사 정보 삭제 API", description = "사진 속의 식사 정보를 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 식사 정보를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @DeleteMapping
    fun deleteDish(@Parameter(description = "삭제할 식사 ID") @RequestParam dishId: Long): ResponseEntity<ResponseMessage> {
        dishService.deleteDish(dishId)

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK
        )
    }
}