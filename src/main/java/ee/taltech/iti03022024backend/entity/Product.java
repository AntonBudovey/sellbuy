package ee.taltech.iti03022024backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "products_id_seq")
    @SequenceGenerator(name = "products_id_seq",
            sequenceName = "products_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "sold_out", nullable = false)
    private Boolean soldOut;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "product")
    private Set<Review> reviews;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_categories"
            , joinColumns = @JoinColumn(name = "product_id")
            , inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        // Appropriate serialization logic here
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Appropriate deserialization logic here
    }
}
