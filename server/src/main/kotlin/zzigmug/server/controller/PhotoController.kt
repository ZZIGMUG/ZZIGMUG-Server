package zzigmug.server.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
    @Operation(summary = "사진 등록 API")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "성공", content = [
            Content(mediaType = "application/json", array = (ArraySchema(schema = Schema(implementation = PhotoResponseDto::class))))]),
    ])
    @PostMapping
    fun savePhoto(
        @RequestPart image: MultipartFile,
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") @RequestParam time: LocalDateTime,
        request: HttpServletRequest): ResponseEntity<Any> {
        val userEmail = request.userPrincipal.name

        return ResponseEntity
            .ok()
            .body(photoService.extractDishesFromPhoto(image, time, userEmail))
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