package io.hexlet.project_devops_deploy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "bulletins")
public class Bulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BulletinState state = BulletinState.DRAFT;

    @Column(nullable = false)
    private String contact;

    public String getContact() {
        return contact;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public BulletinState getState() {
        return state;
    }

    public String getTitle() {
        return title;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setState(BulletinState state) {
        this.state = state;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
