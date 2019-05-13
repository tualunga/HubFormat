package com.zyxo.hubformatapp.base.options;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zyxo.hubformatapp.base.domain.Extent;
import com.zyxo.hubformatapp.base.domain.Hbdf;
import com.zyxo.hubformatapp.base.entity.HbdfEntity;
import com.zyxo.hubformatapp.base.repository.HbdfRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/calculate")
public class CalculatesCoordinates {

    HbdfRepository repository;
    XmlMapper mapper;

    //1
    private Point HEAD = new Point();
    //2
    private Point NECK= new Point();
    //3
    private Point ARM_LEFT= new Point();
    //4
    private Point ARM_RIGHT= new Point();
    //5
    private Point ELBOW_LEFT= new Point();
    //6
    private Point ELBOW_RIGHT= new Point();
    //7
    private Point HAND_LEFT= new Point();
    //8
    private Point HAND_RIGHT= new Point();
    //9
    private Point UMBILICIUS= new Point();
    //10
    private Point CROTCH= new Point();
    //11
    private Point ILIACSPINE_LEFT= new Point();
    //12
    private Point ILIACSPINE_RIGHT= new Point();
    //13
    private Point KNEE_LEFT= new Point();
    //14
    private Point KNEE_RIGHT= new Point();
    //15
    private Point FOOT_LEFT= new Point();
    //16
    private Point FOOT_RIGHT= new Point();
    //17
    private Point GROUND_LEFT= new Point();
    //18
    private Point GROUND_RIGHT= new Point();

    private JSONArray jsonOfPoints = new JSONArray();

    @Autowired
    public CalculatesCoordinates(HbdfRepository repository, XmlMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @CrossOrigin
    @RequestMapping(value = "/id", method = RequestMethod.GET)
    public ResponseEntity<String> calculate(@RequestParam Integer id) {
        jsonOfPoints = new JSONArray();
        Hbdf createdHbdf;
        Optional<HbdfEntity> hbdfEntity = repository.findById(id);
        if(hbdfEntity.isPresent()){
            String xml = hbdfEntity.get().getXml();
            try {
                createdHbdf = mapper.readValue(xml, Hbdf.class);
                calculateHbdfEntity(createdHbdf);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONArray jsInArray = new JSONArray();
        jsInArray.add(jsonOfPoints);
        return new ResponseEntity<>(jsInArray.toJSONString(), HttpStatus.OK);
    }

    private void addToJsonObject(Point point){
        JSONObject jsonPoint = new JSONObject();
        jsonPoint.put("x", point.getX());
        jsonPoint.put("y", point.getY());
        jsonPoint.put("z",0);

        jsonOfPoints.add(jsonPoint);

    }
    public void calculateHbdfEntity(Hbdf hbdf){

        List<Extent> torso = hbdf.getTypeOfMeasurement().getIso_7250().getTorso().getExtent();
        List<Extent> head = hbdf.getTypeOfMeasurement().getIso_7250().getHead().getExtent();
        List<Extent> arm = hbdf.getTypeOfMeasurement().getIso_7250().getArm().getExtent();
        List<Extent> leg = hbdf.getTypeOfMeasurement().getIso_7250().getLeg().getExtent();


        HEAD.setLocation(0,
                Double.parseDouble(getValue(head, "Sellion", "height")));
        addToJsonObject(HEAD);

        NECK.setLocation(0,
                Double.parseDouble(getValue(torso, "Neck", "height")));
        addToJsonObject(NECK);
        CROTCH.setLocation(0,
                Double.parseDouble(getValue(torso, "Crotch", "height")));
        addToJsonObject(CROTCH);

        UMBILICIUS.setLocation(0,
                Double.parseDouble(getValue(torso, "Umbilicus", "height")));
        addToJsonObject(UMBILICIUS);

        ARM_LEFT.setLocation(
                NECK.getX()
                 - (Double.parseDouble(getValue(torso, "Acromion", "breadth"))/2),
                Double.parseDouble(getValue(torso, "Acromion", "height")));
        addToJsonObject(ARM_LEFT);

        ARM_RIGHT.setLocation(
            NECK.getX()
                    + (Double.parseDouble(getValue(torso, "Acromion", "breadth"))/2),
                Double.parseDouble(getValue(torso, "Acromion", "height")));
        addToJsonObject(ARM_RIGHT);

        ELBOW_LEFT.setLocation(
                calculateTangensCoordinates(
                    ARM_LEFT.getX(),
                        (Double.parseDouble(getValue(torso, "Acromion", "height"))
                                - Double.parseDouble(getValue(arm, "Elbow", "height"))),
                    15,
                    "-"
                ),
                Double.parseDouble(getValue(arm, "Elbow", "height")));
        addToJsonObject(ELBOW_LEFT);

        ELBOW_RIGHT.setLocation(
                calculateTangensCoordinates(
                        ARM_RIGHT.getX(),
                        (Double.parseDouble(getValue(torso, "Acromion", "height"))
                                - Double.parseDouble(getValue(arm, "Elbow", "height"))),
                        15,
                        "+"
                ),
                Double.parseDouble(getValue(arm, "Elbow", "height")));
        addToJsonObject(ELBOW_RIGHT);

        HAND_LEFT.setLocation(
                calculateTangensCoordinates(
                    ELBOW_LEFT.getX(),
                        (Double.parseDouble(getValue(arm, "Elbow", "height"))
                        - Double.parseDouble(getValue(arm, "Hand", "height"))),
                    11,
                    "-"),
                Double.parseDouble(getValue(arm, "Hand", "height")));
        addToJsonObject(HAND_LEFT);

        HAND_RIGHT.setLocation(
                calculateTangensCoordinates(
                        ELBOW_RIGHT.getX(),
                        (Double.parseDouble(getValue(arm, "Elbow", "height"))
                                - Double.parseDouble(getValue(arm, "Hand", "height"))),
                        11,
                        "+"),
                Double.parseDouble(getValue(arm, "Hand", "height")));
        addToJsonObject(HAND_RIGHT);

        ILIACSPINE_LEFT.setLocation(
                CROTCH.getX()
                        - (Double.parseDouble(getValue(torso, "Iliac spine", "breadth"))/2),
                Double.parseDouble(getValue(torso, "Iliac spine", "height")));
        addToJsonObject(ILIACSPINE_LEFT);

        ILIACSPINE_RIGHT.setLocation(
                CROTCH.getX()
                        + (Double.parseDouble(getValue(torso, "Iliac spine", "breadth"))/2),
                Double.parseDouble(getValue(torso, "Iliac spine", "height")));
        addToJsonObject(ILIACSPINE_RIGHT);

        KNEE_LEFT.setLocation(
                ILIACSPINE_LEFT.getX()-5,
                Double.parseDouble(getValue(leg, "Knee", "height"))
        );
        addToJsonObject(KNEE_LEFT);

        KNEE_RIGHT.setLocation(
                ILIACSPINE_RIGHT.getX()+5,
                Double.parseDouble(getValue(leg, "Knee", "height"))
        );
        addToJsonObject(KNEE_RIGHT);

        GROUND_LEFT.setLocation(
                ILIACSPINE_LEFT.getX()
                        - (Double.parseDouble(getValue(torso, "Iliac spine", "breadth"))/5),
                0);
        addToJsonObject(GROUND_LEFT);

        GROUND_RIGHT.setLocation(
                ILIACSPINE_RIGHT.getX()
                        + (Double.parseDouble(getValue(torso, "Iliac spine", "breadth"))/6),
                0);
        addToJsonObject(GROUND_RIGHT);

        FOOT_LEFT.setLocation(
                calculateTangensCoordinates(
                        GROUND_LEFT.getX(),
                        0 - Double.parseDouble(getValue(leg, "Foot", "height")),
                        70,
                        "-")+10,
                Double.parseDouble(getValue(leg, "Foot", "height"))
        );
        addToJsonObject(FOOT_LEFT);

        FOOT_RIGHT.setLocation(
                calculateTangensCoordinates(
                        GROUND_RIGHT.getX(),

                        0 - Double.parseDouble(getValue(leg, "Foot", "height")),
                        70,
                        "+")-10,
                Double.parseDouble(getValue(leg, "Foot", "height"))
        );
        addToJsonObject(FOOT_RIGHT);


        saveData(jsonOfPoints);
    }

    private void saveData(JSONArray array) {
            try (FileWriter file = new FileWriter("points.json")) {
            file.write(array.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private double calculateTangensCoordinates(double coordinate, double length, int angle, String mark){
        return mark.equals("+")
                ? (coordinate + Math.tan(Math.toRadians(angle))*length)
                : (coordinate - Math.tan(Math.toRadians(angle))*length);

    }
}
