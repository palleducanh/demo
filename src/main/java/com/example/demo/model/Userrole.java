package com.example.demo.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_role", schema = "public")
public class Userrole {
    @Id
    @Column(name = "user_role_id")
    private int userroleid;
    @Column(name = "user_id")
    private int userid;
    @Column(name = "role_id")
    private int roleid;
    @Column(name = "count")
    private int count;
    @Column(name = "status")
    private int status;
}
