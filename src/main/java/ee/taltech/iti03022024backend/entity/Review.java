package ee.taltech.iti03022024backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
@Table(name = "reviews")
public class Review implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "reviews_id_seq")
    @SequenceGenerator(name = "reviews_id_seq",
            sequenceName = "reviews_id_seq",
            allocationSize = 1)
    private Long id;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "text", nullable = true)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        // Appropriate serialization logic here
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        // Appropriate deserialization logic here
    }

}
