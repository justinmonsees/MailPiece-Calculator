/*
This class defines the object for a self mailer which is a type of mail component.
A self mailer is a single piece of paper that is being mailed out. Examples would be a
postcard, bifold card, or a trifolded sheet.
It defines how to get the weight and thickness for a self mailer.
 */
package com.monsees.mailpiececalc;

public class SelfMailer extends SingleSheet {


    private int numPanels;
    private PaperDB paperDB;

    SelfMailer(String paperName,double width,double height,int numPanels){

        super(width,height,paperName);

        this.numPanels = numPanels;

    }

    public double getThickness(){

        //The thickness depends on how many panels there are. By folding a sheet into panels, for each panel the thickness
        // of one sheet is stacked on top of the other. numPanels must be typecast to double to preserve the decimal in thickness
        double thickness = super.getThickness() * (double)this.numPanels;
        //round the thickness in inches to three decimal places
        //thickness = Math.round(thickness*1000)/1000.0;
        System.out.println(thickness);
        return(thickness);

    }

    public double getWeight(){

        double sheetWeightOZ = super.getWeight();

        //round the weight in ounces per sheet to two decimal places
        //sheetWeightOZ = Math.round(sheetWeightOZ*100)/100.0;
        System.out.println(sheetWeightOZ);
        return(sheetWeightOZ);

    }

    public String toString(){

        return(super.getWidth() + "x" + super.getHeight() + " Self Mailer" + ": " + super.getPaperName());
    }

}
