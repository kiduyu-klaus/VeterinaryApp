package com.kiduyu.Andrewproject.k_vet.Models;

public class Appointments {

    String Animal_name,Animal_owner,Animal_owner_phone,Animal_description,Appiontment_date,Appointment_Address,user_image,animal_image,status;

    public Appointments(){

    }

    public Appointments(String animal_name, String animal_owner, String animal_owner_phone, String animal_description, String appiontment_date, String appointment_Address, String user_image, String animal_image, String status) {
        Animal_name = animal_name;
        Animal_owner = animal_owner;
        Animal_owner_phone = animal_owner_phone;
        Animal_description = animal_description;
        Appiontment_date = appiontment_date;
        Appointment_Address = appointment_Address;
        this.user_image = user_image;
        this.animal_image = animal_image;
        this.status = status;
    }


    public String getAnimal_name() {
        return Animal_name;
    }

    public void setAnimal_name(String animal_name) {
        Animal_name = animal_name;
    }

    public String getAnimal_owner() {
        return Animal_owner;
    }

    public void setAnimal_owner(String animal_owner) {
        Animal_owner = animal_owner;
    }

    public String getAnimal_owner_phone() {
        return Animal_owner_phone;
    }

    public void setAnimal_owner_phone(String animal_owner_phone) {
        Animal_owner_phone = animal_owner_phone;
    }

    public String getAnimal_description() {
        return Animal_description;
    }

    public void setAnimal_description(String animal_description) {
        Animal_description = animal_description;
    }

    public String getAppiontment_date() {
        return Appiontment_date;
    }

    public void setAppiontment_date(String appiontment_date) {
        Appiontment_date = appiontment_date;
    }

    public String getAppointment_Address() {
        return Appointment_Address;
    }

    public void setAppointment_Address(String appointment_Address) {
        Appointment_Address = appointment_Address;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getAnimal_image() {
        return animal_image;
    }

    public void setAnimal_image(String animal_image) {
        this.animal_image = animal_image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

