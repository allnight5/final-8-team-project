package com.team.final8teamproject.board.dto;

import com.team.final8teamproject.board.entity.T_exercise;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class T_exerciseBoardResponseDTO {


    private final String title;

    private final String content;

    private final String image;

    private final LocalDateTime createdDate;



    public T_exerciseBoardResponseDTO(T_exercise t_exercise) {
        this.title = t_exercise.getTitle();
        this.content =t_exercise.getContent();
        this.image = t_exercise.getFilepath(); // 이게 image url 로 바뀌나?
        this.createdDate =t_exercise.getCreatedAt();
    }
}