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

    @Operation(summary = "사진에서 음식 추출 API", description = "파라미터로 보낸 사진 파일에서 음식 데이터를 추출합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping("/extract", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    fun extractFoodFromPhoto(
        @Parameter(description = "음식을 추출하고 싶은 사진 파일") @RequestPart(name = "image") image: MultipartFile,
        request: HttpServletRequest)
    : ResponseEntity<Any> {
        return ResponseEntity
            .ok()
            .body(photoService.extractDishesFromPhoto(image))
    }


    @Operation(summary = "사진 저장 API", description = "/photo/extract API에서 사진 속 음식을 추출한 다음, 사진을 저장하고 싶을 때 호출합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping("/save", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    fun savePhoto(
        @Parameter(description = "사진 관련 정보") @RequestBody requestDto: PhotoRequestDto,
        request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.savePhoto(requestDto, userEmail))
    }

    @Operation(summary = "날짜로 사진 조회 API", description = "해당 날짜에 저장된 사진들을 모두 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @GetMapping
    fun readByDate(
        @Parameter(description = "조회할 날짜") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate,
        request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.readPhotosByDate(date, userEmail))
    }

    @Operation(summary = "주간 칼로리 정보 조회 API", description = "파라미터에 입력한 날짜부터 해당 날짜의 7일 전까지 섭취한 칼로리 정보를 조회합니다.")
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

    @Operation(summary = "오늘의 영양소 정보 조회 API", description = "date 파라미터에 입력한 날짜 하루 동안 섭취한 영양소 정보를 모두 조회합니다.")
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

    @Operation(summary = "사진 삭제 API", description = "파라미터에 입력한 ID의 사진과 관련 데이터를 모두 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 사진 데이터를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ErrorResponse::class))))]),
    ])
    @DeleteMapping
    fun deletePhoto(@Parameter(description = "삭제할 사진 ID") @RequestParam photoId: Long): ResponseEntity<Any> {
        photoService.deletePhoto(photoId)

        return ResponseEntity
            .ok()
            .body(ResponseMessage(HttpStatus.OK.value(), "성공"))
    }
}