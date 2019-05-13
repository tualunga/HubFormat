package com.zyxo.hubformatapp.base.options;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zyxo.hubformatapp.base.repository.HbdfRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("archive")
public class Archive {

    private XmlMapper xmlMapper;
    private HbdfRepository hbdfRepository;
    private ObjectMapper objectMapper;

    @Autowired
    public Archive(XmlMapper xmlMapper, HbdfRepository hbdfRepository, ObjectMapper objectMapper) {
        this.xmlMapper = xmlMapper;
        this.hbdfRepository = hbdfRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/get")
    public String getUsers() {
        try {
            return objectMapper.writeValueAsString(hbdfRepository.findAll());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return "V databaze sa nenachadzaju ziadne zaznamy.";
    }


}
