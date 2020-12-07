package com.humber.its2020.ibourit.server.controller

import com.humber.its2020.ibourit.server.entity.Image
import com.humber.its2020.ibourit.server.repository.ImageRepository
import com.humber.its2020.ibourit.server.util.FileUpload
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class ImageController (val repo: ImageRepository) {
    @PostMapping("/image")
    fun upload(
//        @RequestParam("userId") userId: Long,
//        @RequestParam("category") category: Int,
//        @RequestParam("articleId") articleId: String,
        @RequestParam("file") file: MultipartFile) {

        println("Test")
        println(file.originalFilename!!)
        FileUpload.saveFile("uploaded_images", file.originalFilename!!, file)
//        val image = Image(userId = userId, category = category, articleId = articleId)
//        repo.save(image)
    }

    @GetMapping("/image/{id}")
    fun getImage(@PathVariable id: Long): Optional<Image> {
        return repo.findById(id)
    }

    @GetMapping("/image")
    fun getAllImages(): List<Image> {
        return repo.findAll().toList();
    }
}