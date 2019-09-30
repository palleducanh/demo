package com.example.demo.repo;

import com.example.demo.model.Role;
import com.example.demo.model.Userrole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


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
public interface Userrolerepo extends JpaRepository<Userrole, Long>{

}