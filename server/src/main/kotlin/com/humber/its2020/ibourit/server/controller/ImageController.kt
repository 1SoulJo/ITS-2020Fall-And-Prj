package com.humber.its2020.ibourit.server.controller

import com.humber.its2020.ibourit.server.constant.FileConstant.Companion.BASE_IMAGE_PATH
import com.humber.its2020.ibourit.server.entity.Image
import com.humber.its2020.ibourit.server.repository.ArticleRepository
import com.humber.its2020.ibourit.server.repository.ImageRepository
import com.humber.its2020.ibourit.server.util.FileUpload
import com.sun.imageio.plugins.common.ImageUtil
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
class ImageController (val repo: ImageRepository, val articleRepo: ArticleRepository) {
    @PostMapping("/image")
    fun upload(
        @RequestParam("userId") userId: String,
        @RequestParam("category") category: Int,
        @RequestParam("articleId") articleId: String,
        @RequestParam("file") file: MultipartFile,
        @RequestParam("imageOrder") imageOrder: Int) {

        FileUpload.saveFile(BASE_IMAGE_PATH, file.originalFilename!!, file)
        val image = Image(userId = userId, category = category, articleId = articleId,
            path = file.originalFilename!!, imageOrder = imageOrder)
        repo.save(image)

        val a = articleRepo.findByArticleId(articleId)
        a.images.add(file.originalFilename!!)
        articleRepo.save(a)
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