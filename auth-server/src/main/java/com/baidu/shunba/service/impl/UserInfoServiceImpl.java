package com.baidu.shunba.service.impl;

import com.baidu.shunba.dao.UserRepository;
import com.baidu.shunba.entity.User;
import com.baidu.shunba.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameAndAndDeleted(userName, "N");

        if (user == null) {
            throw new UsernameNotFoundException(userName);
        }

        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorityList);
    }
}
