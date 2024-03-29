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
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import zzigmug.server.data.*
import zzigmug.server.service.DishService
import zzigmug.server.service.PhotoService
import zzigmug.server.utils.exception.ResponseCode
import zzigmug.server.utils.exception.ResponseMessage
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@Tag(name = "photo", description = "음식 사진 API")
@RestController
@RequestMapping("/photo")
class PhotoController(
    private val photoService: PhotoService,
    private val dishService: DishService,
) {

    @Operation(summary = "사진에서 음식 추출 API", description = "파라미터로 보낸 사진 파일에서 음식 데이터를 추출합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping("/extract", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    fun extractDishesFromPhoto(
        @Parameter(description = "음식을 추출하고 싶은 사진 파일") @RequestPart(name = "image") image: MultipartFile,
        request: HttpServletRequest)
    : ResponseEntity<ResponseMessage> {
        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            photoService.extractDishesFromPhoto(image)
        )
    }


    @Operation(summary = "사진 저장 API", description = "/photo/extract API에서 사진 속 음식을 추출한 다음, 사진을 저장하고 싶을 때 호출합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping("/save", consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE])
    fun savePhoto(
        @Parameter(description = "사진 관련 정보") @RequestBody requestDto: PhotoRequestDto,
        request: HttpServletRequest): ResponseEntity<ResponseMessage> {
        val userEmail = request.userPrincipal.name

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            photoService.savePhoto(requestDto, userEmail)
        )
    }

    @Operation(summary = "사진에 식사 정보 추가 API", description = "AI가 사진에서 발견하지 못한 음식을 사용자가 직접 등록할 수 있도록 합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = DishResponseDto::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 사진을 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PostMapping("/{photoId}/dish")
    fun createDish(
        @Parameter(description = "사진 ID") @PathVariable photoId: Long,
        @Parameter(description = "추가하고 싶은 식사 정보") @RequestBody requestDto: DishRequestDto
    ): ResponseEntity<ResponseMessage> {

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            dishService.createDish(photoId, requestDto)
        )
    }

    @Operation(summary = "식사량 수정 API", description = "식사의 양(1인분, 0.5인분 등)을 수정합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 식사 정보를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @PatchMapping("/dish/{dishId}")
    fun editDishCount(
        @Parameter(description = "수정할 식사 ID") @PathVariable dishId: Long,
        @Parameter(description = "수정할 식사 정보") @RequestBody requestDto: DishUpdateRequestDto
    ): ResponseEntity<ResponseMessage> {
        dishService.editDishCount(dishId, requestDto)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }

    @Operation(summary = "식사 정보 삭제 API", description = "사진 속의 식사 정보를 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 식사 정보를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @DeleteMapping("/dish/{dishId}")
    fun deleteDish(@Parameter(description = "삭제할 식사 ID") @PathVariable dishId: Long): ResponseEntity<ResponseMessage> {
        dishService.deleteDish(dishId)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }

    @Operation(summary = "날짜로 사진 조회 API", description = "해당 날짜에 저장된 사진들을 모두 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @GetMapping
    fun readPhotoByDate(
        @Parameter(description = "조회할 날짜") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate,
        request: HttpServletRequest): ResponseEntity<ResponseMessage> {
        val userEmail = request.userPrincipal.name

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            photoService.readPhotosByDate(date, userEmail)
        )
    }

    @Operation(summary = "주간 칼로리 정보 조회 API", description = "파라미터에 입력한 날짜부터 해당 날짜의 7일 전까지 섭취한 칼로리 정보를 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoCalorieResponseDto::class))))]),
    ])
    @GetMapping("/week/calorie")
    fun readWeeklyCalories(
        @Parameter(description = "조회할 날짜") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val userEmail = request.userPrincipal.name

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            photoService.readWeeklyCalories(userEmail, date)
        )
    }

    @Operation(summary = "오늘의 영양소 정보 조회 API", description = "date 파라미터에 입력한 날짜 하루 동안 섭취한 영양소 정보를 모두 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoNutrientResponseDto::class))))]),
    ])
    @GetMapping("/nutrient")
    fun readNutrients(
        @Parameter(description = "조회할 날짜") @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: LocalDate,
        request: HttpServletRequest
    ): ResponseEntity<ResponseMessage> {
        val userEmail = request.userPrincipal.name

        return ResponseMessage.toResponseEntity(
            ResponseCode.OK,
            photoService.readNutrients(userEmail, date)
        )
    }

    @Operation(summary = "사진 삭제 API", description = "파라미터에 입력한 ID의 사진과 관련 데이터를 모두 삭제합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
        ApiResponse(responseCode = "404", description = "해당 ID의 사진 데이터를 찾을 수 없습니다.", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = ResponseMessage::class))))]),
    ])
    @DeleteMapping("/{photoId}")
    fun deletePhoto(@Parameter(description = "삭제할 사진 ID") @PathVariable photoId: Long): ResponseEntity<ResponseMessage> {
        photoService.deletePhoto(photoId)

        return ResponseMessage.toResponseEntity(ResponseCode.OK)
    }
}