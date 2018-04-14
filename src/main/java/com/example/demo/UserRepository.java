package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{
	
	//jpa 문법이다 By는 무엇으로 찾겠다 이고 Id가 붙었으니 id로 찾겠다는 것이다.
	User findById(String id);
	User findByName(String name);
	// select * from users where id = ?
}
