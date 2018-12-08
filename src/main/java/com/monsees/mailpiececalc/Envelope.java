/*
This class defines the object for an envelope which is a type of mail component.
It defines how to get the weight and thickness for an envelope.
 */
package com.monsees.mailpiececalc;

public class Envelope extends MailComponent {

    private String paperName;
    private PaperDB paperDB;

    Envelope(String paperName) {

        this.paperName = paperName;

        //Instantiate object of PaperDB class to be able to query the
        paperDB = new PaperDB();

    }

    public double getThickness() {

        //get the thickness of the envelope from the database
        double thickness = paperDB.getEnvelopeCaliper(this.paperName);

        return (thickness);
    }

    public double getWeight() {

        //get the weight of the envelope from the database
        double weight = paperDB.getEnvelopeWeight(this.paperName);

        return (weight);

    }

    public String toString() {

        return (this.paperName);
    }
}
