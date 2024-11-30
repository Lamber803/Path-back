package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.entity.User;
@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// 通過用戶名查找用戶
	Optional<User> findByUsername(String username);
	// 通過電子信箱查找用戶
    Optional<User> findByEmail(String email);
    
	
   
    // 通過電子信箱查找用戶
    //@Query("Select u.user_name From User u where u.email = :email")
    //User findByEmail(String email);
 
	@Query("select u from User u where u.userId = : id")
	List<User> findUserNameByUserId(Integer id);
	
	
}
