package ee.taltech.iti03022024backend.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "categories_id_seq")
    @SequenceGenerator(name = "categories_id_seq",
            sequenceName = "categories_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(name = "product_categories"
            , joinColumns = @JoinColumn(name = "category_id")
            , inverseJoinColumns = @JoinColumn(name = "products_id")
    )
    private Set<Product> products;
}
