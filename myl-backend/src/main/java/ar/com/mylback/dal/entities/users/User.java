package ar.com.mylback.dal.entities.users;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class User {
    @Id
    @Column(name = "uuid", nullable = false, length = 128)
    private String uuid;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin = false;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
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

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
