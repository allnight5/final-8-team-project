package com.team.final8teamproject;


import com.team.final8teamproject.board.entity.T_exercise;
import com.team.final8teamproject.board.like.entity.T_exerciseLike;
import com.team.final8teamproject.board.like.repository.T_exerciseLikeRepository;
import com.team.final8teamproject.board.repository.T_exerciseRepository;
import com.team.final8teamproject.contact.Comment.entity.ContactComment;
import com.team.final8teamproject.contact.Comment.repository.ContactCommentRepository;
import com.team.final8teamproject.contact.Repository.FaqRepository;
import com.team.final8teamproject.contact.Repository.InquiryRepository;
import com.team.final8teamproject.contact.Repository.NoticeRepository;
import com.team.final8teamproject.contact.entity.Faq;
import com.team.final8teamproject.contact.entity.Inquiry;
import com.team.final8teamproject.contact.entity.Notice;
import com.team.final8teamproject.gymboard.entity.GymBoard;
import com.team.final8teamproject.gymboard.repository.GymBoardRepository;
import com.team.final8teamproject.gymboardreview.entity.GymReview;
import com.team.final8teamproject.gymboardreview.repository.GymReviewRepository;
import com.team.final8teamproject.manager.entity.Manager;
import com.team.final8teamproject.manager.repository.ManagerRepository;
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
import org.springframework.transaction.annotation.Transactional;

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

  private final T_exerciseLikeRepository tExerciseLikeRepository;

  private final ContactCommentRepository contactCommentRepository;
  private final GymBoardRepository gymBoardRepository;
  private final GymReviewRepository gymReviewRepository;
  private final ManagerRepository managerRepository;

  @Override
  @Transactional
  public void run(ApplicationArguments args) throws Exception {
//
//    Manager manager = Manager.builder()
//            .nickName("manager").email("manager@nvaer.com")
//            .phoneNumber("01011112222").password(passwordEncoder.encode("manager1234"))
//            .username("manager").role(UserRoleEnum.MANAGER).experience(0L)
//            .build();
//    Owner owner = Owner.builder()
//            .nickName("owner1").email("owner@google.com")
//            .phoneNumber("01022223333")
//            .ownerNumber("owner2")
//            .password(passwordEncoder.encode("owner1234"))
//            .username("owner1").role(UserRoleEnum.OWNER).experience(0L)
//            .storeName("짐")
//            .build();
//    Owner owner1 = Owner.builder()
//            .nickName("owner2").email("owner2@google.com")
//            .ownerNumber("owner2")
//            .phoneNumber("01022233333")
//            .password(passwordEncoder.encode("owner1234"))
//            .username("owner2").role(UserRoleEnum.OWNER).experience(0L)
//            .storeName("짐짐")
//            .build();
//    User member = User.builder()
//            .nickName("member").email("member@google.com")
//            .phoneNumber("01033334444")
//            .password(passwordEncoder.encode("member1234"))
//            .username("member").role(UserRoleEnum.MEMBER).experience(0L)
//            .build();
//
//    User member1 = User.builder()
//            .nickName("member1").email("member1@google.com")
//            .phoneNumber("01033334444").password(passwordEncoder.encode("member1234"))
//            .username("member1").role(UserRoleEnum.MEMBER).experience(0L)
//            .build();
//    User member2 = User.builder()
//            .nickName("안녕하세요").email("member2@google.com")
//            .phoneNumber("01033334444").password(passwordEncoder.encode("member1234"))
//            .username("member2").role(UserRoleEnum.MEMBER).experience(0L)
//            .build();
//    User member3 = User.builder()
//            .nickName("마포동포동동").email("member3@google.com")
//            .phoneNumber("01033334444").password(passwordEncoder.encode("member1234"))
//            .username("member3").role(UserRoleEnum.MEMBER).experience(0L)
//            .build();
//
//    User member4 = User.builder()
//            .nickName("로니에여").email("member4@google.com")
//            .phoneNumber("01033334444").password(passwordEncoder.encode("member1234"))
//            .username("member4").role(UserRoleEnum.MEMBER).experience(0L)
//            .build();
//
//    User member5 = User.builder()
//            .nickName("콜먼").email("member5@google.com")
//            .phoneNumber("01033334444").password(passwordEncoder.encode("member1234"))
//            .username("member5").role(UserRoleEnum.MEMBER).experience(0L)
//            .build();
//
//    managerRepository.save(manager);
//    userRepository.save(member);
//    userRepository.save(member1);
//    userRepository.save(member2);
//    userRepository.save(member3);
//    userRepository.save(member4);
//    userRepository.save(member5);
//    ownerRepository.save(owner);
//    ownerRepository.save(owner1);
//
//    // -------------------고객 센터 관련
//
//    Inquiry ownerInquiry = new Inquiry("member1", "member ", "사업자 문의", "입점가능?", false);
//    Inquiry memberInquiry = new Inquiry("member2", "member1", "회원 문의", "회원가입?", true); // 문의글 비밀상태
//    Inquiry ownerInquiry1 = new Inquiry("member1", "member", "아무거나 문의", "입점가능?", false);
//    Inquiry memberInquiry2 = new Inquiry("member2", "member1", "이것도 문의", "회원가입?", true); // 문의글 비밀상태
//    inquiryRepository.save(ownerInquiry);
//    inquiryRepository.save(memberInquiry);
//    inquiryRepository.save(ownerInquiry1);
//    inquiryRepository.save(memberInquiry2);
//
//    ContactComment parentComment = new ContactComment("부모댓글", "member", 4L, "멤버", null, 0);
//    contactCommentRepository.save(parentComment);
//    ContactComment childrenComment = new ContactComment("댓글", "member", 4L, "멤버", parentComment,
//            1);
//    contactCommentRepository.save(childrenComment);
//    ContactComment childrenComment1 = new ContactComment("대댓글", "member", 4L, "멤버",
//            childrenComment, 2);
//    contactCommentRepository.save(childrenComment1);
//    ContactComment childrenComment3 = new ContactComment("대대댓글", "member", 4L, "멤버",
//            childrenComment, 3);
//    contactCommentRepository.save(childrenComment3);
//    ContactComment parentComment1 = new ContactComment("부모댓글", "member", 4L, "멤버", null, 0);
//    contactCommentRepository.save(parentComment1);
//    ContactComment childrenComment2 = new ContactComment("댓글", "member", 4L, "멤버", parentComment1,
//            1);
//    contactCommentRepository.save(childrenComment2);
//
//    Notice notice = new Notice(1L, "공지사항", "공지사항입니다.",
//            "");
//    Notice notice1 = new Notice(1L, " 오픈했습니다.", "오픈했습니다.^^",
//            "");
//    Notice notice2 = new Notice(1L, " 더미1", "휴무입니다.",
//            "");
//    Notice notice3 = new Notice(1L, " 더미2", "휴무입니다.",
//            "");
//    Notice notice4 = new Notice(1L, " 더미3", "휴무입니다.",
//            "");
//    Notice notice5 = new Notice(1L, " 더미4", "휴무입니다.",
//            "");
//    Notice notice6 = new Notice(1L, "더미5", "공지사항입니다.",
//            "");
//    Notice notice7 = new Notice(1L, " 더미6", "휴무입니다.",
//            "");
//    Notice notice8 = new Notice(1L, "더미7", "휴무입니다.",
//            "");
//    Notice notice9 = new Notice(1L, " 더미8", "휴무입니다.",
//            "");
//    Notice notice10 = new Notice(1L, " 더미9", "휴무입니다.",
//            "");
//    Notice notice11 = new Notice(1L, " 더미10", "휴무입니다.",
//            "");
//    Notice notice12 = new Notice(1L, " 더미11", "휴무입니다.",
//            "");
//
//    noticeRepository.save(notice);
//    noticeRepository.save(notice1);
//    noticeRepository.save(notice2);
//    noticeRepository.save(notice3);
//    noticeRepository.save(notice4);
//    noticeRepository.save(notice5);
//    noticeRepository.save(notice6);
//    noticeRepository.save(notice7);
//    noticeRepository.save(notice8);
//    noticeRepository.save(notice9);
//    noticeRepository.save(notice10);
//    noticeRepository.save(notice11);
//    noticeRepository.save(notice12);
//
//
//    Faq faq1 = new Faq(1L, "센터 운영 중인데 [ㅇㅇㅇ] 에 등록?입점? 하고 싶습니다.", "홈페이지 상단 xx 페이지에서 ~ 진행");
//    Faq faq2 = new Faq(1L, "회원 탈퇴는 어떻게 해야 하나요?",
//            "1. 홈페이지 xx 메뉴중 '전체' 메뉴를 클릭하시고 '내 정보' 메뉴를 클릭합니다.\n"
//                    + "2. 하단 좌측에 '탈퇴하기' 버튼을 클릭하고 탈퇴를 진행합니다.\n"
//                    + "\n"
//                    + "유의사항\n"
//                    + "1. 회원 탈퇴 하더라도 모든 정보는 삭제 되지 않습니다. 원할 시 회원 탈퇴 전에 원하는 글에 대해 삭제 하시길 바랍니다. \n"
//                    + "   단, 게시글 중 댓글이 있는 경우는 삭제 할 수 없습니다. \n"
//                    + "2. 본인의 의지와 상관없이 이용약관 위반 등으로 탈퇴된 경우 재등록이 제한될 수 있습니다");
//    Faq faq3 = new Faq(1L, "인증메일이 오지 않습니다.", "인증 이메일은 입력하신 이메일로 전송됩니다. 다시 한번 확인해주세요.\n"
//            + "인증이메일이 안온다면 이메일를 정확히 입력 하셨는지 다시 한 번 확인부탁드리겠습니다.\n"
//            + "이메일을 정확히 입력 하셨는데도 인증메세지가 안온다면 서칭짐 고객센터로 문의주시길 부탁드리겠습니다. \n"
//            + "최선을 다해 문제해결을 도와드리겠습니다.");
//    faqRepository.save(faq1);
//    faqRepository.save(faq2);
//    faqRepository.save(faq3);
//    // -----------------------------------
//
//    // -----------------------------------------------------오운완 초기데이터----------------------------
//
//    T_exercise t_exercise1 = new T_exercise("오늘등운동했습니다", "내용",
//            "https://cfnimage.commutil.kr/phpwas/restmb_allidxmake.php?pp=002&idx=3&simg=201908231123000034655469ec1315814021222.jpg&nmt=18",
//            member1);
//    T_exercise t_exercise2 = new T_exercise("오운완", "내용",
//            "https://scent.kisti.re.kr/site/main/file/image/8756", member2);
//    T_exercise t_exercise3 = new T_exercise("안녕하세요", "내용",
//            "https://mblogthumb-phinf.pstatic.net/20160507_57/clubswin_1462592272624dXmOO_JPEG/%BF%EE%B5%BF8.jpg?type=w2",
//            member4);
//    T_exercise t_exercise4 = new T_exercise("반갑습니다", "검색",
//            "https://mblogthumb-phinf.pstatic.net/20160507_271/clubswin_1462592270408ERwhX_JPEG/%BF%EE%B5%BF5.jpg?type=w2",
//            member3);
//    T_exercise t_exercise5 = new T_exercise("오늘 어깨 운동 했습니다", "내용",
//            "https://cdn.4flix.co.kr/data/file/gallery/3076623982_l51Yduam_deaed678afae4d43901c0163ab25685c21f8abdb.jpeg",
//            member5);
//    T_exercise t_exercise6 = new T_exercise("오.운.완!", "내용",
//            "https://i.pinimg.com/1200x/12/9b/a4/129ba49677dc1453ba16d1d244ad00ea.jpg", member1);
//    T_exercise t_exercise7 = new T_exercise("제목쓰기 어렵네요", "검색",
//            "https://cdn.clien.net/web/api/file/F01/10934363/6e707fbe69cf2b.jpg?w=850&h=30000",
//            member4);
//    T_exercise t_exercise8 = new T_exercise("이지합니다", "내용",
//            "https://a-static.besthdwallpaper.com/you-can-have-muscles-like-this-by-working-out-weights-wallpaper-2560x1600-92007_7.jpg",
//            member2);
//    T_exercise t_exercise9 = new T_exercise("안녕하세요~~~~~~", "검",
//            "https://mblogthumb-phinf.pstatic.net/20150722_180/zpaxm20_1437550649646j1LoB_JPEG/1423579188602.jpg?type=w2",
//            member3);
//    T_exercise t_exercise10 = new T_exercise("오늘운동끝~~~", "내용",
//            "https://www.thedailypost.kr/news/photo/202111/84872_80385_2547.png", member4);
//
//    T_exercise t_exercise11 = new T_exercise("오늘등운동했습니다", "내용",
//            "https://cfnimage.commutil.kr/phpwas/restmb_allidxmake.php?pp=002&idx=3&simg=201908231123000034655469ec1315814021222.jpg&nmt=18",
//            member1);
//    T_exercise t_exercise12 = new T_exercise("오운완", "내용",
//            "https://scent.kisti.re.kr/site/main/file/image/8756", member2);
//    T_exercise t_exercise13 = new T_exercise("안녕하세요", "내용",
//            "https://mblogthumb-phinf.pstatic.net/20160507_57/clubswin_1462592272624dXmOO_JPEG/%BF%EE%B5%BF8.jpg?type=w2",
//            member4);
//    T_exercise t_exercise14 = new T_exercise("반갑습니다", "검색",
//            "https://mblogthumb-phinf.pstatic.net/20160507_271/clubswin_1462592270408ERwhX_JPEG/%BF%EE%B5%BF5.jpg?type=w2",
//            member3);
//    T_exercise t_exercise15 = new T_exercise("오늘 어깨 운동 했습니다", "내용",
//            "https://cdn.4flix.co.kr/data/file/gallery/3076623982_l51Yduam_deaed678afae4d43901c0163ab25685c21f8abdb.jpeg",
//            member5);
//    T_exercise t_exercise16 = new T_exercise("오.운.완!", "내용",
//            "https://i.pinimg.com/1200x/12/9b/a4/129ba49677dc1453ba16d1d244ad00ea.jpg", member1);
//    T_exercise t_exercise17 = new T_exercise("제목쓰기 어렵네요", "검색",
//            "https://cdn.clien.net/web/api/file/F01/10934363/6e707fbe69cf2b.jpg?w=850&h=30000",
//            member4);
//    T_exercise t_exercise18 = new T_exercise("이지합니다", "내용",
//            "https://a-static.besthdwallpaper.com/you-can-have-muscles-like-this-by-working-out-weights-wallpaper-2560x1600-92007_7.jpg",
//            member2);
//    T_exercise t_exercise19 = new T_exercise("안녕하세요~~~~~~", "검",
//            "https://mblogthumb-phinf.pstatic.net/20150722_180/zpaxm20_1437550649646j1LoB_JPEG/1423579188602.jpg?type=w2",
//            member3);
//    T_exercise t_exercise20 = new T_exercise("오늘운동끝~~~", "내용",
//            "https://www.thedailypost.kr/news/photo/202111/84872_80385_2547.png", member4);
//
//    tExerciseRepository.save(t_exercise1);
//    tExerciseRepository.save(t_exercise2);
//    tExerciseRepository.save(t_exercise3);
//    tExerciseRepository.save(t_exercise4);
//    tExerciseRepository.save(t_exercise5);
//    tExerciseRepository.save(t_exercise6);
//    tExerciseRepository.save(t_exercise7);
//    tExerciseRepository.save(t_exercise8);
//    tExerciseRepository.save(t_exercise9);
//    tExerciseRepository.save(t_exercise10);
//    tExerciseRepository.save(t_exercise11);
//    tExerciseRepository.save(t_exercise12);
//    tExerciseRepository.save(t_exercise13);
//    tExerciseRepository.save(t_exercise14);
//    tExerciseRepository.save(t_exercise15);
//    tExerciseRepository.save(t_exercise16);
//    tExerciseRepository.save(t_exercise17);
//    tExerciseRepository.save(t_exercise18);
//    tExerciseRepository.save(t_exercise19);
//    tExerciseRepository.save(t_exercise20);
//
//    //오운완 좋아요 등록
//    for (int i = 0; i < 56; i++) {
//      T_exerciseLike like = new T_exerciseLike(member3.getUsername(), 5L);
//      tExerciseLikeRepository.save(like);
//    }
//
//    for (int i = 0; i < 20; i++) {
//      T_exerciseLike like = new T_exerciseLike(member5.getUsername(), 19L);
//      tExerciseLikeRepository.save(like);
//    }
//
//    for (int i = 0; i < 2; i++) {
//      T_exerciseLike like = new T_exerciseLike(member5.getUsername(), 1L);
//      tExerciseLikeRepository.save(like);
//    }
//    // 운동시설 등록
//
//    GymBoard gymBoard1 = GymBoard.GymBoard()
//            .title("[마포] 플라이번지 피트니스 - 다이어트와 근력운동, 바디밸런스까지 모두 잡을 수 있는 번지피트니스")
//            .content("안녕하세요,\n" +
//                    "\n" +
//                    "플라이번지 피트니스 입니다.\n" +
//                    "★MY OWN FITNESS★\n" +
//                    "\n" +
//                    "당신의 건강파트너!\n" +
//                    "\n" +
//                    "■ 번지코드의 탄성을 이용해 빠르고 정확하게 몸의 밸런스를 잡는 번지운동법\n" +
//                    "\n" +
//                    "■ 코어강화와 유산소를 동시에 할 수 있는 운동법")
//            .username("owner1")
//            .image("images\\img_5.jpg")
//            .gymName("[마포] 광흥창 플라이번지피트니스")
//            .ownerNumber("112A23B1")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000")
//            .region("서울 마포구 토정로 192, 진영빌딩 지하 1층")
//            .openTime("[평   일] 09:00 ~ 22:00\n" +
//                    "[토요일] 09:00 ~ 22:00\n" +
//                    "[일요일] 09:00 ~ 22:00\n" +
//                    "[휴무일] 공휴일")
//            .amenities("시설_1_2_3_4_5")
//            .amenitiesDetail("편의시설 이용 정보\n" +
//                    "무료\n" +
//                    "주차 3시간 / 수건 / 운동복\n" +
//                    "유료\n" +
//                    "라커 : 월 10,000원")
//            .build();
//
//    GymBoard gymBoard2 = GymBoard.GymBoard()
//            .title("[마포] 상암 지플렉스 온핏 스마트짐 - 상암동 최대규모 900평대 헬스장")
//            .content("안녕하세요,\n" +
//                    "\n" +
//                    "상암 지플렉스 온핏 스마트짐 입니다.\n" +
//                    "\n" +
//                    "★MY OWN FITNESS★\n" +
//                    "\n" +
//                    "당신의 건강파트너!\n" +
//                    "\n" +
//                    "■ 상암동 최대규모 900평대 헬스장 온핏 지플렉스\n" +
//                    "\n" +
//                    "■ 개인별 운동처방! 맞춤형 프로그램\n" +
//                    "\n" +
//                    "■ 과학적이고 체계적인 시스템")
//            .username("owner2")
//            .image("images\\img_4.jpg")
//            .gymName("피트니스탑")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000")
//            .ownerNumber("112A23C1")
//            .region("서울 마포구 매봉산로 31, 지하 2층")
//            .openTime("[평   일] 06:00 ~ 22:00\n" +
//                    "\n" +
//                    "[토요일] 07:00 ~ 19:00\n" +
//                    "\n" +"유료\n" +
////                        "운동복 : 월 11,000원 / 라커 : 월 11,000원
//                    "[일요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[휴무일] 매달 셋째주 일요일")
//            .amenities("시설_1_2_3_4_5_6")
//            .amenitiesDetail("무료\n" +
//                    "주차 3시간 / 수건\n" +
//                    "\n" +
//                    "")
//            .build();
//
//    GymBoard gymBoard3 = GymBoard.GymBoard()
//            .title("[마포] 엠베이스짐 - 상암동 1:1 PT 전문으로 하는 프라이빗샵")
//            .content("안녕하세요,\n" +
//                    "\n" +
//                    "상암 엠베이스짐 입니다.\n" +
//                    "\n" +
//                    "★MY OWN FITNESS★\n" +
//                    "\n" +
//                    "당신의 건강파트너!\n" +
//                    "\n" +
//                    "■ 1:1 P.T만을 전문으로 하는 프라이빗 샵")
//            .username("owner2")
//            .image("images\\img_1.jpg")
//            .gymName("[마포] 상암 엠베이스짐")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000")
//            .ownerNumber("112A23C1")
//            .region("서울 용산구 보광로 126")
//            .openTime("[평   일] 09:00 ~ 23:00\n" +
//                    "\n" +
//                    "[토요일] 09:00 ~ 18:00\n" +
//                    "\n" +
//                    "[일요일]\n" +
//                    "\n" +
//                    "[휴무일] 공휴일,매주 일요일")
//            .amenities("시설_1_2_3_4_5_6")
//            .amenitiesDetail("편의시설 이용 정보\n" +
//                    "무료\n" +
//                    "주차 3시간 / 수건 / 운동복\n" +
//                    "유료\n" +
//                    "라커 : 월 10,000원")
//            .build();
//
//    GymBoard gymBoard4 = GymBoard.GymBoard()
//            .title("[인천]피트니스를 연구하는~ 연구소 용마루점")
//            .content("안녕하세요," +
//                    "\n" +
//                    " 용마루점 입니다.\n" +
//                    "\n" +
//                    "★MY OWN FITNESS★\n" +
//                    "\n" +
//                    "당신의 건강파트너!\n" +
//                    "\n" +
//                    "■ 1:1 P.T만을 전문으로 하는 프라이빗 샵")
//            .username("owner2")
//            .image("images\\img_3.jpg")
//            .gymName("피트니스연구소 용마루점")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000")
//            .ownerNumber("112A23C1")
//            .region("인천 미추홀구 인주대로 68")
//            .openTime("[평   일] 06:00 ~ 22:00\n" +
//                    "\n" +
//                    "[토요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[일요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[휴무일] 매달 셋째주 일요일")
//            .amenities("시설_3_6")
//            .amenitiesDetail("편의시설 이용 정보\n" +
//                    "무료\n" +
//                    "주차 3시간 / 수건 / 운동복            \n" +
//                    "유료\n" +
//                    "라커 : 월 10,000원")
//            .build();
//
//    GymBoard gymBoard5 = GymBoard.GymBoard()
//            .title("명현만멀티짐")
//            .content("여러분 없는거좀 있지만 이번에 새로 개점한 피트니스에요" +
//                    "많이 찾아와주시기 바랍니다.~" +
//                    "게다가 이벤트도 개최중이니 많은참여 부탁드려요" +
//                    "모두 화이팅")
//            .username("owner2")
//            .image("images\\T_exe.jpg")
//            .gymName("명현만멀티짐")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000.트레이닝_1_21000_2_40000")
//            .ownerNumber("112A23C1")
//            .region("인천 연수구 앵고개로246번길 3")
//            .openTime("[평   일] 06:00 ~ 22:00\n" +
//                    "\n" +
//                    "[토요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[일요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[휴무일] 매달 셋째주 일요일")
//            .amenities("시설_1_3_6")
//            .amenitiesDetail("편의시설 이용 정보\n" +
//                    "무료\n" +
//                    "주차 3시간 / 수건 / 운동복            \n" +
//                    "유료\n" +
//                    "라커 : 월 10,000원")
//            .build();
//
//    GymBoard gymBoard6 = GymBoard.GymBoard()
//            .title("휩스휘트니스클럽")
//            .content("여러분 없는거좀 있지만 이번에 새로 개점한 피트니스에요" +
//                    "많이 찾아와주시기 바랍니다.~" +
//                    "게다가 이벤트도 개최중이니 많은참여 부탁드려요" +
//                    "모두 화이팅")
//            .username("owner2")
//            .image("images\\img_4.jpg")
//            .gymName("휩스휘트니스클럽")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000.트레이닝_1_21000_2_40000")
//            .ownerNumber("112A23C1")
//            .region("대전 유성구 문화원로 101")
//            .openTime("[평   일] 06:00 ~ 22:00\n" +
//                    "\n" +
//                    "[토요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[일요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[휴무일] 매달 셋째주 일요일")
//            .amenities("시설_1_2_3_4_5_6")
//            .amenitiesDetail("편의시설 이용 정보\n" +
//                    "무료\n" +
//                    "주차 3시간 / 수건 / 운동복            \n" +
//                    "유료\n" +
//                    "라커 : 월 10,000원")
//            .build();
//    GymBoard gymBoard7 = GymBoard.GymBoard()
//            .title("랩스 유성온천점")
//            .content("여러분 없는거좀 있지만 이번에 새로 개점한 피트니스에요" +
//                    "많이 찾아와주시기 바랍니다.~" +
//                    "게다가 이벤트도 개최중이니 많은참여 부탁드려요" +
//                    "모두 화이팅")
//            .username("owner2")
//            .image("images\\noImage.PNG")
//            .gymName("랩스 유성온천점")
//            .price("시설비용_1_21000_2_40000_4_80000_5_100000.트레이닝_1_21000_2_40000")
//            .ownerNumber("112A23C1")
//            .region("대전 유성구 계룡로52번길 11")
//            .openTime("[평   일] 06:00 ~ 22:00\n" +
//                    "\n" +
//                    "[토요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[일요일] 07:00 ~ 19:00\n" +
//                    "\n" +
//                    "[휴무일] 매달 셋째주 일요일")
//            .amenities("시설_1_2_3_4_6")
//            .amenitiesDetail("편의시설 이용 정보\n" +
//                    "무료\n" +
//                    "주차 3시간 / 수건 / 운동복            \n" +
//                    "유료\n" +
//                    "라커 : 월 10,000원")
//            .build();
//    gymBoardRepository.save(gymBoard3);
//    gymBoardRepository.save(gymBoard4);
//    gymBoardRepository.save(gymBoard5);
//    gymBoardRepository.save(gymBoard2);
//    gymBoardRepository.save(gymBoard1);
//    gymBoardRepository.save(gymBoard6);
//    gymBoardRepository.save(gymBoard7);
//    // ----------------
//
//
//    //운동시설 후기 등록
//    GymReview gymReview1 = GymReview.builder()
//            .gymId(1L)
//            .comment("여기 정말 별로에여")
//            .username("member2")
//            .rating(1L)
//            .build();
//
//    GymReview gymReview2 = GymReview.builder()
//            .gymId(2L)
//            .comment("여기 정말 트레이너분도 친절하고 좋네요")
//            .username("member2")
//            .rating(5L)
//            .build();
//
//    GymReview gymReview3 = GymReview.builder()
//            .gymId(2L)
//            .comment("여기 정말좋아여 많이들 오세요")
//            .username("member1")
//            .rating(4L)
//            .build();
//
//    GymReview gymReview4 = GymReview.builder()
//            .gymId(2L)
//            .comment("여기 좋은가여 많이들 오지마세여 좋지 못해여")
//            .username("member2")
//            .rating(1L)
//            .build();
//
//    GymReview gymReview5 = GymReview.builder()
//            .gymId(2L)
//            .comment("그냥 평벙함? 그런느낌")
//            .username("owner1")
//            .rating(3L)
//            .build();
//    gymReviewRepository.save(gymReview1);
//    gymReviewRepository.save(gymReview2);
//    gymReviewRepository.save(gymReview3);
//    gymReviewRepository.save(gymReview4);
//    gymReviewRepository.save(gymReview5);

  }
}
