package io.hexlet.project_devops_deploy.dto;

import io.hexlet.project_devops_deploy.model.Bulletin;
import io.hexlet.project_devops_deploy.model.BulletinState;

public record BulletinDto(
        Long id,
        String title,
        String description,
        BulletinState state,
        String contact
) {
    public static BulletinDto fromEntity(Bulletin bulletin) {
        return new BulletinDto(
                bulletin.getId(),
                bulletin.getTitle(),
                bulletin.getDescription(),
                bulletin.getState(),
                bulletin.getContact()
        );
    }
}
