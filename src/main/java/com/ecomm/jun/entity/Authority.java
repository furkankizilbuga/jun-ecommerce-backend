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

    @Enumerated(EnumType.STRING)
    private Role authority;

    @Override
    public String getAuthority() {
        return authority.name();
    }

    //@ManyToMany(mappedBy = "authorities")
    //private List<User> users;
}
