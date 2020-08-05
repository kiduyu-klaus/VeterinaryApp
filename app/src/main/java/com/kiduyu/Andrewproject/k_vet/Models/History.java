package com.kiduyu.Andrewproject.k_vet.Models;

public class History {
    String Animal_Name,Remarks,Date;
    public History(){

    }

    public History(String animal_Name, String remarks, String date) {
        Animal_Name = animal_Name;
        Remarks = remarks;
        Date = date;
    }

    public String getAnimal_Name() {
        return Animal_Name;
    }

    public void setAnimal_Name(String animal_Name) {
        Animal_Name = animal_Name;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
