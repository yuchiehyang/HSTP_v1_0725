package com;

public class Teacher {
    protected int id;
    protected String name;
    protected String preferredTimeSlots;

    public Teacher(){
    }


    public Teacher(int id) {
        this.id = id;
    }

    public Teacher(int id, String name, String preferredTimeSlots) {
        this.name = name;
        this.preferredTimeSlots = preferredTimeSlots;
        this.id = id;
    }

    public Teacher(String name, String preferredTimeSlots) {
        this.name = name;
        this.preferredTimeSlots = preferredTimeSlots;
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



    public String getPreferredTimeSlots() {
        return preferredTimeSlots;
    }

    public void setPreferredTimeSlots(String preferredTimeSlots) {
        this.preferredTimeSlots = preferredTimeSlots;
    }
}
