package com.example.demo.repo;
import com.example.demo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface Rolerepo extends JpaRepository<Role, Long> {

    @Query(value = "select c.role_name from public.user a join user_role b on a.user_id=b.user_id join role c on b.role_id=c.role_id where a.email =:email", nativeQuery = true)
    String getrole(@Param("email") String email);
}
