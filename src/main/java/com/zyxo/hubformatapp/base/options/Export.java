package com.zyxo.hubformatapp.base.options;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.zyxo.hubformatapp.base.domain.Hbdf;
import com.zyxo.hubformatapp.base.entity.HbdfEntity;
import com.zyxo.hubformatapp.base.repository.HbdfRepository;
import com.zyxo.hubformatapp.base.services.WriteInExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/export")
public class Export {

    private XmlMapper mapper;
    private HbdfRepository repository;
    private ObjectMapper objectMapper;
    private WriteInExcelService service;

    @Autowired
    public Export(XmlMapper xmlMapper, HbdfRepository hbdfRepository,
                  ObjectMapper objectMapper, WriteInExcelService service) {
        this.mapper = xmlMapper;
        this.objectMapper = objectMapper;
        this.repository = hbdfRepository;
        this.service = service;
    }

      @RequestMapping(value = "/HBDF_matrix.xls", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public ResponseEntity<InputStreamResource> downloadMatrix(@RequestParam Integer id)
            throws IOException {
        Hbdf createdHbdf = getHbdf(id);
        service.writeMatrix(createdHbdf);

        Resource resource = new ClassPathResource("data/HBDF_matrix.xls");
        File file = resource.getFile();

        HttpHeaders headers = getHttpHeaders();

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return headers;
    }

    @RequestMapping(value = "/HBDF_coo.xls", method = RequestMethod.GET, produces = "application/vnd.ms-excel")
    public ResponseEntity<InputStreamResource> downloadCoo(@RequestParam Integer id)
            throws IOException {
        Hbdf createdHbdf = getHbdf(id);
        service.writeCoo(createdHbdf);

        Resource resource = new ClassPathResource("data/HBDF_coo.xls");
        File file = resource.getFile();

        HttpHeaders headers = getHttpHeaders();

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new FileInputStream(file)));
    }

    private Hbdf getHbdf(@RequestParam Integer id) {
        Hbdf createdHbdf = null;
        Optional<HbdfEntity> hbdfEntity = repository.findById(id);
        if (hbdfEntity.isPresent()) {
            String xml = hbdfEntity.get().getXml();
            try {
                createdHbdf = mapper.readValue(xml, Hbdf.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return createdHbdf;
    }
}
