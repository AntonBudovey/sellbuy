package ee.taltech.iti03022024backend.security.expression;

import ee.taltech.iti03022024backend.entity.Product;
import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.service.UserService;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final UserService userService;

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return id.equals(user.getId()) || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    public boolean canAccessProduct(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User userWithProducts = userService.getUserByIdWithProducts(user.getId());
        System.out.println(userWithProducts.getProducts().stream().map(Product::getId).toList());
        return userWithProducts.getProducts().stream().map(Product::getId).toList().contains(productId)
                || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    public boolean canAccessReview(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        User userWithReviews = userService.getUserByIdWithReviews(user.getId());
        return userWithReviews.getReviews().stream().map(Review::getId).toList().contains(reviewId)
                || user.getRoles().contains(Role.ROLE_ADMIN);
    }
}
