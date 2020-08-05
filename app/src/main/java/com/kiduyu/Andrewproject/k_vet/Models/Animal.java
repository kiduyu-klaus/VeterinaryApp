package com.kiduyu.Andrewproject.k_vet.Models;

public class Animal {
    private String name, breed, description,owner ,image;
    public Animal()
    {

    }

    public Animal(String name, String breed, String description, String owner, String image) {
        this.name = name;
        this.breed = breed;
        this.description = description;
        this.owner = owner;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
