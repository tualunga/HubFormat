package com.zyxo.hubformatapp.base.options;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Visualisation {

        @RequestMapping("/visual")
        public String getHomepage() {
            return "index.html";
        }
}
