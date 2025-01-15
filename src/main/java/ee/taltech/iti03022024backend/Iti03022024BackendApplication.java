package ee.taltech.iti03022024backend;

import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@RequiredArgsConstructor
public class Iti03022024BackendApplication {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public static void main(String[] args) {
        SpringApplication.run(Iti03022024BackendApplication.class, args);
    }

}
