/*
This class defines the object for a single sheet which is a type of mail component. Because
a self mailer and an insert both need to calculate the thickness and weight of a single sheet, 
both of those classes extend this SingleSheet class. Since this class is a descendant of MailComponent,
any class that extends this class is also a descendant of the MailComponent class
It defines how to get the weight and thickness for a single sheet.
 */
package com.monsees.mailpiececalc;

public class SingleSheet extends MailComponent {

    private double width,height;
    private String paperName;
    private PaperDB paperDB;

    SingleSheet(){

    }

    SingleSheet(double width,double height,String paperName){

        this.width = width;
        this.height = height;
        this.paperName = paperName;

        //Instantiate object of PaperDB class to be able to query the database
        paperDB = new PaperDB();

    }

    public double getThickness(){

        //Get the thickness of one piece of paper
        double thickness = paperDB.getPaperCaliper(this.paperName);
        //round the thickness in inches to three decimal places
        //thickness = Math.round(thickness*1000)/1000.0;
        System.out.println(thickness);
        return(thickness);

    }
    public double getWeight(){

        //Calculate the weight of one piece of paper by first calculating how many
        //square inches in one sheet. Then convert that number to meters. Since every paper has
        // a gsm of (grams per square meter) we can calculate the weight of one sheet of paper in grams
        //by multiple how many square meters in one sheet by the weight of one square gram. To get the weight in ounces,
        //the weight of one sheet in grams is converted to ounces
        double sqInches = this.width * this.height;
        double sqMeters = sqInches / 1550;
        double gsmPerSheet =  sqMeters * paperDB.getPaperGSM(this.paperName);
        double sheetWeightOZ = gsmPerSheet * .03527396195;

        //round the weight in ounces per sheet to two decimal places
        //sheetWeightOZ = Math.round(sheetWeightOZ*100)/100.0;
        System.out.println(sheetWeightOZ);
        return(sheetWeightOZ);

    }
    public double getWidth(){
        return(this.width);
    }
    public double getHeight(){
        return(this.height);
    }
    public String getPaperName(){
        return(this.paperName);
    }

    public String toString(){

        return(this.width + "x" + this.height + " Single Sheet" + ": " + this.paperName);
    }

}