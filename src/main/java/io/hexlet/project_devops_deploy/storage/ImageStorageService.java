package io.hexlet.project_devops_deploy.storage;

import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String upload(String keyPrefix, MultipartFile file);

    Optional<String> getUrl(String key);
}
