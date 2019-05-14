package com.zyxo.hubformatapp.base.options;

import com.zyxo.hubformatapp.base.entity.HbdfEntity;
import com.zyxo.hubformatapp.base.repository.HbdfRepository;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("visualisation")
public class Visualisation {

    private HbdfRepository hbdfRepository;

    @Autowired
    public Visualisation(HbdfRepository hbdfRepository) {
        this.hbdfRepository = hbdfRepository;
    }

    @CrossOrigin
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public ResponseEntity<String> getvisualisationInfo(@RequestParam Integer id) {
        Optional<HbdfEntity> hbdfEntity = hbdfRepository.findById(id);
        JSONObject jsonInfo = new JSONObject();

        if(hbdfEntity.isPresent()) {
            jsonInfo.put("id", hbdfEntity.get().getId());
            jsonInfo.put("name", hbdfEntity.get().getName());
            jsonInfo.put("age", hbdfEntity.get().getAge());
            jsonInfo.put("height", hbdfEntity.get().getHeight());
            jsonInfo.put("weight", hbdfEntity.get().getWeight());
            jsonInfo.put("type", hbdfEntity.get().getTypeOfDevice());

            return new ResponseEntity<>(jsonInfo.toJSONString(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Nothing", HttpStatus.NOT_FOUND);
    }

}
