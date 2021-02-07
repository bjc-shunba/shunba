package com.baidu.shunba.dao;

import com.baidu.shunba.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUserNameAndAndDeleted (String userName, String deleted);
}
