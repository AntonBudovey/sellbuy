package ee.taltech.iti03022024backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {

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

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
