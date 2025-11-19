package io.hexlet.project_devops_deploy.service;

import io.hexlet.project_devops_deploy.dto.BulletinDto;
import io.hexlet.project_devops_deploy.dto.BulletinRequest;
import io.hexlet.project_devops_deploy.exception.BulletinNotFoundException;
import io.hexlet.project_devops_deploy.model.Bulletin;
import io.hexlet.project_devops_deploy.repository.BulletinRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BulletinService {

    private final BulletinRepository repository;

    public BulletinService(BulletinRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<BulletinDto> findAll() {
        return repository.findAll().stream()
                .map(BulletinDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public BulletinDto findById(Long id) {
        return BulletinDto.fromEntity(getBulletin(id));
    }

    public BulletinDto create(BulletinRequest request) {
        Bulletin bulletin = request.toEntity();
        return BulletinDto.fromEntity(repository.save(bulletin));
    }

    public BulletinDto update(Long id, BulletinRequest request) {
        Bulletin bulletin = getBulletin(id);
        request.updateEntity(bulletin);
        return BulletinDto.fromEntity(repository.save(bulletin));
    }

    public void delete(Long id) {
        Bulletin bulletin = getBulletin(id);
        repository.delete(bulletin);
    }

    private Bulletin getBulletin(Long id) {
        return repository.findById(id).orElseThrow(() -> new BulletinNotFoundException(id));
    }
}
