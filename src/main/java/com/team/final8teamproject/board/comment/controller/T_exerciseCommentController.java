package com.team.final8teamproject.board.comment.controller;

import com.team.final8teamproject.board.comment.dto.CreatCommentRequestDTO;
import com.team.final8teamproject.board.comment.service.T_exerciseCommentService;
import com.team.final8teamproject.security.service.UserDetailsImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/t-exercise")
@RequiredArgsConstructor
public class T_exerciseCommentController {
    private final T_exerciseCommentService tExerciseCommentService;

    //댓글작성
    @PostMapping("/{boardId}/comment")
    public ResponseEntity<String> createComment(@RequestBody CreatCommentRequestDTO requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long boardId) {
        String comment = requestDto.getComment();
        String userName = userDetails.getUsername();
        String userNickname = userDetails.getBase().getNickName();
        return tExerciseCommentService.createComment(comment, boardId,userName,userNickname);
    }

    //댓글삭제 //
    @DeleteMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {
        return tExerciseCommentService.deleteComment(userDetails.getBase(), commentId);
    }

    //댓글수정
    @PutMapping("/{boardId}/comment/{commentId}")
    public ResponseEntity<String> updateComment(@RequestBody CreatCommentRequestDTO requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {

        return tExerciseCommentService.updateComment(requestDto, userDetails.getBase(), commentId);
    }
}
