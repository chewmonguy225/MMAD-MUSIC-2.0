package com.MMAD.MMAD.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.MMAD.MMAD.model.User;

public interface UserRepo extends JpaRepository<User, Long> {

}
