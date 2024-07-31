package com.commit.campus.service.impl;

import com.commit.campus.common.exceptions.ReviewAlreadyExistsException;
import com.commit.campus.dto.ReviewDTO;
import com.commit.campus.entity.MyReview;
import com.commit.campus.entity.Review;
import com.commit.campus.repository.MyReviewRepository;
import com.commit.campus.repository.RatingSummaryRepository;
import com.commit.campus.repository.ReviewRepository;
import com.commit.campus.repository.UserRepository;
import com.commit.campus.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final MyReviewRepository myReviewRepository;
    private final UserRepository userRepository;
    private final RatingSummaryRepository ratingSummaryRepository;
    private final ModelMapper modelMapper;

    public ReviewServiceImpl(ReviewRepository reviewRepository, MyReviewRepository myReviewRepository, UserRepository userRepository, RatingSummaryRepository ratingSummaryRepository, ModelMapper modelMapper) {
        this.reviewRepository = reviewRepository;
        this.myReviewRepository = myReviewRepository;
        this.userRepository = userRepository;
        this.ratingSummaryRepository = ratingSummaryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ReviewDTO> getReviewsByCampId(long campId, Pageable pageable) {
        log.info("서비스 진입");
        Page<Review> reviewPage = reviewRepository.findByCampId(campId, pageable);
        log.info("반환 확인: {}", reviewPage);

        return reviewPage.map(review -> {
            ReviewDTO reviewDTO = modelMapper.map(review, ReviewDTO.class);
            log.info("DTO 확인: {}", reviewDTO);
            long userId = review.getUserId();
            log.info("id 확인: {}", userId);
            String userNickname = userRepository.findNicknameByUserId(userId);
            log.info("nickname 확인: {}", userNickname);
            reviewDTO.setUserNickname(userNickname);
            log.info("DTO 확인: {}", reviewDTO);
            return reviewDTO;
        });
    }

    @Override
    public void createReview(ReviewDTO reviewDTO) throws ReviewAlreadyExistsException {

        boolean reviewExists = reviewRepository.existsByUserIdAndCampId(reviewDTO.getUserId(), reviewDTO.getCampId());
        if (reviewExists) {
            throw new ReviewAlreadyExistsException("이미 이 캠핑장에 대한 리뷰를 작성하셨습니다.", HttpStatus.CONFLICT);
        }

        log.info("서비스 확인 reviewDTO {}", reviewDTO);
        Review review = Review.builder()
                .campId(reviewDTO.getCampId())
                .userId(reviewDTO.getUserId())
                .reviewContent(reviewDTO.getReviewContent())
                .rating(reviewDTO.getRating())
                .reviewCreatedDate(LocalDateTime.now())
                .reviewImageUrl(reviewDTO.getReviewImageUrl())
                .build();
        log.info("서비스 확인 entity {}", review);

        Review savedReivew = reviewRepository.save(review);

        MyReview myReview = myReviewRepository.findById(savedReivew.getUserId())
                .orElse(new MyReview(savedReivew.getUserId()));

        myReview.addReview(savedReivew.getReviewId());
        log.info("서비스 확인 myreview {}", myReview);

        myReviewRepository.save(myReview);
        log.info("서비스 확인 내 리뷰 저장 완료");
        ratingSummaryRepository.ratingUpdate(savedReivew.getCampId(), savedReivew.getRating());
    }

    @Override
    public void updateReview(long reviewId, ReviewDTO reviewDTO) {
    }

    @Override
    public void deleteReview(long reviewId) {

    }
}
