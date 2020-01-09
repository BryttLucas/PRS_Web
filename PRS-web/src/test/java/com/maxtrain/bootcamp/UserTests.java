package com.maxtrain.bootcamp;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.maxtrain.bootcamp.prs.user.User;
import com.maxtrain.bootcamp.prs.user.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTests {
	@Autowired
	UserRepository userRepo;
	
	@Test
	public void testFindAll() {
		Iterable<User> users = userRepo.findAll();
		assertNotNull(users);
		assertTrue(((Collection<User>) users).size()>0);
		
	}
	@Test
	public void testUserAdd() {
		User users = new User("Tuser","pwd","test","user","513-111-2222","test@test.com",true,true);
			assertNotNull(userRepo.save(users));
	}
}
