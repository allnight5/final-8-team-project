package com.team.final8teamproject.contact.Comment.entity;



import com.team.final8teamproject.user.entity.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**inquiry , user 연관관계  x  없이 구현
 *todo  세터 사용 하지않고 , 부보댓글 아이디 저장하는 방법
 */

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ContactComment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(nullable = false)
  private String comments;

  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private Long inquiryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private ContactComment parent;

  @OneToMany(mappedBy = "parent", orphanRemoval = true)
  private List<ContactComment> children = new ArrayList<>();

  @Builder
  public ContactComment(String comments, Long inquiryId, String username, ContactComment parent) {
    this.comments = comments;
    this.username = username;
    this.inquiryId = inquiryId;
    this.parent = parent;
  }

  public void update(String comments) {
    this.comments = comments;
  }

}



