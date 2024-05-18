package org.ag.authenticationservice.repositories;

import org.ag.authenticationservice.models.Session;
import org.ag.authenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByTokenAndUser_id(String token, long userId);
}
