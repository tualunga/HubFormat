package com.zyxo.hubformatapp.base.options;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zyxo.hubformatapp.base.domain.Hbdf;
import com.zyxo.hubformatapp.base.domain.Extent;
import com.zyxo.hubformatapp.base.entity.HbdfEntity;
import com.zyxo.hubformatapp.base.repository.HbdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("hbdf")
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

    public ResponseEntity<String> load(){
        return ResponseEntity.ok("OK");
    }

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            System.out.println("-----\n " + file.getBytes());

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:/uploadStatus";
    }

    @GetMapping("/load")
    public ResponseEntity<Hbdf> createHbdf() {
        Hbdf loadHbdf = xmlToPojo();
        HbdfEntity hbdfEntityToSave = HbdfEntity.builder()
                .name(loadHbdf.getPersonalInformation().getName())
                .date(loadHbdf.getPersonalInformation().getDate())
                .xml(loadHbdf.toString())
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

        return ResponseEntity.ok(xmlToPojo());
    }

    @GetMapping("/calculate")
    public ResponseEntity<Hbdf> calculateHbdf() {
        CalculatesCoordinates calculatesCoordinates = new CalculatesCoordinates();
        calculatesCoordinates.calculateHbdfEntity(xmlToPojo());
        return ResponseEntity.ok(xmlToPojo());
    }

    //@CrossOrigin
    @GetMapping("/get")
    public String getUsers() {
        try {
            return objectMapper.writeValueAsString(hbdfRepository.findAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Hbdf xmlToPojo() {
        try {
            String xml = inputStreamToString(new FileInputStream(
                    ResourceUtils.getFile(
                            "classpath:temp/vstup1.xml")));
            Hbdf createdHbdf = xmlMapper.readValue(xml, Hbdf.class);
            System.out.printf(createdHbdf.toString());
            return createdHbdf;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
