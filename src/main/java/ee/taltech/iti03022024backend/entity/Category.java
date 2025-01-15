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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "categories_id_seq")
    @SequenceGenerator(name = "categories_id_seq",
            sequenceName = "categories_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "category_name", nullable = false, unique = true)
    private String name;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "product_categories"
            , joinColumns = @JoinColumn(name = "category_id")
            , inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        // Appropriate serialization logic here
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Appropriate deserialization logic here
    }
}
