package com.test.security.repository;

import com.test.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository라는 어노테이션이 없어도 Ioc가 가능하다. 이유는 JpaRepository를 상속했기 떄문에 가능.
// JpaRepository를 상속하면 기본적인 CRUD를 갖고 있기 떄문에 해당 함수를 통해 구현할 수 있다.
public interface UserRepository extends JpaRepository<User, Integer> {

}
