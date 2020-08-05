package com.kiduyu.Andrewproject.k_vet.Models;

public class Treatment {
    private String animal_name,animal_owner, vet_name,animal_image,date_treated,vet_remarks;

    public Treatment(){

    }

    public Treatment(String animal_name, String animal_owner, String vet_name, String animal_image, String date_treated, String vet_remarks) {
        this.animal_name = animal_name;
        this.animal_owner = animal_owner;
        this.vet_name = vet_name;
        this.animal_image = animal_image;
        this.date_treated = date_treated;
        this.vet_remarks = vet_remarks;
    }

    public String getAnimal_name() {
        return animal_name;
    }

    public void setAnimal_name(String animal_name) {
        this.animal_name = animal_name;
    }

    public String getAnimal_owner() {
        return animal_owner;
    }

    public void setAnimal_owner(String animal_owner) {
        this.animal_owner = animal_owner;
    }

    public String getVet_name() {
        return vet_name;
    }

    public void setVet_name(String vet_name) {
        this.vet_name = vet_name;
    }

    public String getAnimal_image() {
        return animal_image;
    }

    public void setAnimal_image(String animal_image) {
        this.animal_image = animal_image;
    }

    public String getDate_treated() {
        return date_treated;
    }

    public void setDate_treated(String date_treated) {
        this.date_treated = date_treated;
    }

    public String getVet_remarks() {
        return vet_remarks;
    }

    public void setVet_remarks(String vet_remarks) {
        this.vet_remarks = vet_remarks;
    }
}
