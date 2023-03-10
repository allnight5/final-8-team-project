package com.team.final8teamproject.board.service;


import com.team.final8teamproject.base.entity.BaseEntity;

import com.team.final8teamproject.board.comment.commentReply.dto.TodayMealCommentReplyResponseDTO;

import com.team.final8teamproject.board.comment.dto.TodayMealCommentResponseDTO;
import com.team.final8teamproject.board.comment.entity.TodayMealComment;
import com.team.final8teamproject.board.comment.service.TodayMealCommentService;
import com.team.final8teamproject.board.dto.CreatBordRequestDTO;
import com.team.final8teamproject.board.dto.T_exerciseBoardResponseDTO;
import com.team.final8teamproject.board.dto.TodayMealBoardResponseDTO;
import com.team.final8teamproject.board.entity.T_exercise;
import com.team.final8teamproject.board.entity.TodayMeal;
import com.team.final8teamproject.board.like.service.TodayMealLikeService;
import com.team.final8teamproject.board.repository.TodayMealRepository;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import com.team.final8teamproject.user.entity.User;
import com.team.final8teamproject.user.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayMealServiceImple implements  TodayMealService{
    private final TodayMealRepository todayMealRepository;

    private final TodayMealCommentService todayMealCommentService;
    private final TodayMealLikeService todayMealLikeService;

    private final UserService userService;

    /**
     *?????? ????????? ??????
     * @param title  ??????
     * @param content  ??????
     * @param user   ????????? ?????? ?????? ~ ????????? ?????? ?????????
     * @return    http status
     * @throws NullPointerException  ?
     * @throws IOException ?
     */
    @Transactional
    @Override
    public ResponseEntity<String> creatTodayMealBord(String title, String content, String url, BaseEntity user) throws NullPointerException, IOException {

        TodayMeal todayMeal = new TodayMeal(title,content,url,user);
        todayMealRepository.save(todayMeal);

        return new ResponseEntity<>("????????????", HttpStatus.OK);
    }

    
    /**
     * ?????? ????????? ?????? ????????? + ?????? ??????
     * @param pageRequest ????????? ?????? ??????????????? ???????????? ?????? ??????
     * @param search ????????? ????????? ????????? ???.. ??????????????? ""????????? ??????x??? ?????? ????????? ??????
     * @return ???????????? ??????
     */
    @Override
    public Result getAllTodayBoards(Pageable pageRequest, String search, Integer size, Integer page) {
        Page<TodayMeal> todayMeals = todayMealRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(search, search, pageRequest);
        int totalCount = (int) todayMeals.getTotalElements();
        Long countList = size.longValue();
        int countPage = 5;//??????????????? 10??????????????????!

        int totalPage = (int) (totalCount / countList);

        if (totalCount % countList > 0) {
            totalPage++;
        }
        if (totalPage < page) {
            page = totalPage;
        }

        List<T_exerciseBoardResponseDTO> boardResponseDTO = new ArrayList<>();

        for (TodayMeal todayMeal : todayMeals) {
            Long boardId = todayMeal.returnPostId();
            Long countLike = todayMealLikeService.countLike(boardId);
            String title = todayMeal.getTitle();
            String content = todayMeal.getContent();
            String imageUrl = todayMeal.getFilepath();
            LocalDateTime modifiedDate = todayMeal.getModifiedDate();
            String username = todayMeal.getUser().getUsername();
            String nickName = userService.getUserNickname(todayMeal.getUser());

            T_exerciseBoardResponseDTO dto = new T_exerciseBoardResponseDTO(countLike, boardId, title, content, imageUrl, modifiedDate, username, nickName);
            boardResponseDTO.add(dto);
        }
        return new Result(page, totalCount, countPage, totalPage, boardResponseDTO);
    }

    /**
     * ??????.. ????????? ?????? ?????? ~ ?????? ????????? ?????? ????????? ?????????... ??????????????? ????????????..
     * ????????? ??????????????? ?????? ??? !
     * ???????????? ??????????????? ... ????????????~ ?????? ?????? ???????????? ???????????? ???
     * @param boardId  ?????????????????????
     * @return DTO??? ????????? ??????
     */
    //??????...?????? ??????????????? ??????????????? ..!!! ! ! ! ! ! ! ! ! !! ! ! ! !
    @Override
    public TodayMealBoardResponseDTO getTodayMealBoard(Long boardId) {
        TodayMeal todayMeal = todayMealRepository.findById(boardId).orElseThrow(()-> new CustomException(ExceptionStatus.BOARD_NOT_EXIST));
        List<TodayMealComment> comments = todayMealCommentService.findCommentByBoardId(boardId);
        List<TodayMealCommentResponseDTO> commentFilter = comments.stream()
                .map(comment -> {
                    List<TodayMealCommentReplyResponseDTO> toList = comment.getCommentReplyList().stream()
                            .map(TodayMealCommentReplyResponseDTO::new)
                            .collect(Collectors.toList());
                    return new TodayMealCommentResponseDTO(comment.getId(), comment.getComment(), comment.getUsername(),
                            comment.getCreatedDate(), toList, comment.getUserNickname());
                })
                .collect(Collectors.toList());
        Long countLike = todayMealLikeService.countLike(boardId);
        String userNickname = userService.getUserNickname(todayMeal.getUser());

        return new TodayMealBoardResponseDTO(countLike,todayMeal,commentFilter,userNickname);
    }


    /**
     * ???????????? ????????? ????????? ????????????!
     * @param boardId ?????????????????????
     * @param user  ?????????????????? ??????
     * @return  status
     */
    @Override
    @Transactional
    public ResponseEntity<String> deletePost(Long boardId, BaseEntity user) {
        TodayMeal todayMeal = todayMealRepository.findById(boardId).orElseThrow(()-> new CustomException(ExceptionStatus.BOARD_NOT_EXIST));


        if (todayMeal.isWriter(user.getId())) {
            todayMealRepository.deleteById(boardId);
            todayMealCommentService.deleteByBoardId(boardId);
            return new ResponseEntity<>("????????? ?????? ??????????????????", HttpStatus.OK);
        } else {
            throw new CustomException(ExceptionStatus.WRONG_SELLER_ID_T0_BOARD);
                    
        }
    }


    /**
     * ????????? ????????? ??????
     * @param boardId  ?????????id
     * @param creatBordRequestDTO ????????? ????????? ????????????
     * @param user  ????????? ????????? ??????
     * @param imageUrl ????????? ????????? ?????????~
     * @return   status
     * @throws IOException ?
     */
    @Override
    @Transactional
    public ResponseEntity<String> editPost(Long boardId,
                                           CreatBordRequestDTO creatBordRequestDTO,
                                           BaseEntity user,
                                           String imageUrl) throws  IOException
    {
        TodayMeal todayMeal = todayMealRepository.findById(boardId).orElseThrow(() -> new CustomException(ExceptionStatus.BOARD_NOT_EXIST));

        if(todayMeal.isWriter(user.getId())){

            String content = creatBordRequestDTO.getContent();
            String title = creatBordRequestDTO.getTitle();

            todayMeal.editSalePost(title,content,imageUrl);
            return new ResponseEntity<>("????????? ?????? ??????",HttpStatus.OK);
        }throw new CustomException(ExceptionStatus.WRONG_SELLER_ID_T0_BOARD);
    }

    @Override
    public TodayMeal findBoardById(Long id) {
        return todayMealRepository.findById(id).orElseThrow(()-> new CustomException(ExceptionStatus.BOARD_NOT_EXIST));
    }

    @Override
    public List<TodayMealBoardResponseDTO> getTop3PostByLike() {
        List<TodayMeal> todaymeals = todayMealRepository.findIdByCreatedDateString(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        List<TodayMealBoardResponseDTO> top3Post = new ArrayList<>();


        HashMap<TodayMeal,Long> postSortByLike = new HashMap();
        ValueComparator bvc =  new ValueComparator(postSortByLike);
        TreeMap<TodayMeal,Long> sorted_map = new TreeMap<>(bvc);


        for (TodayMeal todayMeal : todaymeals) {
            Long boardId = todayMeal.returnPostId();
            Long countLike = todayMealLikeService.countLike(boardId);
            postSortByLike.put(todayMeal,countLike);
        }
        sorted_map.putAll(postSortByLike);

        int count =0;
        for (Map.Entry<TodayMeal, Long> todayMealLongEntry : sorted_map.entrySet()) {
           TodayMeal exercise = todayMealLongEntry.getKey();

            Long boardId = exercise.returnPostId();
            Long countLike = todayMealLongEntry.getValue();
            String title = exercise.getTitle();
            String content = exercise.getContent();
            String imageUrl = exercise.getFilepath();
            LocalDateTime modifiedDate = exercise.getModifiedDate();
            String username = exercise.getUser().getUsername();
            String nickName = userService.getUserNickname(exercise.getUser());

            TodayMealBoardResponseDTO dto = new TodayMealBoardResponseDTO(countLike, boardId, title, content, imageUrl, modifiedDate, username, nickName);
            top3Post.add(dto);
            count++;
            if (count==3){
                break;
            }
        }
        return top3Post;
    }

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
    private class ValueComparator implements Comparator<TodayMeal> {

        Map<TodayMeal, Long> base;

        public ValueComparator(Map<TodayMeal, Long> base) {
            this.base = base;
        }

        // Note: this comparator imposes orderings that are inconsistent with equals.
        public int compare(TodayMeal a, TodayMeal b) {
            if (base.get(a) >= base.get(b)) { //????????? ?????? ???????????? <=
                return -1;
            } else {
                return 1;
            } // returning 0 would merge keys
        }
    }
}
