package com.team.final8teamproject.share.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionStatus {
    DUPLICATED_USERNAME(409, "이미 사용중인 아이디입니다."),
    DUPLICATED_NICKNAME(408, "이미 사용중인 닉네임입니다."),
    DUPLICATED_EMAIL(407, "이미 사용중인 이메일입니다."),
    DUPLICATED_PHONENUMBER(406, "이미 사용중인 휴대폰번호입니다."),
    SIGNUP_WRONG_USERNAME(409, "최소 4자 이상, 10자 이하이며, 영문과 숫자만 입력하세요."),
    WRONG_USERNAME(404, "아이디를 잘못 입력 하였거나 등록되지 않은 아이디 입니다."),
    WRONG_PASSWORD(400, "잘못된 비밀번호 입니다."),
    WRONG_PROFILE(404, "프로필이 존재하지 않습니다."),
    WRONG_AUTHORITY_DEMAND(404, "판매자 권한 신청서가 존재하지 않습니다"),
    ALREADY_EXIST_SELLER(409,"셀러로 등록된 아이디 입니다,"),
    ALREADY_EXIST_ADMIN(409,"어드민으로 등록된 아이디 입니다."),
    ALREADY_EXIST_REQUEST(409,"이미 전송된 요청입니다."),
    ALREADY_PROCESSED_REQUEST(409,"이미 처리된 요청입니다."),
    WRONG_ADMINTOKEN(400, "잘못된 관리자 비밀번호 입니다."),
    ACCESS_DENINED(500, "접근 권한이 없습니다."),
    AUTHENTICATION(500, "인증 실패"), 
    NOT_FOUNT_USER(404,"해당 사용자가 존재하지 않습니다."),
    NOT_FOUNT_TOKEN(404,"토큰이 일치하지 않습니다."), 
    AUTH_EXPIRED(501, "인증 만료"), 
    BOARD_NOT_EXIST(404, "게시물이 삭제되어 존재하지 않습니다."),
    COMMENT_NOT_EXIST(404, "해당 댓글이 삭제되어 존재 하지 않습니다."),
    COMMENT_REPLY_NOT_EXIST(404, "해당하는 댓글이 삭제되어 존재 하지 않습니다."),
    REQUEST_NOT_EXIST(404,"해당하는 요청이 존재하지 않습니다."),
    WRONG_POST_ID(404,"게시글 번호가 일치하지 않습니다."),
    POST_IS_EMPTY(404,"해당 페이지는 게시글이 존재하지 않습니다."),
    SECRET_POST(403,"비밀글입니다. 해당 사용자 외엔 조회 불가능 합니다. "),
    WRONG_SELLER_ID_T0_BOARD(403,"다른 판매자의 게시물에는 접근 할 수 없습니다."),
    WRONG_USER_T0_COMMENT(403,"다른 유저의 댓글에는 접근 할 수 없습니다."),
    WRONG_USER_T0_CONTACT(403,"다른 유저의 게시글에는 접근 할 수 없습니다."),
    WRONG_USER_T0_COMMENT_REPLY(403,"다른 유저의 대댓글에는 접근 할 수 없습니다."),
    WRONG_SELLER_ID_TO_USER_REQUEST(403,"다른 판매자의 요청목록에는 접근 할 수 없습니다."),

    IS_NOT_CORRECT_FORMAT(400,"지원하지 않는 형식입니다.");

    private final int statusCode;
    private final String message;
}
