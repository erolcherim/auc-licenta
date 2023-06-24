package com.unibuc.auclicenta.service;

import com.unibuc.auclicenta.exception.EntityNotFoundException;
import com.unibuc.auclicenta.exception.FileTypeNotAcceptedException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class StorageService {
    public void saveImage(MultipartFile multipartFile, String fileName) throws IOException {
        if (Objects.equals(FilenameUtils.getExtension(multipartFile.getOriginalFilename()), "jpg") || Objects.equals(FilenameUtils.getExtension(multipartFile.getOriginalFilename()), "jpeg")) {
            String destinationFolder = "./photos/";
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(destinationFolder + fileName);
            Files.write(path, bytes);
        } else {
            throw new FileTypeNotAcceptedException();
        }
    }

    public byte[] getImage(String id) {
        try {
            FileInputStream in = new FileInputStream("./photos/" + id + ".jpg");
            return in.readAllBytes();
        } catch (IOException e) {
            throw new EntityNotFoundException();
        }
    }

    public boolean deleteImage(String id) {
        File image = new File("./photos" + id + ".jpg");
        return image.delete();
    }
}
