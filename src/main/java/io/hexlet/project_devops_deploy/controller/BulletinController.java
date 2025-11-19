package io.hexlet.project_devops_deploy.controller;

import jakarta.validation.Valid;
import java.util.List;
import io.hexlet.project_devops_deploy.dto.BulletinDto;
import io.hexlet.project_devops_deploy.dto.BulletinRequest;
import io.hexlet.project_devops_deploy.service.BulletinService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bulletins")
public class BulletinController {

    private final BulletinService service;

    public BulletinController(BulletinService service) {
        this.service = service;
    }

    @GetMapping
    public List<BulletinDto> index() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public BulletinDto show(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BulletinDto create(@Valid @RequestBody BulletinRequest request) {
        return service.create(request);
    }

    @PutMapping("/{id}")
    public BulletinDto update(@PathVariable Long id, @Valid @RequestBody BulletinRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
