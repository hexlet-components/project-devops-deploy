package io.hexlet.project_devops_deploy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BulletinNotFoundException extends RuntimeException {

    public BulletinNotFoundException(Long id) {
        super("Bulletin %d not found".formatted(id));
    }
}
