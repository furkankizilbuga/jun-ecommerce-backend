package com.ecomm.jun.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "rating")
    private Double rating;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Stock stock;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonManagedReference
    private Set<Category> categories;

}
