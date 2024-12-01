package ee.taltech.iti03022024backend;

import com.github.javafaker.Faker;
import ee.taltech.iti03022024backend.entity.Category;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.repository.CategoryRepository;
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
    private final ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(Iti03022024BackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner startup(CategoryRepository categoryRepository) {

        return args -> {
            Faker faker = new Faker();
            for (int i = 0; i < 50; i++) {
                Product product = new Product();
                product.setPrice((double) i);
                product.setTitle(faker.book().title());
                product.setDescription(faker.chuckNorris().fact());
                product.setSoldOut(false);
                productRepository.save(product);
            }
            Product product = new Product();
            product.setTitle("Hoodie");
            product.setPrice(10d);
            product.setSoldOut(false);
            product.setId(51L);
            productRepository.save(product);

            Category category = new Category();
            category.setId(1L);
            category.setName("Clothing");
            categoryRepository.save(category);
            User user = new User();
            user.setUsername("anton");
            user.setEmail("email1");
            user.setPassword("$2a$12$4PdxKUC1NZN9cX4WRB4kP.KrxD98xWAmZ12Koy0zTyVLEifnnhFIO");
            user.setRoles(Set.of(Role.ROLE_USER));
            userRepository.save(user);


        };
    }

}
