package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import zzigmug.server.data.CalorieResponseDto
import zzigmug.server.data.NutrientResponseDto
import zzigmug.server.data.PhotoRequestDto
import zzigmug.server.data.PhotoResponseDto
import zzigmug.server.service.PhotoService
import zzigmug.server.utils.exception.ErrorResponse
import zzigmug.server.utils.exception.ResponseMessage
import java.time.LocalDate
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest

@Tag(name = "photo", description = "음식 사진 API")
@RestController
@RequestMapping("/photo")
class PhotoController(
    private val photoService: PhotoService,
) {

    @Operation(summary = "사진에서 음식 추출 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping("/extract", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    fun extractFoodFromPhoto(
        @RequestPart(name = "image") image: MultipartFile,
        request: HttpServletRequest)
    : ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(photoService.extractDishesFromPhoto(image))
    }


    @Operation(summary = "사진 저장 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping("/save", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    fun savePhoto(
        @RequestBody requestDto: PhotoRequestDto,
        request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.savePhoto(requestDto, userEmail))
    }

    @Operation(summary = "날짜로 사진 조회 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @GetMapping
    fun readByDate(@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate, request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.readPhotosByDate(date, userEmail))
    }

    @Operation(summary = "주간 칼로리 정보 조회 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = CalorieResponseDto::class))))]),
    ])
    @GetMapping("/week/calorie")
    fun readWeeklyCalories(
        @Parameter(description = "조회할 날짜") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.getWeeklyCalories(userEmail, date))
    }

    @Operation(summary = "오늘의 영양소 정보 조회 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = NutrientResponseDto::class))))]),
    ])
    @GetMapping("/nutrient")
    fun readNutrients(
        @Parameter(description = "조회할 날짜") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate,
        request: HttpServletRequest
    ): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.getNutrients(userEmail, date))
    }

    @Operation(summary = "사진 삭제 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 사진 데이터를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @DeleteMapping
    fun deletePhoto(@RequestParam photoId: Long): ResponseEntity<Any> {
        photoService.deletePhoto(photoId)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }
}