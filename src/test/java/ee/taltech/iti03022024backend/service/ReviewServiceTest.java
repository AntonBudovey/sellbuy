package ee.taltech.iti03022024backend.service;

import ee.taltech.iti03022024backend.entity.Review;
import ee.taltech.iti03022024backend.exception.ResourceNotFoundException;
import ee.taltech.iti03022024backend.repository.ReviewRepository;
import ee.taltech.iti03022024backend.web.dto.ReviewDto;
import ee.taltech.iti03022024backend.web.mapper.ReviewMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private ReviewMapper reviewMapper;
    @Mock
    private ProductService productService;
    private Review review;
    private Review savedReview;
    private ReviewDto reviewDto;
    private ReviewDto savedReviewDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.review = new Review(null, 5.0, "test", null, null);
        this.savedReview = new Review(1L, 5.0, "test", null, null);
        this.reviewDto = new ReviewDto(null, 5.0, "test");
        this.savedReviewDto = new ReviewDto(1L, 5.0, "test");
    }

    @Test
    void testCreateReviewSuccess() {
        Mockito.when(reviewMapper.toDto(savedReview)).thenReturn(savedReviewDto);
        Mockito.when(reviewRepository.save(review)).thenReturn(savedReview);
        Mockito.when(reviewMapper.toEntity(reviewDto)).thenReturn(review);
        ReviewDto result = reviewService.createReview(reviewDto, 1L, 1L);
        assertEquals(savedReviewDto, result);
    }

    @Test
    void testUpdateReviewSuccess() {
        Mockito.when(reviewMapper.toEntity(savedReviewDto)).thenReturn(savedReview);
        Mockito.when(reviewRepository.findById(1L)).thenReturn(Optional.of(savedReview));
        Mockito.when(reviewRepository.save(savedReview)).thenReturn(savedReview);
        Mockito.when(reviewMapper.toDto(savedReview)).thenReturn(savedReviewDto);
        ReviewDto result = reviewService.updateReview(savedReviewDto);
        assertEquals(savedReviewDto, result);
    }
    @Test
    void testUpdateReviewThatDoesNotExist() {
        Mockito.when(reviewMapper.toEntity(savedReviewDto)).thenReturn(savedReview);
        Mockito.when(reviewRepository.findById(1L)).thenReturn(Optional.empty());
        Mockito.when(reviewRepository.save(savedReview)).thenReturn(savedReview);
        Mockito.when(reviewMapper.toDto(savedReview)).thenReturn(savedReviewDto);
        assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReview(savedReviewDto));
    }

    @Test
    void testGetAllReviewsByProductIdSuccess() {
        List<Review> reviews = List.of(savedReview);
        List<ReviewDto> reviewDtos = List.of(savedReviewDto);
        Mockito.when(reviewMapper.toDto(reviews)).thenReturn(reviewDtos);
        Mockito.when(productService.existsById(1L)).thenReturn(true);
        Mockito.when(reviewRepository.findAllByProductId(1L)).thenReturn(reviews);
        List<ReviewDto> result = reviewService.getAllReviewsByProductId(1L);
        assertEquals(reviewDtos, result);
    }
    @Test
    void testGetAllReviewsByProductIdWhenProductNotExist() {
        List<Review> reviews = List.of(savedReview);
        List<ReviewDto> reviewDtos = List.of(savedReviewDto);
        Mockito.when(reviewMapper.toDto(reviews)).thenReturn(reviewDtos);
        Mockito.when(productService.existsById(1L)).thenReturn(false);
        Mockito.when(reviewRepository.findAllByProductId(1L)).thenReturn(reviews);
        assertThrows(ResourceNotFoundException.class, () -> reviewService.getAllReviewsByProductId(1L));
    }


    @Test
    void testDeleteReviewSuccess() {
        Mockito.when(reviewRepository.existsById(1L)).thenReturn(true);
        reviewService.deleteReview(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }
    @Test
    void testDeleteReviewWhenIdNotExist() {
        Mockito.when(reviewRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReview(1L));

    }

    @Test
    void getReviewsByUserId() {
    }
}