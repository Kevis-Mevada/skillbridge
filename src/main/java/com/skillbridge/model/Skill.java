package com.skillbridge.model;

import java.io.Serializable;

/**
 * Skill entity representing industry skills in the master skills table.
 */
public class Skill implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String category;

    public Skill() {}

    public Skill(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Skill(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name;
    }
}
