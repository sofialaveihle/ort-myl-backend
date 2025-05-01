package ar.com.mylback.dal.entities;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class CardProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;
    @Column(nullable = false, unique = true)
    protected String name;

    public CardProperties() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
