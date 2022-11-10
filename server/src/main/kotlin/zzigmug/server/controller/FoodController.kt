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
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import zzigmug.server.data.FoodRequestDto
import zzigmug.server.service.FoodService
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage

@Tag(name = "food", description = "음식 리스트 API")
@RestController
@RequestMapping("/food")
class FoodController(
    private val foodService: FoodService,
) {

    @Operation(summary = "음식 추가 API", description = "음식 및 칼로리 정보를 추가합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PostMapping
    fun createFood(@RequestBody requestDto: FoodRequestDto): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            foodService.createFood(requestDto)
        )
    }

    @Operation(summary = "음식 목록 조회 API", description = "음식 및 칼로리 정보를 조회합니다. keyword 파라미터에 들어간 데이터가 없을 시 전체 리스트를 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @GetMapping
    fun readFoodList(
        @Parameter(description = "몇 번째 페이지 (0부터 시작)") @RequestParam(required = false) page: Int = 0,
        @Parameter(description = "페이지 크기") @RequestParam(required = false) size: Int = 10,
        @Parameter(description = "검색 키워드") @RequestParam keyword: String?
    ): ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            foodService.readAll(PageRequest.of(page, size), keyword)
        )
    }

    @Operation(summary = "음식 삭제 API", description = "음식 및 칼로리 정보를 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 음식을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @DeleteMapping
    fun deleteFood(@Parameter(description = "음식 ID") @RequestParam foodId: Long): ResponseEntity<ResponseMessage> {
        foodService.deleteFood(foodId)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }

}