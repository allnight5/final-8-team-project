package com.team.final8teamproject;

//import com.team.final8teamproject.contact.Comment.entity.ContactComment;
//import com.team.final8teamproject.contact.Comment.repository.ContactCommentRepository;
import com.team.final8teamproject.board.entity.T_exercise;
import com.team.final8teamproject.board.repository.T_exerciseRepository;
import com.team.final8teamproject.contact.Repository.FaqRepository;
import com.team.final8teamproject.contact.Repository.InquiryRepository;
import com.team.final8teamproject.contact.Repository.NoticeRepository;
import com.team.final8teamproject.contact.entity.Faq;
import com.team.final8teamproject.contact.entity.Inquiry;
import com.team.final8teamproject.contact.entity.Notice;
import com.team.final8teamproject.contact.service.FaqService;
import com.team.final8teamproject.owner.entity.Owner;
import com.team.final8teamproject.owner.repository.OwnerRepository;
import com.team.final8teamproject.user.entity.User;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import com.team.final8teamproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class initData implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final OwnerRepository ownerRepository;
    private final InquiryRepository inquiryRepository;
    private final FaqRepository faqRepository;
//    private final ContactCommentRepository contactCommentRepository;
    private final NoticeRepository noticeRepository;

    private final T_exerciseRepository tExerciseRepository;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        User manager = User.builder()
                .nickName("manager").email("manager@nvaer.com")
                .phoneNumber("01011112222").password(passwordEncoder.encode("manager1234"))
                .username("manager").role(UserRoleEnum.MANAGER).experience(0L)
                .build();
        Owner owner = Owner.builder()
                .nickName("owner1").email("owner@google.com")
                .phoneNumber("01022223333")
                .ownerNumber("owner2")
                .password(passwordEncoder.encode("owner1234"))
                .username("owner1").role(UserRoleEnum.OWNER).experience(0L)
                .build();
        Owner owner1 = Owner.builder()
                .nickName("owner2").email("owner2@google.com")
                .ownerNumber("owner2")
                .phoneNumber("01022233333")
                .password(passwordEncoder.encode("owner1234"))
                .username("owner2").role(UserRoleEnum.OWNER).experience(0L)
                .build();
        User member =User.builder()
                .nickName("member").email("member1@google.com")
                .phoneNumber("01033334444")
                .password(passwordEncoder.encode("member1234"))
                .username("member1").role(UserRoleEnum.MEMBER).experience(0L)
                .build();

        User member1 =User.builder()
                .nickName("member1").email("member2@google.com")
                .phoneNumber("01033334444").password(passwordEncoder.encode("member1234"))
                .username("member2").role(UserRoleEnum.MEMBER).experience(0L)
                .build();

        userRepository.save(manager);
        userRepository.save(member);
        userRepository.save(member1);
        ownerRepository.save(owner);
        ownerRepository.save(owner1);


        // -------------------고객 센터 관련

        Inquiry ownerInquiry = new Inquiry("owner","사업자 문의","입점가능?",false);
        Inquiry memberInquiry = new Inquiry("member","회원 문의","회원가입?",true); // 문의글 비밀상태
        inquiryRepository.save(ownerInquiry);
        inquiryRepository.save(memberInquiry);

//        ContactComment parentComment = new ContactComment("부모댓글",1L,"member",null);
//        ContactComment parentComment1 = new ContactComment("부모댓글",1L,"member",null);
//        contactCommentRepository.save(parentComment);
//        contactCommentRepository.save(parentComment1);
//        ContactComment childrenComment = new ContactComment("댓글",1L,"member",parentComment);
//        contactCommentRepository.save(childrenComment);
//        ContactComment childrenComment1 = new ContactComment("대댓글",1L,"member",childrenComment);
//        contactCommentRepository.save(childrenComment1);
//        ContactComment childrenComment2 = new ContactComment("댓글",1L,"member",parentComment1);
//        contactCommentRepository.save(childrenComment2);

        Notice notice = new Notice(1L,"공지사항","공지사항입니다.");
        Notice notice1 = new Notice(1L," 대체공휴일 휴무 안내","휴무입니다.");
        noticeRepository.save(notice);
        noticeRepository.save(notice1);


        Faq faq1 = new Faq(1L,"센터 운영 중인데 [ㅇㅇㅇ] 에 등록?입점? 하고 싶습니다.","홈페이지 상단 xx 페이지에서 ~ 진행");
        Faq faq2 = new Faq(1L,"회원 탈퇴는 어떻게 해야 하나요?",
                "1. 홈페이지 xx 메뉴중 '전체' 메뉴를 클릭하시고 '내 정보' 메뉴를 클릭합니다.\n"
                        + "2. 하단 좌측에 '탈퇴하기' 버튼을 클릭하고 탈퇴를 진행합니다.\n"
                        + "\n"
                        + "유의사항\n"
                        + "1. 회원 탈퇴 하더라도 모든 정보는 삭제 되지 않습니다. 원할 시 회원 탈퇴 전에 원하는 글에 대해 삭제 하시길 바랍니다. \n"
                        + "   단, 게시글 중 댓글이 있는 경우는 삭제 할 수 없습니다. \n"
                        + "2. 본인의 의지와 상관없이 이용약관 위반 등으로 탈퇴된 경우 재등록이 제한될 수 있습니다");
        faqRepository.save(faq1);
        faqRepository.save(faq2);
        // -----------------------------------


        T_exercise t_exercise1 = new T_exercise("1번제목","내용","파일이름","경로",member1);
        T_exercise t_exercise2 = new T_exercise("2번제목","내용","파일이름","경로",member1);
        T_exercise t_exercise3 = new T_exercise("3번제목","내용","파일이름","경로",member1);
        T_exercise t_exercise4 = new T_exercise("4번제목","검색","파일이름","경로",member1);
        T_exercise t_exercise5 = new T_exercise("5번제목","내용","파일이름","경로",member1);
        T_exercise t_exercise6 = new T_exercise("6번제목","내용","파일이름","경로",member1);
        T_exercise t_exercise7 = new T_exercise("7번제목","검색","파일이름","경로",member1);
        T_exercise t_exercise8 = new T_exercise("8번제목","내용","파일이름","경로",member1);
        T_exercise t_exercise9 = new T_exercise("9번제목","검","파일이름","경로",member1);
        T_exercise t_exercise10 = new T_exercise("10번제목검색","내용","파일이름","경로",member1);

        tExerciseRepository.save(t_exercise1);
        tExerciseRepository.save(t_exercise2);
        tExerciseRepository.save(t_exercise3);
        tExerciseRepository.save(t_exercise4);
        tExerciseRepository.save(t_exercise5);
        tExerciseRepository.save(t_exercise6);
        tExerciseRepository.save(t_exercise7);
        tExerciseRepository.save(t_exercise8);
        tExerciseRepository.save(t_exercise9);
        tExerciseRepository.save(t_exercise10);
    }
}