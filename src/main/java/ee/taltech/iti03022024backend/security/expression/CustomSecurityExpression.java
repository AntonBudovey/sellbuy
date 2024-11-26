package ee.taltech.iti03022024backend.security.expression;

import ee.taltech.iti03022024backend.entity.User;
import ee.taltech.iti03022024backend.entity._enum.Role;
import ee.taltech.iti03022024backend.service.ProductService;
import ee.taltech.iti03022024backend.service.ReviewService;
import ee.taltech.iti03022024backend.web.dto.ProductDto;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final ProductService productService;
    private final ReviewService reviewService;

    public boolean canAccessUser(Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return id.equals(user.getId()) || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    public boolean canAccessProduct(Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<ProductDto> productDtos = productService.getProductsByUserId(user.getId());
        return productDtos.stream().map(ProductDto::getId).toList().contains(productId)
                || user.getRoles().contains(Role.ROLE_ADMIN);
    }

    public boolean canAccessReview(Long reviewId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<ReviewDto> reviewDtos = reviewService.getAllReviewsByProductId(user.getId());
        return reviewDtos.stream().map(ReviewDto::getId).toList().contains(reviewId)
                || user.getRoles().contains(Role.ROLE_ADMIN);
    }
}
