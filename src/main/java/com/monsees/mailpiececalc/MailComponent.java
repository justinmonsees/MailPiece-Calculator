/*
This is simply an abstract class to enforce that any descendant of a Mail Component 
is able to calculate the thickness and weight for that type of mail component.
 */
package com.monsees.mailpiececalc;

public abstract class MailComponent {


    public abstract double getThickness();
    public abstract double getWeight();

}
