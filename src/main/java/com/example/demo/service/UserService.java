package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.model.User;
import com.example.demo.model.Userrole;
import com.example.demo.repo.Rolerepo;
import com.example.demo.repo.Userrepo;
import com.example.demo.repo.Userrolerepo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    Rolerepo rolerepo;
    @Autowired
    Userrepo userrepo;
    @Autowired
    Userrolerepo userrolerepo;

    public static List<User> listUser = new ArrayList<User>();



    public List<User> findAll() {
        listUser=userrepo.findAll();
        return listUser;
    }

    public User findById(int id) {
        listUser=userrepo.findAll();
        for (User user : listUser) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public boolean add(User user,int role) {
//        for (User userExist : listUser) {
//            if (user.getId() == userExist.getId() || StringUtils.equals(user.getUsername(), userExist.getUsername())) {
//                return false;
//            }
//        }

        userrolerepo.save(Userrole.builder().userid(user.getId()).roleid(role).count(0).build());
        userrepo.save(user);
        return true;
    }

    public void delete(int id) {
        listUser=userrepo.findAll();
        listUser.removeIf(user -> user.getId() == id);
    }

    public User loadUserByUsername(String username) {
        listUser=userrepo.findAll();
        for (User user : listUser) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean checkLogin(User user) {
        listUser=userrepo.findAll();
        for (User userExist : listUser) {
            if (StringUtils.equals(user.getUsername(), userExist.getUsername())
                    && StringUtils.equals(user.getPassword(), userExist.getPassword())) {
                return true;
            }
        }
        return false;
    }

    public List<GrantedAuthority> getAuthorities(User user) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

            authorities.add(new SimpleGrantedAuthority(rolerepo.getrole(user.getUsername())));

        return authorities;
    }
}