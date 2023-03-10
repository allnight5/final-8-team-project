package com.team.final8teamproject.contact.Comment.service;

import com.team.final8teamproject.base.entity.BaseEntity;
import com.team.final8teamproject.contact.Comment.dto.CreateContactCommentRequest;
import com.team.final8teamproject.contact.Comment.dto.UpdateContactCommentRequest;
import com.team.final8teamproject.contact.Comment.entity.ContactComment;
import com.team.final8teamproject.contact.Comment.repository.ContactCommentRepository;
import com.team.final8teamproject.contact.Repository.InquiryRepository;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import com.team.final8teamproject.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactCommentServiceImpl implements ContactCommentService {

  private final ContactCommentRepository contactCommentRepository;
  private final InquiryRepository inquiryRepository;
  /**
   * 부모댓글이 있는 경우 . 자식댓글인 경우 (대댓글) 댓글에 댓글을 저장
   * 부모댓글이 없는 경우. 포스트에 댓글을 저장
   * 프론트 단 고려하여 depth 로 계층구조 나눔
   */

  @Override
  public void saveInquiryComment(Long inquiryId,
      CreateContactCommentRequest createContactCommentRequest,
      String username,String nickName) {
    if (!inquiryRepository.existsById(inquiryId)) {
      throw new CustomException(ExceptionStatus.BOARD_NOT_EXIST);
    } else {
      /**부모댓글이 있는 경우 - 대댓글 등록. 즉 자식 댓글이 됨 */
      ContactComment parent = null;

      if (createContactCommentRequest.getParentId() != null) {
        parent = contactCommentRepository.findById(createContactCommentRequest.getParentId())
            .orElseThrow(
                () -> new CustomException(ExceptionStatus.COMMENT_NOT_EXIST)
            );
        /** 부모 댓글과 자식 댓글의 게시글 아이디가 같은지 확인*/
        if (!parent.getInquiryId().equals(inquiryId)) {
          throw new CustomException(ExceptionStatus.WRONG_POST_ID);
        }
        int depth = parent.getDepth();
        ContactComment contactComment = createContactCommentRequest.toEntity(inquiryId, username,
            nickName, parent,depth);
        // ContactComment contactComment = new ContactComment(comments,inquiryId,username,parent);
        depth = contactComment.getDepth()+1;
        contactComment.getParent().setId(createContactCommentRequest.getParentId());
        contactComment.setDepth(depth);
        contactCommentRepository.save(contactComment);

        /**부모댓글이 없는 경우 - 댓글 등록*/
      } else {
        int depth = 1;
        //   ContactComment contactComment = new ContactComment(comments,inquiryId,username,parent);
        ContactComment contactComment = createContactCommentRequest.toEntity(inquiryId, username,
           nickName, parent, depth);
        contactCommentRepository.save(contactComment);
      }
    }
  }

  @Override
  @Transactional
  public void updateInquiryComment(Long commentId, UpdateContactCommentRequest updateCommentRequest,
      String username) {
    String comments = updateCommentRequest.getComments();
    ContactComment comment = contactCommentRepository.findById(commentId).orElseThrow(
        () -> new CustomException(ExceptionStatus.COMMENT_NOT_EXIST)
    );
    if (comment.isWriter(username)) {
      comment.update(comments);
      contactCommentRepository.save(comment);
    } else {
      throw new CustomException(ExceptionStatus.WRONG_USER_T0_COMMENT);
    }
  }

  @Transactional
  @Override
  public void deleteInquiryComment(Long commentId, String username, UserRoleEnum role) {
    ContactComment comment = contactCommentRepository.findById(commentId).orElseThrow(
        () -> new CustomException(ExceptionStatus.COMMENT_NOT_EXIST)
    );
    if(comment.isWriter(username)){
      contactCommentRepository.deleteById(commentId);
    } else if(role.equals(UserRoleEnum.MANAGER)){
      contactCommentRepository.deleteById(commentId);
    }else{
      throw new CustomException(ExceptionStatus.WRONG_USER_T0_COMMENT);
    }
  }
//  @Transactional
//  @Override
//  public void deleteManager(Long id) {
//    ContactComment contactComment = contactCommentRepository.findById(id).orElseThrow(
//        () -> new CustomException(ExceptionStatus.WRONG_USER_T0_COMMENT)
//    );
//    contactCommentRepository.delete(contactComment);
//  }


  /** 해당 문의글 의 댓글 삭제하는 서비스 호출 */

  public void deleteAllByInquiryId(Long inquiryId){
    contactCommentRepository.deleteAllByInquiryId(inquiryId);
  }

  /**문의글에 해당하는 댓글 리스트 같이 반환하는 서비스 호출 */
  @Override
  public List<ContactComment> findAllByInquiryIdAndParentIsNull(Long inquiryId) {
    return contactCommentRepository.findAllByInquiryIdAndParentIsNull(inquiryId);
  }

}
