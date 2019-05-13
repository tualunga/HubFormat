package com.zyxo.hubformatapp.base.options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zyxo.hubformatapp.base.domain.Extent;
import com.zyxo.hubformatapp.base.domain.Hbdf;
import com.zyxo.hubformatapp.base.entity.HbdfEntity;
import com.zyxo.hubformatapp.base.repository.HbdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.List;

@Controller
public class LoadHbdf {

    private XmlMapper xmlMapper;
    private HbdfRepository hbdfRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public LoadHbdf(XmlMapper xmlMapper, HbdfRepository hbdfRepository, ObjectMapper objectMapper) {
        this.xmlMapper = xmlMapper;
        this.objectMapper = objectMapper;
        this.hbdfRepository = hbdfRepository;
    }

    public static boolean validateXMLSchema(File fileToValidate) {

        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File("src/main/resources/temp/hbdf_schema.xsd"));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(fileToValidate));
        } catch (IOException | SAXException e) {
            System.out.println("Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    @CrossOrigin
    @RequestMapping(value = "/load", method = RequestMethod.POST)
    public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        Hbdf createdHbdf;
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        if (!validateXMLSchema(convFile)) {
            return new ResponseEntity<>("Nahrate XML nezodpoveda spravnej strukture!", HttpStatus.NOT_ACCEPTABLE);
        }
        String xml;
        try {
            xml = inputStreamToString(new FileInputStream(convFile));
            createdHbdf = xmlMapper.readValue(xml, Hbdf.class);
            System.out.printf(createdHbdf.toString());
            if (createHbdfInDb(createdHbdf, xml)) {
                return new ResponseEntity<>("Nahrate XML je validne a nachadza sa v databaze.", HttpStatus.OK);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>("Nahrate XML ma nevalidne data!", HttpStatus.NOT_IMPLEMENTED);
    }

    public Boolean createHbdfInDb(Hbdf createdHbdf, String xml) {
        try {
            Hbdf loadHbdf = createdHbdf;
            HbdfEntity hbdfEntityToSave = HbdfEntity.builder()
                    .name(loadHbdf.getPersonalInformation().getName())
                    .date(loadHbdf.getPersonalInformation().getDate())
                    .xml(xml)
                    .height(loadHbdf.getPersonalInformation().getHeight())
                    .typeOfDevice(loadHbdf.getTypeOfDevice())
                    .age(loadHbdf.getPersonalInformation().getAge())
                    .weight(loadHbdf.getPersonalInformation().getWeight())
                    .iso_7250(loadHbdf.getTypeOfMeasurement().getIso_7250() != null)
                    .tailoring(loadHbdf.getTypeOfMeasurement().getTailoring() != null)
                    .build();
            hbdfRepository.save(hbdfEntityToSave);
            System.out.printf(loadHbdf.toString());
            List<Extent> torso = loadHbdf.getTypeOfMeasurement().getIso_7250().getTorso().getExtent();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String inputStreamToString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
