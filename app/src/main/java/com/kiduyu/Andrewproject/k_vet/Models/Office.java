package com.kiduyu.Andrewproject.k_vet.Models;

public class Office {
    private String office_name, office_owner,office_number, office_description, office_location, image;

    public Office()
    {

    }

    public Office(String office_name, String office_owner, String office_number, String office_description, String office_location, String image) {
        this.office_name = office_name;
        this.office_owner = office_owner;
        this.office_number = office_number;
        this.office_description = office_description;
        this.office_location = office_location;
        this.image = image;
    }

    public String getOffice_name() {
        return office_name;
    }

    public void setOffice_name(String office_name) {
        this.office_name = office_name;
    }

    public String getOffice_owner() {
        return office_owner;
    }

    public void setOffice_owner(String office_owner) {
        this.office_owner = office_owner;
    }

    public String getOffice_number() {
        return office_number;
    }

    public void setOffice_number(String office_number) {
        this.office_number = office_number;
    }

    public String getOffice_description() {
        return office_description;
    }

    public void setOffice_description(String office_description) {
        this.office_description = office_description;
    }

    public String getOffice_location() {
        return office_location;
    }

    public void setOffice_location(String office_location) {
        this.office_location = office_location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
