package com.desouza.dscommerce.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.desouza.dscommerce.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {

    @Query(nativeQuery = true, value = """
            SELECT tb_pr.*
            FROM tb_password_recover AS tb_pr
            WHERE tb_pr.token = :token
            AND tb_pr.expiration > :now
            """)
    List<PasswordRecover> searchValidTokens(String token, Instant now);

}
