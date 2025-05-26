package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public abstract class User {
    @Id
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID uuid;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "name", length = 100)
    private String name;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
