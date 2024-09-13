package com.ecomm.jun.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@NoArgsConstructor
@Entity
@Data
@Table(name = "authority")
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    private List<User> users;
}
