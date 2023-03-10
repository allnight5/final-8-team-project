package com.team.final8teamproject.gymboard.service;

import com.team.final8teamproject.gymboard.dto.*;
import com.team.final8teamproject.gymboard.entity.GymBoard;
import com.team.final8teamproject.gymboard.repository.GymBoardRepository;
import com.team.final8teamproject.gymboardreview.dto.GymBoardReviewResponseDto;
import com.team.final8teamproject.gymboardreview.dto.GymBoardviewResponseDto;
import com.team.final8teamproject.gymboardreview.repository.GymReviewRepository;
import com.team.final8teamproject.gymboardreview.service.GymBoardReviewServiceImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GymPostServiceImpl implements GymPostService {
    private final GymBoardRepository gymBoardRepository;
    private final GymBoardReviewServiceImpl gymBoardReviewServiceImpl;
    private final GymReviewRepository gymReviewRepository;

    //1.헬스장 게시글 작성
    @Override
    public String createGymPost(CreatePostGymRequestDto requestDto,
                                String imgUrl,
                                String username){
        GymBoard gymBoard = GymBoard.GymBoard()
                .title(requestDto.getTitle())
                .content(requestDto.getContents())
                .username(username)
                .image(imgUrl)
                .gymName(requestDto.getGymName())
                .ownerNumber(requestDto.getOwnerNumber())
                .region(requestDto.getRegion())
                .price(requestDto.getPrice())
                .openTime(requestDto.getOpenTime())
                .amenitiesDetail(requestDto.getAmenitiesDetail())
                .amenities(requestDto.getAmenities())
                .build();
        gymBoardRepository.save(gymBoard);
        return "등록 완료";
    }
    //2. 메인 페이지 보여주기
    @Override
    public List<GymPostResponseDto> getGymPostAll() {
        List<GymBoard> gymBoards = gymBoardRepository.findAll();
        List<GymPostResponseDto> gymPostResponseDetailDtosList = new ArrayList<>();
        for (GymBoard gymBoard : gymBoards) {
            GymPostResponseDto gymPostResponseDetailDto = new GymPostResponseDto(gymBoard);
            gymPostResponseDetailDtosList.add(gymPostResponseDetailDto);
        }
        return gymPostResponseDetailDtosList;
//        return new InquiryServiceImpl.Result<List<InquiryResponse>>(page, totalCount, countPage, totalPage, inquiryResponses);
//        return new Result<List<GymPostResponseDto>>(page, totalCount, countPage, totalPage, gymPostResponseDetailDtosList);
    }
    //3. 작성된 운동시설 보여주기
    @Override
    public Result<List<GymPostResponseDto>> getGymPost(Pageable pageRequest, String search, Integer size, Integer page) {
        Page<GymBoard> gymBoards = gymBoardRepository.findByTitleContainingIgnoreCase(search, pageRequest);
        int totalCount = (int) gymBoards.getTotalElements();
        Long countList = size.longValue();
        int countPage = 5;//리펙토링때 10으로변경합세!
        int totalPage = (int) (totalCount / countList);

        if (totalCount % countList > 0) {
            totalPage++;
        }
        if (totalPage < page) {
            page = totalPage;
        }
        List<GymPostResponseDto> gymPostResponseDetailDtosList = new ArrayList<>();
        for (GymBoard gymBoard : gymBoards) {
            GymPostResponseDto gymPostResponseDetailDto = new GymPostResponseDto(gymBoard);
            gymPostResponseDetailDtosList.add(gymPostResponseDetailDto);
        }
        return new Result<List<GymPostResponseDto>>(page, totalCount, countPage, totalPage, gymPostResponseDetailDtosList);

//        return gymPostResponseDetailDtosList;
//        return new InquiryServiceImpl.Result<List<InquiryResponse>>(page, totalCount, countPage, totalPage, inquiryResponses);
    }
    //4. 작성한 운동시설 선택시 세부사항
    @Override
    @Transactional(readOnly = true)
    public GymPostResponseDetailDto getGymPostDetail(Long id) {
        GymBoard gymBoards = gymBoardRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("게시글이 삭제되어 찾을수 없습니다.")
        );
        List<GymBoardReviewResponseDto> review = gymBoardReviewServiceImpl.getReview(gymBoards.getId());
        return new GymPostResponseDetailDto(gymBoards, review);
    }
    //5. 자기가 작성한 운동시설 전부 조회
    @Override
    public List<GymBoardviewResponseDto> getAllGymPost(int pageChoice, String username){
        Page<GymBoard> gymBoards = gymBoardRepository.findByUsername(pageableGymPostSetting(pageChoice),username);
        return gymBoards.stream().map(GymBoardviewResponseDto::new).collect(Collectors.toList());
    }
    //5-1. 페이징 처리 메소드
    private Pageable pageableGymPostSetting(int pageChoice){
        Sort.Direction direction = Sort.Direction.DESC;
        Sort sort = Sort.by(direction,"id");
        return PageRequest.of(pageChoice -1,9,sort);
    }
    //6. 헬스장 게시글 수정
    @Override
    public void updateGymPost(GymUpdateRequestDto requestDto,String imageUrl, String username, Long id) {
        GymBoard gymBoard = gymBoardRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        gymBoard.update(requestDto, imageUrl);
    }
    /** 
     * @param id          삭제할 게시글 번호
     * @param username    게시글과 작성자가 같은지 검사하기 위한 파라미터
     * @return  
     */

    //7. 헬스장 게시글 삭제
    @Override
    @Transactional
    public String deleteGymPost(Long id, String username) {
        GymBoard gymBoard = gymBoardRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        gymBoardRepository.deleteById(gymBoard.getId());
        gymReviewRepository.deleteByGymId(gymBoard.getId());
        return "삭제완료";
    }

    //8.가격 할인 업데이트
    @Override
    public String discountUpdate(Long id, String discount, String username){
        GymBoard gymBoard = gymBoardRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        gymBoard.discountUpdate(discount);
        return "할인적용";
    }
    //8.가격 할인 업데이트
    @Override
    public String deleteUpdate(Long id, String discount, String username){
        GymBoard gymBoard = gymBoardRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        gymBoard.discountDelete(discount);
        return "할인적용";
    }
    //10. 가격 변경
    public String priceUpdate(Long id, String price, String username){
        GymBoard gymBoard = gymBoardRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        gymBoard.priceUpdate(price);
        return "변경완료";
    }
    public String imageUpdate(Long id, String imageUrl, String username){
        GymBoard gymBoard = gymBoardRepository.findByIdAndUsername(id, username).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        gymBoard.priceUpdate(imageUrl);
        return "변경완료";
    }
    /**
     * 스레드와 스케쥴러를 이용하여
     * 일정 주기별로 리뷰 평점점수를 총합하여 데이터베이스를 업데이트 해줌
     */

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Result<T> {
        private int page;
        private int totalCount;
        private int countPage;
        private int totalPage;
        private T data;

        public Result(int page, int totalCount, int countPage, int totalPage, T data) {
            this.page = page;
            this.totalCount = totalCount;
            this.countPage = countPage;
            this.totalPage = totalPage;
            this.data = data;
        }
    }
}

 /* 참조형 해보기 default를 활용해서 더하기가 안됨
        List<GymBoard> gymBoards = gymBoardRepository.findAll();
        if(gymBoards.isEmpty()){
            throw new IllegalArgumentException("작성된 운동시설이 없습니다.");
        }
        List<GymReview> gymReview = gymReviewRepository.findAll();
        if(gymReview.isEmpty()){
            throw new IllegalArgumentException("리뷰가 없습니다.");
        }
        Map<Long, RatingDto> rating = new HashMap<>();
        for(GymReview gymReview1 : gymReview){
            Long key = gymReview1.getGymId();
            Long value = gymReview1.getRating();
            RatingDto ratingDto = new RatingDto(value, 1L);
            rating.put(key , ratingDto);
        }
        for (GymBoard gymBoard : gymBoards) {
            if(rating.get(gymBoard.getId()) == null){
                continue;
            }
            RatingDto ratingDto = rating.get(gymBoard.getId());
            gymBoard.ratingUpdate(ratingDto.getTotal()/ratingDto.getCount());
        }
 */
