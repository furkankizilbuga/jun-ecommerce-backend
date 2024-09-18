package com.ecomm.jun.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
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
    @Size(max = 50)
    @NotBlank(message = "Product name cannot be empty!")
    @NotNull(message = "Product name must be valid!")
    private String name;

    @Column(name = "image_path")
    @Size(max = 300)
    private String imagePath;

    @Column(name = "rating")
    @Max(5)
    @Min(0)
    private Double rating;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Inventory inventory;

    //@OneToMany(cascade = CascadeType.ALL)
    //private Set<Comment> comments;

    @ManyToMany(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonBackReference
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        if (category != null) {
            categories.add(category);
            category.getProducts().add(this);
        }
    }

}
