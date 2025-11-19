package io.hexlet.project_devops_deploy.component;

import io.hexlet.project_devops_deploy.repository.BulletinRepository;
import io.hexlet.project_devops_deploy.util.ModelGenerator;
import jakarta.annotation.PostConstruct;
import java.util.stream.IntStream;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private static final int BULLETIN_SEED_COUNT = 10;

    private final BulletinRepository repository;
    private final ModelGenerator modelGenerator;

    public DataInitializer(BulletinRepository repository, ModelGenerator modelGenerator) {
        this.repository = repository;
        this.modelGenerator = modelGenerator;
    }

    @PostConstruct
    public void seedData() {
        IntStream.range(0, BULLETIN_SEED_COUNT).mapToObj(i -> modelGenerator.generateBulletin())
                .forEach(repository::save);
    }
}
