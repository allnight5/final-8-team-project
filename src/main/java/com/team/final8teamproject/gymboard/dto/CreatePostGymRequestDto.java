package com.team.final8teamproject.gymboard.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class CreatePostGymRequestDto {
    private final String title;
    private final String contents;
    private final String ownerNumber;
    private final String gymName;
    private final String region;
    private final String openTime;
    private final String amenities;
    private final String amenitiesDetail;
    private final String price;
}
