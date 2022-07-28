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
import zzigmug.server.utils.exception.ErrorResponse
import zzigmug.server.utils.exception.ResponseMessage

@Tag(name = "food", description = "음식 리스트 API")
@RestController
@RequestMapping("/food")
class FoodController(
    private val foodService: FoodService,
) {

    @Operation(summary = "음식 추가 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PostMapping
    fun createFood(@RequestBody requestDto: FoodRequestDto): ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(foodService.createFood(requestDto))
    }

    @Operation(summary = "음식 목록 조회 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @GetMapping
    fun readFoodList(@Parameter(description = "검색 조건") @RequestParam params: MutableMap<String, String>): ResponseEntity<Any> {
        val page = params["page"]?.toInt() ?: 0
        val size = params["size"]?.toInt() ?: 10

        return ResponseEntity
            .ok()
            .body(foodService.readAll(PageRequest.of(page, size), params))
    }

    @Operation(summary = "음식 삭제 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 음식을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @DeleteMapping
    fun deleteFood(@Parameter(description = "음식 ID") @RequestParam foodId: Long): ResponseEntity<Any> {
        foodService.deleteFood(foodId)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }

}