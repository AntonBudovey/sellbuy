package ee.taltech.iti03022024backend;

import com.github.javafaker.Faker;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.UserRepository;
import ee.taltech.iti03022024backend.service.ProductService;
import ee.taltech.iti03022024backend.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Set;

@SpringBootApplication
@EnableTransactionManagement
@RequiredArgsConstructor
public class Iti03022024BackendApplication {
    private final UserRepository userRepository;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final ReviewService reviewService;

    public static void main(String[] args) {
        SpringApplication.run(Iti03022024BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup() {

        return args -> {
//            Faker faker = new Faker();
//            for (int i = 0; i < 50; i++) {
//                Product product = new Product();
//                product.setPrice((double) i);
//                product.setTitle(faker.book().title());
//                product.setDescription(faker.chuckNorris().fact());
//                product.setSoldOut(false);
//                productRepository.save(product);
//            }
//            if (userRepository.findByUsername("nikita").isEmpty()) {
//
//                User user = new User();
//                user.setUsername("anton");
//                user.setEmail("email1");
//                user.setPassword("$2a$12$4PdxKUC1NZN9cX4WRB4kP.KrxD98xWAmZ12Koy0zTyVLEifnnhFIO");
//                user.setRoles(Set.of(Role.ROLE_ADMIN));
//                userRepository.save(user);
//
//                User user2 = new User();
//                user2.setUsername("nikita");
//                user2.setPassword("$2a$12$4PdxKUC1NZN9cX4WRB4kP.KrxD98xWAmZ12Koy0zTyVLEifnnhFIO");
//                user2.setEmail("email2");
//                user2.setRoles(Set.of(Role.ROLE_USER));
//                userRepository.save(user2);
//
//                Product product = new Product();
//                product.setTitle("test product");
//                product.setSoldOut(false);
//                product.setPrice(10.0);
//
//                productService.createProduct(product, 1L);
//
//                Product product2 = new Product();
//                product2.setTitle("test product2");
//                product2.setSoldOut(false);
//                product2.setPrice(11.0);
//
//                productService.createProduct(product2, 1L);
//
//                Product product3 = new Product();
//                product3.setTitle("test product3");
//                product3.setSoldOut(false);
//                product3.setPrice(12.0);
//
//                productService.createProduct(product3, 2L);
//
//                Review review = new Review();
//                review.setRating(4.0);
//                review.setText("test review");
//
//                reviewService.createReview(review, 1L, 1L);
//
//                Review review2 = new Review();
//                review2.setRating(5.0);
//                review2.setText("test review2");
//
//                reviewService.createReview(review2, 1L, 1L);
//
//                Review review3 = new Review();
//                review3.setRating(3.0);
//                review3.setText("test review3");
//
//                reviewService.createReview(review3, 2L, 2L);
//
//                Review review4 = new Review();
//                review4.setRating(2.0);
//                review4.setText("test review4");
//
//                reviewService.createReview(review4, 3L, 2L);
//            }
//






        };
    }

}
