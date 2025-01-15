package ee.taltech.iti03022024backend.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.taltech.iti03022024backend.AbstractIntegrationTest;
import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.repository.CategoryRepository;
import ee.taltech.iti03022024backend.repository.ProductRepository;
import ee.taltech.iti03022024backend.repository.ReviewRepository;
import ee.taltech.iti03022024backend.repository.UserRepository;
import ee.taltech.iti03022024backend.web.dto.CategoryDto;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtRequest;
import ee.taltech.iti03022024backend.web.dto.jwt.JwtResponse;
import ee.taltech.iti03022024backend.web.dto.pagination.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/sql/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Transactional
class ProductControllerTest extends AbstractIntegrationTest {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AuthController authController;
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper objectMapper;
    private User myUser;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        reviewRepository.deleteAll();
        myUser = new User(1L, "anton", "email", "$2a$10$4rjYv4iMaM2ayhyZD8mYu.xj8xr72fwoCobh.8oeDFhZQcY4iIJ4e", null, null, null);
    }

    @Test
    void testGetProductByIdSuccess() throws Exception {
        Product product = productRepository.save(new Product(null, "Title", "Description", 5.99, false, null, null, null));
        mvc.perform(get("/api/v1/products/" + product.getId()).with(user(myUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));

    }

    @Test
    void testCreateProductSuccess() throws Exception {
        ProductDto productDto = new ProductDto(null, "Title", "Description", 5.99, false, null, null);

        String productJson = objectMapper.writeValueAsString(productDto);

        String response = mvc.perform(post("/api/v1/products")
                        .with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ProductDto product = objectMapper.readValue(response, ProductDto.class);
        mvc.perform(get("/api/v1/products/" + product.getId()).with(user("user")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));

    }

    @Test
    void testUpdateProductSuccess() throws Exception {
        ProductDto productDto = new ProductDto(null, "Title", "Description", 5.99, false, null, null);
        String productJson = objectMapper.writeValueAsString(productDto);
        String response = mvc.perform(post("/api/v1/products").with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ProductDto productResponse = objectMapper.readValue(response, ProductDto.class);
        productResponse.setTitle("updatedTitle");
        mvc.perform(put("/api/v1/products").with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productResponse)))
                .andExpect(status().isOk());

        mvc.perform(get("/api/v1/products/" + productResponse.getId()).with(user(myUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("updatedTitle"));

    }

    @Test
    void testGetAllProductsSuccess() throws Exception {
        ProductDto productDto = new ProductDto(null, "Title", "Description", 5.99, false, null, null);
        String productJson = objectMapper.writeValueAsString(productDto);
        mvc.perform(post("/api/v1/products").with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String response = mvc.perform(get("/api/v1/products").with(user(myUser))
                        .param("minPrice", "1.99"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        PageResponse<ProductDto> products = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructParametricType(PageResponse.class, ProductDto.class));
        assertEquals(1, products.content().size());

        String response2 = mvc.perform(get("/api/v1/products").with(user(myUser))
                        .param("minPrice", "6.99"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        PageResponse<ProductDto> products2 = objectMapper.readValue(response2,
                objectMapper.getTypeFactory().constructParametricType(PageResponse.class, ProductDto.class));
        assertEquals(0, products2.content().size());
    }

    @Test
    void testGetProductsByUserIdSuccess() throws Exception {
        ProductDto productDto = new ProductDto(null, "Title", "Description", 5.99, false, null, null);
        String productJson = objectMapper.writeValueAsString(productDto);
        mvc.perform(post("/api/v1/products").with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String response = mvc.perform(get("/api/v1/products/user/1").with(user(myUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDto> products = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructParametricType(List.class, ProductDto.class));
        assertEquals(1, products.size());
        assertEquals("Title", products.get(0).getTitle());
    }

    @Test
    void testDeleteProductSuccess() throws Exception {
        ProductDto productDto = new ProductDto(null, "Title", "Description", 5.99, false, null, null);
        String productJson = objectMapper.writeValueAsString(productDto);
        String response = mvc.perform(post("/api/v1/products").with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ProductDto dto = objectMapper.readValue(response, ProductDto.class);
        mvc.perform(delete("/api/v1/products/" + dto.getId()).with(user(myUser)))
                .andExpect(status().isNoContent());

    }

    @Test
    void testCreateCategorySuccess() throws Exception {
        CategoryDto dto = new CategoryDto(null, "test");
        mvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(user(myUser)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllCategoriesSuccess() throws Exception {
        CategoryDto dto = new CategoryDto(null, "test");
        mvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(user(myUser)))
                .andExpect(status().isOk());
        String response = mvc.perform(get("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(user(myUser)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        List<CategoryDto> categories = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructParametricType(List.class, CategoryDto.class));
        assertEquals(1, categories.size());
        assertEquals("test", categories.get(0).getName());
    }

    @Test
    void testDeleteCategorySuccess() throws Exception {
        CategoryDto dto = new CategoryDto(null, "test");
        String response = mvc.perform(post("/api/v1/categories").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(user(myUser)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CategoryDto category = objectMapper.readValue(response, CategoryDto.class);
        Long id = category.getId();
        mvc.perform(delete("/api/v1/categories/" + id).with(user(myUser)))
                .andExpect(status().isNoContent());

    }

    @Test
    void testAddProductToCategorySuccess() throws Exception {
        CategoryDto dto = new CategoryDto(null, "test");
        String response = mvc.perform(post("/api/v1/categories").with(user(myUser)).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)).with(user(myUser)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        CategoryDto category = objectMapper.readValue(response, CategoryDto.class);
        Long categoryId = category.getId();
        ProductDto productDto = new ProductDto(null, "test", "desc", 5.99, false, null, new ArrayList<>());
        response = mvc.perform(post("/api/v1/products").with(user(myUser)).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto))).andReturn().getResponse().getContentAsString();
        Long productId = objectMapper.readValue(response, ProductDto.class).getId();
        mvc.perform(post("/api/v1/categories/" + productId + "/" + categoryId).with(user(myUser))).andExpect(status().isOk());

    }


    @Test
    void testCreateReviewSuccess() throws Exception {
        Product product = productRepository.save(new Product(null, "Title", "Description", 5.99, false, null, null, null));
        ReviewDto reviewDto = new ReviewDto(null, 3.0, "test");
        mvc.perform(post("/api/v1/reviews/" + product.getId()).with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateReviewWithProblemData() throws Exception {
        Product product = productRepository.save(new Product(null, "Title", "Description", 5.99, false, null, null, null));
        ReviewDto reviewDto = new ReviewDto(null, 6.0, "test");
        mvc.perform(post("/api/v1/reviews/" + product.getId()).with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateReviewSuccess() throws Exception {
        Product product = productRepository.save(new Product(null, "Title", "Description", 5.99, false, null, null, null));
        ReviewDto reviewDto = new ReviewDto(null, 3.0, "test");
        String response = mvc.perform(post("/api/v1/reviews/" + product.getId()).with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ReviewDto review = objectMapper.readValue(response, ReviewDto.class);
        review.setRating(5.0);
        mvc.perform(put("/api/v1/reviews").with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllReviewsByIdSuccess() throws Exception {
        Product product = productRepository.save(new Product(null, "Title", "Description", 5.99, false, null, null, null));
        ReviewDto reviewDto = new ReviewDto(null, 3.0, "test");
        mvc.perform(post("/api/v1/reviews/" + product.getId()).with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk());
        String response = mvc.perform(get("/api/v1/reviews/" + product.getId()).with(user(myUser)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ReviewDto> reviews = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructParametricType(List.class, ReviewDto.class));
        assertEquals(1, reviews.size());
        assertEquals(reviewDto.getText(), reviews.get(0).getText());
    }

    @Test
    void testDeleteReviewSuccess() throws Exception {
        String response2 = mvc.perform(post("/api/v1/products").with(user(myUser)).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductDto(null, "Title", "Description", 5.99, false, null, null))))
                .andReturn().getResponse().getContentAsString();
        ProductDto product = objectMapper.readValue(response2, ProductDto.class);
        ReviewDto reviewDto = new ReviewDto(null, 3.0, "test");
        String response = mvc.perform(post("/api/v1/reviews/" + product.getId()).with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ReviewDto review = objectMapper.readValue(response, ReviewDto.class);
        mvc.perform(delete("/api/v1/reviews/" + review.getId()).with(user(myUser))).andExpect(status().isNoContent());

    }

    @Test
    void testDeleteReviewThatNotExists() throws Exception {
        mvc.perform(delete("/api/v1/reviews/1").with(user(myUser))).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteReviewWithoutAuthorization() throws Exception {
        String response2 = mvc.perform(post("/api/v1/products").with(user(myUser)).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ProductDto(null, "Title", "Description", 5.99, false, null, null))))
                .andReturn().getResponse().getContentAsString();
        ProductDto product = objectMapper.readValue(response2, ProductDto.class);
        ReviewDto reviewDto = new ReviewDto(null, 3.0, "test");
        String response = mvc.perform(post("/api/v1/reviews/" + product.getId()).with(user(myUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ReviewDto review = objectMapper.readValue(response, ReviewDto.class);
        mvc.perform(delete("/api/v1/reviews/" + review.getId())).andExpect(status().isUnauthorized());
    }

    @Test
    void testRegisterSuccess() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@mail.com", "1", null);
        String userJson = objectMapper.writeValueAsString(userDto);
        String result = userJson.substring(0, userJson.length() - 1) + ",\"password\": \"1\"}";
        String response = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(result))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        UserDto user = objectMapper.readValue(response, UserDto.class);
        JwtRequest request = new JwtRequest("test", "1");
        response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();
        mvc.perform(delete("/api/v1/users/" + user.getId()).header("Authorization", "Bearer " + jwt)).andExpect(status().isNoContent());
    }

    @Test
    void testLoginSuccess() throws Exception {
        JwtRequest request = new JwtRequest("anton", "1");
        mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void testRefreshTokenSuccess() throws Exception {
        JwtRequest request = new JwtRequest("anton", "1");
        String response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();
        mvc.perform(post("/api/v1/auth/refresh").contentType(MediaType.TEXT_PLAIN).content(jwt)).andExpect(status().isOk());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        JwtRequest request = new JwtRequest("anton", "1");
        String response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();
        mvc.perform(post("/api/v1/auth/logout").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk());
    }

    @Test
    void testRetrieveUserSuccess() throws Exception {
        JwtRequest request = new JwtRequest("anton", "1");
        String response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();
        response = mvc.perform(get("/api/v1/auth").header("Authorization", "Bearer " + jwt)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        UserDto user = objectMapper.readValue(response, UserDto.class);
        assertEquals("anton", user.getUsername());
        assertEquals("email", user.getEmail());
    }


    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@mail.com", "1", null);
        String userJson = objectMapper.writeValueAsString(userDto);
        String result = userJson.substring(0, userJson.length() - 1) + ",\"password\": \"1\"}";
        String response = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(result))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        UserDto user = objectMapper.readValue(response, UserDto.class);
        JwtRequest request = new JwtRequest("test", "1");
        response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();
        user.setEmail("email@mail.com");
        userJson = objectMapper.writeValueAsString(user);
        result = userJson.substring(0, userJson.length() - 1) + ",\"password\": \"1\"}";
        response = mvc.perform(put("/api/v1/users").header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON).content(result))
                .andReturn().getResponse().getContentAsString();
        assertEquals("email@mail.com", objectMapper.readValue(response, UserDto.class).getEmail());
        assertEquals("test", objectMapper.readValue(response, UserDto.class).getUsername());
        mvc.perform(delete("/api/v1/users/" + user.getId()).header("Authorization", "Bearer " + jwt)).andExpect(status().isNoContent());
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@mail.com", "1", null);
        String userJson = objectMapper.writeValueAsString(userDto);
        String result = userJson.substring(0, userJson.length() - 1) + ",\"password\": \"1\"}";
        String response = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(result))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        UserDto user = objectMapper.readValue(response, UserDto.class);

        JwtRequest request = new JwtRequest("test", "1");
        response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();

        response = mvc.perform(get("/api/v1/users/" + user.getId()).header("Authorization", "Bearer " + jwt)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        assertEquals("test", objectMapper.readValue(response, UserDto.class).getUsername());
        assertEquals("test@mail.com", objectMapper.readValue(response, UserDto.class).getEmail());

        mvc.perform(delete("/api/v1/users/" + user.getId()).header("Authorization", "Bearer " + jwt)).andExpect(status().isNoContent());
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@mail.com", "1", null);
        String userJson = objectMapper.writeValueAsString(userDto);
        String result = userJson.substring(0, userJson.length() - 1) + ",\"password\": \"1\"}";
        String response = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(result))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        UserDto user = objectMapper.readValue(response, UserDto.class);

        JwtRequest request = new JwtRequest("test", "1");
        response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();

        mvc.perform(delete("/api/v1/users/" + user.getId()).header("Authorization", "Bearer " + jwt)).andExpect(status().isNoContent());
        mvc.perform(get("/api/v1/users/" + user.getId()).with(user(myUser))).andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUserForbidden() throws Exception {
        UserDto userDto = new UserDto(null, "test", "test@mail.com", "1", null);
        String userJson = objectMapper.writeValueAsString(userDto);
        String result = userJson.substring(0, userJson.length() - 1) + ",\"password\": \"1\"}";
        String response = mvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(result))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        UserDto user = objectMapper.readValue(response, UserDto.class);
        JwtRequest request = new JwtRequest("anton", "1");
        response = mvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        String jwt = objectMapper.readValue(response, JwtResponse.class).getRefreshToken();
        mvc.perform(delete("/api/v1/users/" + user.getId()).header("Authorization", "Bearer " + jwt)).andExpect(status().isForbidden());
    }
}