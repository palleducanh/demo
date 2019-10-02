package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

import com.example.demo.model.User;
import com.example.demo.model.Userfull;
import com.example.demo.model.Userrole;
import com.example.demo.repo.Rolerepo;
import com.example.demo.repo.Userrolerepo;
import com.example.demo.service.EmailService;
import com.example.demo.service.JwtService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/rest")
public class UserRestController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    EmailService emailService;
    @Autowired
    Rolerepo rolerepo;
    @Autowired
    Userrolerepo userrolerepo;

    /* ---------------- GET ALL USER ------------------------ */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllUser() {
        return new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.OK);
    }

    /* ---------------- GET USER BY ID ------------------------ */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        User user = userService.findById(id);
        if (user != null) {
            return new ResponseEntity<Object>(user, HttpStatus.OK);
        }
        return new ResponseEntity<Object>("Not Found User", HttpStatus.NO_CONTENT);
    }

    /* ---------------- CREATE NEW USER ------------------------ */
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<String> createUser(@RequestBody User user, @RequestParam("role") int role) {
        if (userService.add(user, role)) {
            return new ResponseEntity<String>("Created!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("User Existed!", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/createRole", method = RequestMethod.POST)
    public ResponseEntity<String> createRole(@RequestBody User user, @RequestParam("role") int role) {
        userrolerepo.save(Userrole.builder().userid(user.getId()).roleid(role).count(0).build());
        return new ResponseEntity<String>("createRole", HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        userService.delete(id);
        return new ResponseEntity<String>("Deleted!", HttpStatus.OK);
    }

    @RequestMapping(value = "/usersfull", method = RequestMethod.GET)
    public ResponseEntity<List<Userfull>> findalluserandrole() {
        List<Userfull> users = new ArrayList<>();
        for (User user : userService.findAll()) {
            users.add(Userfull.builder().user(user).Role(rolerepo.getRole(user.getUsername())).build());
        }
        ;

        return new ResponseEntity<List<Userfull>>(users, HttpStatus.OK);
    }

    @RequestMapping(value = "/usersfull", method = RequestMethod.POST)
    public ResponseEntity<String> updateRole(@RequestBody Userfull userfull) {
        int id = rolerepo.findByRolename(userfull.getRole()).getRoleid();
        Optional<Userrole> userrole = userrolerepo.findById((long) id);
        rolerepo.setRole(userfull.getUser().getId());
        if (userrole.isPresent()) {
            userrole.get().setStatus(1);
        } else
            userrolerepo.save(Userrole.builder().userroleid(userfull.getUser().getId() + id).count(0).status(1).roleid(id).userid(userfull.getUser().getId()).build());

        return new ResponseEntity<String>("ok", HttpStatus.OK);
    }

    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public ResponseEntity<String> sendmail(@RequestBody List<User> user) {
        emailService.sendEmailToAll((List<User>) user, "");
        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody User user) {
        String result = "";
        HttpStatus httpStatus = null;
        try {
            if (userService.checkLogin(user)) {
                result = jwtService.generateTokenLogin(user.getUsername());
                httpStatus = HttpStatus.OK;
            } else {
                result = "Wrong userId and password";
                httpStatus = HttpStatus.BAD_REQUEST;
            }
        } catch (Exception ex) {
            result = "Server Error";
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<String>(result, httpStatus);
    }
}