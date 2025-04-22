package ar.com.mylback.dal.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "key_words")
public class KeyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_word_id")
    private Integer id;

    @Column(name = "key_word_name", length = 50,nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "keyWords")
    private Set<Card> cards = new HashSet<>();

    public KeyWord() {}

    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
