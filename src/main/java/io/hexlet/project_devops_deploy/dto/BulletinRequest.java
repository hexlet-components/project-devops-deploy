package io.hexlet.project_devops_deploy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.hexlet.project_devops_deploy.model.Bulletin;
import io.hexlet.project_devops_deploy.model.BulletinState;

public record BulletinRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull BulletinState state,
        @NotBlank String contact
) {
    public Bulletin toEntity() {
        Bulletin bulletin = new Bulletin();
        bulletin.setTitle(title);
        bulletin.setDescription(description);
        bulletin.setState(state);
        bulletin.setContact(contact);
        return bulletin;
    }

    public void updateEntity(Bulletin bulletin) {
        bulletin.setTitle(title);
        bulletin.setDescription(description);
        bulletin.setState(state);
        bulletin.setContact(contact);
    }
}
