package com.zyxo.hubformatapp.base.options;

import com.zyxo.hubformatapp.base.domain.Extent;
import com.zyxo.hubformatapp.base.domain.Hbdf;

import java.awt.*;
import java.util.List;

public class CalculatesCoordinates {

    private Point HEAD = new Point();
    private Point NECK= new Point();
    private Point ARM_LEFT= new Point();
    private Point ARM_RIGHT= new Point();
    private Point ELBOW_LEFT= new Point();
    private Point ELBOW_RIGHT= new Point();
    private Point HAND_LEFT= new Point();
    private Point HAND_RIGHT= new Point();
    private Point UMBILICIUS= new Point();
    private Point CROTCH= new Point();
    private Point ILIACSPINE_LEFT= new Point();
    private Point ILIACSPINE_RIGHT= new Point();
    private Point KNEE_LEFT_= new Point();
    private Point KNEE_RIGHT= new Point();
    private Point FOOT_LEFT= new Point();
    private Point FOOT_RIGHT= new Point();

//HEAD, NECK, ARM_LEFT, ARM_RIGHT, ELBOW_LEFT, ELBOW_RIGHT, HAND_LEFT, HAND_RIGHT, UMBILICIUS, CROTCH, ILIACSPINE_LEFT, ILIACSPINE_RIGHT,
//KNEE_LEFT,_KNEE_RIGHT, FOOT_LEFT, FOOT_RIGHT,

    private Hbdf hbdfToCalculate;

    public void calculateHbdfEntity(Hbdf hbdf){
        hbdfToCalculate = hbdf;
        List<Extent> torso = hbdf.getTypeOfMeasurement().getIso_7250().getTorso().getExtent();
        List<Extent> head = hbdf.getTypeOfMeasurement().getIso_7250().getHead().getExtent();
        List<Extent> arm = hbdf.getTypeOfMeasurement().getIso_7250().getArm().getExtent();
        List<Extent> leg = hbdf.getTypeOfMeasurement().getIso_7250().getLeg().getExtent();


        HEAD.setLocation(0,
                Double.parseDouble(getValue(torso, "Crotch", "height"))
                + Double.parseDouble(getValue(torso, "TorsoLenght", "lenght"))
                + Double.parseDouble(getValue(torso, "Neck", "height"))
                + Double.parseDouble(getValue(torso, "Sellion", "height")));

        NECK.setLocation(0,
                Double.parseDouble(getValue(torso, "Crotch", "height"))
                + Double.parseDouble(getValue(torso, "TorsoLenght", "lenght"))
                + Double.parseDouble(getValue(torso, "Neck", "height")));
        CROTCH.setLocation(0, Double.parseDouble(getValue(torso, "Crotch", "height")));
        UMBILICIUS.setLocation(0, Double.parseDouble(getValue(torso, "Crotch", "height"))
                + (Double.parseDouble(getValue(torso, "TorsoLenght", "lenght"))/3));

        ARM_LEFT.setLocation(
                NECK.getX()
                 - (Double.parseDouble(getValue(torso, "Acromion", "Breadth"))/2),
                calculateSinusCoordinates(
                    NECK.y,
                    Double.parseDouble(getValue(arm, "hand", "height")),
                    30,
                    "-")
                );
        ARM_RIGHT.setLocation(
            NECK.getX() + (Double.parseDouble(getValue(torso, "Acromion", "Breadth"))/2),
            ARM_LEFT.getY());

        ELBOW_LEFT.setLocation(
                calculateCosinusCoordinates(
                    ARM_LEFT.getX(),
                    Double.parseDouble(getValue(arm, "elbow", "height")),
                    65,
                    "-"
                ),
                calculateSinusCoordinates(
                    ARM_LEFT.getY(),
                    Double.parseDouble(getValue(arm, "elbow", "height")),
                    65,
                    "-"
                )
        );

        ELBOW_RIGHT.setLocation(
                calculateCosinusCoordinates(
                        ARM_RIGHT.getX(),
                        Double.parseDouble(getValue(arm, "elbow", "height")),
                        65,
                        "+"),
                ELBOW_RIGHT.getY());

        HAND_LEFT.setLocation(
                calculateCosinusCoordinates(
                    ELBOW_LEFT.getX(),
                    Double.parseDouble(getValue(arm, "hand", "height")),
                    70,
                    "-"),
                calculateSinusCoordinates(
                        ELBOW_LEFT.getY(),
                        Double.parseDouble(getValue(arm, "hand", "height")),
                        70,
                        "-"
                )
        );
        HAND_RIGHT.setLocation(
                calculateCosinusCoordinates(
                        ELBOW_LEFT.getX(),
                        Double.parseDouble(getValue(arm, "hand", "height")),
                        70,
                        "+"),
                HAND_LEFT.getY()
        );

//        ILIACSPINE_LEFT.setLocation(
//                CROTCH.getX() + Double.parseDouble(getValue(arm, "Iliac spine", "breadth")),
//                calculateSinusCoordinates(
//                        CROTCH.getY(),
//
//                )
//
//
//
//        );




    }

    private String getValue(List<Extent> typeOfBody, String type, String measurement) {
        return typeOfBody.stream().filter(p -> p.getType().equals(type)
                && p.getMeasurement().equals(measurement)).findAny().get().getValue();
    }

    private double calculateSinusCoordinates(double coordinate, double length, int angle, String mark){

        return mark.equals('+')
                ? (coordinate + (Math.sin(Math.toRadians(angle))*length))
                : (coordinate - (Math.sin(Math.toRadians(angle))*length));

    }

    private double calculateCosinusCoordinates(double coordinate, double length, int angle, String mark){
        return mark.equals('+')
                ? (coordinate + Math.cos(Math.toRadians(angle))*length)
                : (coordinate - Math.cos(Math.toRadians(angle))*length);
    }
}
