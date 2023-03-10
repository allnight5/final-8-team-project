package com.team.final8teamproject.gymboard.entity;

import com.team.final8teamproject.gymboard.dto.CreatePostGymRequestDto;
import com.team.final8teamproject.gymboard.dto.GymUpdateRequestDto;
import com.team.final8teamproject.share.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class GymBoard extends Timestamped {
    /**
     * username 회원 로그인 아이디? or email로 통일할 가능성있음
     * ownerNumber 사업자 번호
     * title 제목
     * gymName 운동시설 이름
     * content 운동시설 소개글
     * convenientFacilities
     * phoneNumber 전화번호
     * region 주소
     * price 가격
     * rating 리뷰 점수
     * gymImage 이미지 주소 넣어두는거
     * amenities 편의시설 확인
     * 운동시설 리뷰, 가격, 트레이너는 다른 테이블에서 연결없이
     * 다 따로 읽어서 불러옴 
     */
    @Id
    @Column(name = "Gym_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String ownerNumber;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String gymName;
    @Column(nullable = false)
    private String content;
    private String convenientFacilities;
    private String phoneNumber;
    @Column(nullable = false)
    private String region;
    private String imageUrl;
    private String price;
    private String openTime;
    private String amenities;
    private String amenitiesDetail;
    private Long rating;
    private String discount;
    //나중에 트레이너 추가하기

    @Builder(builderMethodName = "GymBoard")
    public GymBoard(String title, String username, String gymName,String content,
                    String image, String ownerNumber, String region,
                    String openTime, String amenities, String amenitiesDetail,
                    String price) {
        this.title = title;
        this.ownerNumber= ownerNumber;
        this.gymName = gymName;
        this.username = username;
        this.content = content;
        this.imageUrl = image;
        this.region = region;
        this.openTime = openTime;
        this.amenitiesDetail = amenitiesDetail;
        this.amenities = amenities;
        this.price = price;
        this.rating =0L;
    }
    public GymBoard(String gymName, String address, String price, String discount, String username){
        this.title = "test";
        this.ownerNumber= "test";
        this.content = "test";
        this.gymName = gymName;
        this.region = address;
        this.price = price;
        this.discount = discount;
        this.username = username;

    }
    public void update(GymUpdateRequestDto gymUpdateRequestDto, String imageUrl){
         if(gymUpdateRequestDto.getTitle() != null) {
             this.title = gymUpdateRequestDto.getTitle();
         }
         if(gymUpdateRequestDto.getContent() != null) {
             this.content = gymUpdateRequestDto.getContent();
         }
         if(gymUpdateRequestDto.getRegion() != null) {
             this.region = gymUpdateRequestDto.getRegion();
         }
        if(gymUpdateRequestDto.getGymName() != null) {
            this.gymName = gymUpdateRequestDto.getGymName();
        }
        if(imageUrl != null) {
            this.imageUrl = imageUrl;
        }
        if(gymUpdateRequestDto.getOpenTime() != null) {
            this.openTime = gymUpdateRequestDto.getOpenTime();
        }
        if(gymUpdateRequestDto.getAmenitiesDetail() != null) {
            this.amenitiesDetail = gymUpdateRequestDto.getAmenitiesDetail();
        }
        if(gymUpdateRequestDto.getAmenities() != null) {
            this.amenities = gymUpdateRequestDto.getAmenities();
        }
    }
    public void discountUpdate(String discount){
        this.discount = this.discount + discount;
    }
    public void discountDelete(String discount){
        this.discount = discount;
    }
    public void priceUpdate(String price){
        this.price = price;
    }
    public void ratingUpdate(Long rating){
        this.rating = rating;
    }
}
