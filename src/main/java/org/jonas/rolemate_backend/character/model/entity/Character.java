package org.jonas.rolemate_backend.character.model.entity;

import jakarta.persistence.*;
import org.jonas.rolemate_backend.user.model.entity.CustomUser;


@Entity
@Table(name = "character")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int level;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser owner;

    public Character() {
    }

    public Character(String name, int level, CustomUser owner) {
        this.name = name;
        this.level = level;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public CustomUser getOwner() {
        return owner;
    }

    public void setOwner(CustomUser owner) {
        this.owner = owner;
    }
}