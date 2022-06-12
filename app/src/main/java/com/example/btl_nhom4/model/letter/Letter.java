package com.example.btl_nhom4.model.letter;

import com.example.btl_nhom4.types.TypeOfLetter;

public class Letter {
    private TypeOfLetter typeOfLetter;
    private String timeOfLetter;
    private String reasonResignation;

    public Letter(TypeOfLetter typeOfLetter, String timeOfLetter, String reasonResignation) {
        this.typeOfLetter = typeOfLetter;
        this.timeOfLetter = timeOfLetter;
        this.reasonResignation = reasonResignation;
    }

    public TypeOfLetter getTypeOfLetter() {
        return typeOfLetter;
    }

    public void setTypeOfLetter(TypeOfLetter typeOfLetter) {
        this.typeOfLetter = typeOfLetter;
    }

    public String getTimeOfLetter() {
        return timeOfLetter;
    }

    public void setTimeOfLetter(String timeOfLetter) {
        this.timeOfLetter = timeOfLetter;
    }

    public String getReasonResignation() {
        return reasonResignation;
    }

    public void setReasonResignation(String reasonResignation) {
        this.reasonResignation = reasonResignation;
    }
}
