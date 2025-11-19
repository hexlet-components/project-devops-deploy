package io.hexlet.project_devops_deploy.service;

import io.hexlet.project_devops_deploy.dto.BulletinDto;
import io.hexlet.project_devops_deploy.dto.BulletinRequest;
import io.hexlet.project_devops_deploy.exception.ResourceNotFoundException;
import io.hexlet.project_devops_deploy.model.Bulletin;
import io.hexlet.project_devops_deploy.mapper.BulletinMapper;
import io.hexlet.project_devops_deploy.repository.BulletinRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BulletinService {

    private final BulletinRepository repository;
    private final BulletinMapper mapper;

    public BulletinService(BulletinRepository repository, BulletinMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public BulletinDto create(BulletinRequest request) {
        Bulletin bulletin = mapper.toEntity(request);
        return mapper.toDto(repository.save(bulletin));
    }

    public void delete(Long id) {
        Bulletin bulletin = getBulletin(id);
        repository.delete(bulletin);
    }

    @Transactional(readOnly = true)
    public List<BulletinDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BulletinDto findById(Long id) {
        return mapper.toDto(getBulletin(id));
    }

    private Bulletin getBulletin(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bulletin has not found"));
    }

    public BulletinDto update(Long id, BulletinRequest request) {
        Bulletin bulletin = getBulletin(id);
        mapper.updateEntity(request, bulletin);
        return mapper.toDto(repository.save(bulletin));
    }
}
