package com.monsees.mailpiececalc;

/* Technically this booklet component doesn't extend the MailComponent class but it is a
   descendant class of MailComponent. It's is a descendant class because a booklet is a type of MailComponent, 
   although it's really just a combination of a SelfMailer object and an Insert object.
 */
public class Booklet extends MailComponent {

    private double width, height;
    private int numPages;
    private String coverPaperName, textPaperName;
    private SelfMailer cover;
    private Insert insides;

    Booklet(String coverPaperName, String textPaperName, double width, double height, int numPages) {

        this.width = width;
        this.height = height;

        if (coverPaperName.equals("Self Cover")) {
            //if the booklet is a self cover, the cover page is the same as the inside text
            //create a self mailer component of for the cover
            this.cover = new SelfMailer(textPaperName, width, height, 2);
        } else {
            //create a self mailer component of for the cover
            this.cover = new SelfMailer(coverPaperName, width, height, 2);
        }
        int numTextPages = (numPages / 4) - 1;
        //creat an insert mailer component for the text pages
        this.insides = new Insert(textPaperName, width, height, 2, numTextPages);

    }

    public double getThickness() {

        //The thickness of the booklet is the thickness of the cover page + all of the inside text pages
        double thickness = cover.getThickness() + insides.getThickness();

        return (thickness);

    }

    public double getWeight() {

        //the weight is the weight of the cover page + the inside text pages
        double sheetWeightOZ = cover.getWeight() + insides.getWeight();

        return (sheetWeightOZ);

    }

    public String toString() {

        return (this.width + "x" + this.height + " Booklet");

    }

}
