package cz.zcu.sar.centraldb.web.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Matej Lochman on 16.11.16.
 */

@Controller
@RequestMapping("/")
public class IndexController implements ErrorController {
    private static final String ERROR_PATH = "error";

    @GetMapping
    public String viewIndex(Model model) {
        model.addAttribute("message", "Hello");
        return "index";
    }

    @RequestMapping(value = ERROR_PATH)
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
