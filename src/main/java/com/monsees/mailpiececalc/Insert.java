/*
This class defines the object for an insert which is a type of single sheet.
An insert is a sheet of paper that can be inserted into an envelope. The only difference
than a single sheet is that an insert can be multiple pages. In order to calculate the proper
weight, we'll need the weight of one sheet of paper and multiply that by the number of sheets.
For the thickness, we'll calculate that by getting the number of panels so we know how many times
the insert is folded, get the thickness of one sheet, and then multiple that by the number of sheets.
 */
package com.monsees.mailpiececalc;

public class Insert extends SingleSheet {

    private double width,height;
    private int numPanels,numPages;
    private String paperName;
    private PaperDB paperDB;

    Insert(String paperName,double width,double height,int numPanels,int numPages){

        super(width,height,paperName);

        this.numPanels = numPanels;
        this.numPages = numPages;

    }

    public double getThickness(){

        //The thickness depends on how many panels there are. By folding a sheet into panels, for each panel the thickness
        // of one sheet is stacked on top of the other. numPanels and numPages must be typecast to double to preserve the decimal in thickness
        double thickness = super.getThickness() * (double)this.numPanels * (double)this.numPages;
        //round the thickness in inches to three decimal places
        //thickness = Math.round(thickness*1000)/1000.0;
        System.out.println(thickness);
        return(thickness);


    }

    public double getWeight(){

        double sheetWeightOZ = super.getWeight() * (double)this.numPages;

        //round the weight in ounces per sheet to two decimal places
        //sheetWeightOZ = Math.round(sheetWeightOZ*100)/100.0;
        System.out.println(sheetWeightOZ);
        return(sheetWeightOZ);

    }

    public String toString(){

        return(super.getWidth() + "x" + super.getHeight() + " Self Mailer" + ": " + super.getPaperName());
    }

}
