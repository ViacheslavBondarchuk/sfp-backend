package com.org.house.sfpbackend.repository.sql;

import com.org.house.sfpbackend.model.sql.User;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(final String username);

    @Query("from User where isEnabled = :enabled")
    Optional<List<User>> findByEnabled(boolean enabled);
}
