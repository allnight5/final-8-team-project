package com.team.final8teamproject.board.comment.entity;

import com.team.final8teamproject.board.comment.commentReply.entity.TodayMealCommentReply;
import com.team.final8teamproject.share.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodayMealComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String userNickname;
    private Long boardId;

    public TodayMealComment(String comment, String username, Long boardId,String userNickname) {
        this.comment = comment;
        this.username = username;
        this.boardId =boardId;
        this.userNickname = userNickname;
    }
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comments" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodayMealCommentReply> commentReplyList = new ArrayList<>();


    public boolean isWriter(String username) {
        return this.username.equals(username);
    }

    public void update(String commentContent) {
        this.comment = commentContent;
    }
}
