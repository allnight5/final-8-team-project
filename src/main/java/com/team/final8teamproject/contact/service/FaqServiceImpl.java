package com.team.final8teamproject.contact.service;

import com.team.final8teamproject.contact.Repository.FaqRepository;
import com.team.final8teamproject.contact.dto.FaqRequest;
import com.team.final8teamproject.contact.dto.FaqResponse;
import com.team.final8teamproject.contact.dto.UpdateFaqRequest;
import com.team.final8teamproject.contact.entity.Faq;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

  private final FaqRepository faqRepository;

  @Override
  @Transactional
  public void saveFaq(@Valid FaqRequest faqRequest, Long managerId) {
    Faq faq = faqRequest.toEntity(managerId);
    faqRepository.save(faq);
  }

  //FAQ 전체 조회 (보기)
  @Override
  @Transactional(readOnly = true)
  public Result getFaqList(int page, int size, Direction direction, String properties) {
    Page<Faq> faqListPage = faqRepository.findAll(
        PageRequest.of(page - 1, size, direction, properties));
    int totalCount = (int) faqListPage.getTotalElements();
    if (faqListPage.isEmpty()) {
      throw new CustomException(ExceptionStatus.POST_IS_EMPTY);
    }

    List<FaqResponse> faqResponses = faqListPage.stream().map(FaqResponse::new)
        .collect(Collectors.toList());
    int countList = size;
    int countPage = 5;//todo 리팩토링때  10으로 변경예정
    int totalPage = totalCount / countList;
    if (totalCount % countList > 0) {
      totalPage++;
    }
    if (totalPage < page) {
      page = totalPage;
    }
    return new Result(page, totalCount, countPage, totalPage, faqResponses);
  }

  //FAQ 해당 글 조회 (보기,가져오기)
  @Override
  @Transactional(readOnly = true)
  public FaqResponse getSelectedFaq(Long id) {
    Faq faq = faqRepository.findById(id).orElseThrow(
        () -> new CustomException(ExceptionStatus.BOARD_NOT_EXIST)
    );
    return new FaqResponse(faq);
  }

  @Override
  @Transactional(readOnly = true)
  public Result searchByKeyword(String keyword, int page, int size,
      Direction direction, String properties) {
    String question = keyword;
    String answer = keyword;
    Page<Faq> faqListPage = faqRepository.findAllByQuestionContainingOrAnswerContaining(question,
        answer, PageRequest.of(page - 1, size, direction, properties));
    int totalCount = (int) faqListPage.getTotalElements();
    if (faqListPage.isEmpty()) {
      throw new CustomException(ExceptionStatus.POST_IS_EMPTY);
    }
    List<FaqResponse> faqResponses = faqListPage.stream().map(FaqResponse::new).toList();

    int countList = size;
    int countPage = 5;//todo 리팩토링때  10으로 변경예정
    int totalPage = totalCount / countList;
    if (totalCount % countList > 0) {
      totalPage++;
    }
    if (totalPage < page) {
      page = totalPage;
    }
    return new Result(page, totalCount, countPage, totalPage, faqResponses);
  }

  @Transactional
  @Override
  public void updateFaq(Long id, Long managerId, UpdateFaqRequest updateFaqRequest) {
    String question = updateFaqRequest.getQuestion();
    String answer = updateFaqRequest.getAnswer();

    Faq faq = faqRepository.findById(id).orElseThrow(
        () -> new CustomException(ExceptionStatus.BOARD_NOT_EXIST)
    );
    if (faq.getManagerId().equals(managerId)) {
      faq.update(question, answer);
      faqRepository.save(faq);
    } else {
      throw new CustomException(ExceptionStatus.WRONG_USER_T0_CONTACT);
    }
  }

  @Override
  @Transactional
  public void deleteFaq(Long id, Long managerId) {
    Faq faq = faqRepository.findById(id).orElseThrow(
        () -> new CustomException(ExceptionStatus.BOARD_NOT_EXIST)
    );
    if (faq.getManagerId().equals(managerId)) {
      faqRepository.delete(faq);
    } else {
      throw new CustomException(ExceptionStatus.WRONG_USER_T0_CONTACT);
    }
  }

  @Getter
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  public static class Result<T> {

    private int page;
    private int totalCount;
    private int countPage;
    private int totalPage;
    private T data;

    public Result(int totalCount, T data) {
      this.totalCount = totalCount;
      this.data = data;
    }

    public Result(int page, int totalCount, int countPage, int totalPage, T data) {
      this.page = page;
      this.totalCount = totalCount;
      this.countPage = countPage;
      this.totalPage = totalPage;
      this.data = data;
    }
  }
}



