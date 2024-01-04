package com.ichubtou.excel.user.repository;

import com.ichubtou.excel.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
