package cz.zcu.sar.centraldb.web.controller;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Matej Lochman on 16.11.16.
 */

@Controller
@RequestMapping("/")
public class IndexController implements ErrorController {
    private static final String ERROR_PATH = "error";

    //@Secured({ "ROLE_USER" })
    @GetMapping
    public String viewIndex(Model model) {
        model.addAttribute("message", "Hello");
        return "index";
    }


    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }


    @GetMapping(value = "/person/list")
    public String getList() {
        return "person-list";
    }

    @GetMapping(value = "/p/{id}")
    public String getPerson(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return "person";
    }
    //@Secured({ "ROLE_USER" })
    @RequestMapping(value = ERROR_PATH)
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
