package cz.zcu.sar.centraldb.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Matej Lochman on 16.11.16.
 */

@Controller
@RequestMapping("/")
public class IndexController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SecurityContextRepository repository;

    @PostMapping(value = "sec/login")
    @ResponseBody
    public String performLogin(@RequestParam String username, @RequestParam String password,
                               HttpServletRequest request, HttpServletResponse response) {
        System.out.println(username + password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            repository.saveContext(SecurityContextHolder.getContext(), request, response);
            if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "{\"status\": true, \"isAdmin\": true}";
            } else {
                return "{\"status\": true, \"isAdmin\": false}";
            }
        } catch (BadCredentialsException ex) {
            return "{\"status\": false, \"error\": \"Neplatné jméno nebo heslo.\"}";
        }
    }

    @Secured({ "ROLE_USER" })
    @PostMapping(value = "sec/logout")
    @ResponseBody
    public String performLogout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "{\"status\": true}";
    }

    //@Secured({ "ROLE_USER" })
    @GetMapping
    public String getIndex() {
        return "index";
    }


    @GetMapping(value = "login")
    public String getLogin() {
        return "login";
    }

    @Secured({ "ROLE_USER" })
    @GetMapping(value = "person-list")
    public String getList() {
        return "person-list";
    }

    @Secured({ "ROLE_USER" })
    @GetMapping(value = "home")
    public String getHome() {
        return "home";
    }

    @Secured({ "ROLE_USER" })
    @GetMapping(value = "person")
    public String getPerson() {
        return "person";
    }

    @Secured({ "ROLE_ADMIN" })
    @GetMapping(value = "person-edit")
    public String getPersonEdit() {
        return "person-edit";
    }

    @Secured({ "ROLE_USER" })
    @GetMapping(value = "address")
    public String getAddress() {
        return "address";    }

    @Secured({ "ROLE_ADMIN" })
    @GetMapping(value = "address-edit")
    public String getAddressEdit() {
        return "address-edit";
    }
}
