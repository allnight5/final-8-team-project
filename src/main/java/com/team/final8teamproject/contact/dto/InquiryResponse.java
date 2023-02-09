package com.team.final8teamproject.contact.dto;

import com.team.final8teamproject.contact.entity.Inquiry;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class InquiryResponse {
 private final String title;
 private final String content;
 private final String username;
  private final LocalDateTime createdDate;
  private final LocalDateTime modifiedDate;

  public InquiryResponse(Inquiry inquiry) {
    this.title = inquiry.getTitle();
    this.content = inquiry.getContent();
    this.username = inquiry.getUsername();
    this.createdDate = inquiry.getCreatedDate();
    this.modifiedDate = inquiry.getModifiedDate();
  }
}
