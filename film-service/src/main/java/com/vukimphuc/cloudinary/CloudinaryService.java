package com.vukimphuc.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public Map<String, String> uploadFile(MultipartFile multipartFile) throws IOException {
        Map<String, Object> params = new HashMap<>();
        String publicId = UUID.randomUUID().toString();
        params.put("public_id", publicId);
        params.put("folder", "cinema_online");

        Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), params);

        Map<String, String> result = new HashMap<>();
        result.put("publicId", uploadResult.get("public_id").toString());
        result.put("url", uploadResult.get("url").toString());
        return result;
    }

    public void deleteFile(String publicId) throws IOException{
        cloudinary.uploader().destroy(publicId, null);
    }
}
