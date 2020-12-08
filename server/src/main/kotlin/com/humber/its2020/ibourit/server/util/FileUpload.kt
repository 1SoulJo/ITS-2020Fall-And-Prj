package com.humber.its2020.ibourit.server.util

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption


class FileUpload {
    companion object {
        @Throws(IOException::class)
        fun saveFile(uploadDir: String, fileName: String, multipartFile: MultipartFile) : Path {
            val uploadPath: Path = Paths.get(uploadDir)
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath)
            }
            try {
                multipartFile.inputStream.use { inputStream ->
                    val filePath: Path = uploadPath.resolve(fileName)
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)
                    return filePath
                }
            } catch (ioe: IOException) {
                throw IOException("Could not save image file: $fileName", ioe)
            }
        }
    }
}