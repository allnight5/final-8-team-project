package com.team.final8teamproject.board.comment.service;

import com.team.final8teamproject.board.comment.commentReply.service.T_exerciseCommentReplyService;
import com.team.final8teamproject.board.comment.dto.CreatCommentRequestDTO;
import com.team.final8teamproject.board.comment.entity.T_exerciseComment;
import com.team.final8teamproject.board.comment.repository.T_exerciseCommentRepository;
import com.team.final8teamproject.board.repository.T_exerciseRepository;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import com.team.final8teamproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayMealCommentServiceImple implements TodayMealCommentService {

//   private final T_exerciseService t_exerciseService;
    private final T_exerciseRepository tExerciseRepository;

    private final T_exerciseCommentReplyService tExerciseCommentReplyService;
    private final T_exerciseCommentRepository commentRepository;


    @Override
    @Transactional
    public ResponseEntity<String> createComment(String comment, Long boardId, String userName) {
//        t_exerciseService.findT_exerciseBoardById(boardId);
       if (tExerciseRepository.existsById(boardId)) {
           T_exerciseComment t_exerciseComment = new T_exerciseComment(comment, userName, boardId);
           commentRepository.save(t_exerciseComment);
           return new ResponseEntity<>("댓글 작성완료", HttpStatus.OK);
       }throw new CustomException(ExceptionStatus.BOARD_NOT_EXIST);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteComment(User user, Long commentId) {
        String username = user.getUsername();
        T_exerciseComment comment = commentRepository.findById(commentId).orElseThrow(()->new CustomException(ExceptionStatus.COMMENT_NOT_EXIST));
        if(comment.isWriter(username)){
                commentRepository.deleteById(commentId);
          return new ResponseEntity<>("댓글 삭제완료",HttpStatus.OK);
        }
        throw new CustomException(ExceptionStatus.WRONG_USER_T0_COMMENT);
    }

    @Override
    public List<T_exerciseComment> findCommentByBoardId(Long boardId){

        return commentRepository.findByBoardId(boardId);
    }

    @Override
    public void deleteByBoardId(Long boardId) {

        commentRepository.deleteByBoardId(boardId);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateComment(CreatCommentRequestDTO requestDto, User user, Long commentId) {
        T_exerciseComment comment =  commentRepository.findById(commentId).orElseThrow(()-> new CustomException(ExceptionStatus.COMMENT_NOT_EXIST));

        String username = user.getUsername();

        String commentContent = requestDto.getComment();

        if(comment.isWriter(username)){
            comment.update(commentContent);
            return new ResponseEntity<>("댓글 수정완료",HttpStatus.OK);
        }
        throw new CustomException(ExceptionStatus.WRONG_USER_T0_COMMENT);
    }

}


