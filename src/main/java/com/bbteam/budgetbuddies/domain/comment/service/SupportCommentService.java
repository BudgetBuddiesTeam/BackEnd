package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.converter.CommentConverter;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("supportCommentService")
@Transactional(readOnly = true)
public class SupportCommentService extends AbstractCommentService <CommentRequestDto.SupportInfoCommentRequestDto,
    CommentResponseDto.SupportInfoCommentResponseDto> {

    private final UserRepository userRepository;
    private final SupportInfoRepository supportInfoRepository;

    public SupportCommentService(CommentRepository commentRepository, UserRepository userRepository, SupportInfoRepository supportInfoRepository) {
        super(commentRepository);
        this.userRepository = userRepository;
        this.supportInfoRepository = supportInfoRepository;
    }

    @Override
    @Transactional
    public CommentResponseDto.SupportInfoCommentResponseDto saveComment(Long userId, CommentRequestDto.SupportInfoCommentRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저 존재 x"));
        SupportInfo info = supportInfoRepository.findById(dto.getSupportInfoId()).orElseThrow(() -> new NoSuchElementException("정보 존재 x")); // dto에서 infoId를 추출하여 찾는 메서드
        int anonymousNumber = getAnonymousNumber(user, info);
        Comment comment = CommentConverter.toSupportComment(dto, user, info, anonymousNumber);
        Comment savedComment = commentRepository.save(comment);

        return CommentConverter.toSupportInfoCommentDto(savedComment);
    }

    private int getAnonymousNumber(User user, SupportInfo info) {
        int anonymousNumber;
        Optional<Comment> foundComment = commentRepository.findTopByUserAndSupportInfo(user, info);
        if (foundComment.isEmpty()) {
            anonymousNumber = info.addAndGetAnonymousNumber();
        } else {
            anonymousNumber = foundComment.get().getAnonymousNumber();
        }
        return anonymousNumber;
    }

    @Override
    public List<CommentResponseDto.SupportInfoCommentResponseDto> findByInfo(Long infoId) {
        List<Comment> commentList = commentRepository.findBySupportInfo(infoId);
        List<CommentResponseDto.SupportInfoCommentResponseDto> collect = commentList.stream()
                .map(CommentConverter::toSupportInfoCommentDto)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Page<CommentResponseDto.SupportInfoCommentResponseDto> findByInfoWithPaging(Long infoId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findBySupportInfoWithPaging(infoId, pageable);
        Page<CommentResponseDto.SupportInfoCommentResponseDto> result = commentPage.map(CommentConverter::toSupportInfoCommentDto);
        return result;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("No such id"));
        commentRepository.delete(comment);
    }

    @Override
    public CommentResponseDto.SupportInfoCommentResponseDto findCommentOne(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("No such comment"));
        if(comment.getSupportInfo() == null){
            throw new RuntimeException("supportInfo comment에 대한 요청이 아닙니다.");
        }
        return CommentConverter.toSupportInfoCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto.SupportInfoCommentResponseDto modifyComment(CommentRequestDto.CommentModifyRequestDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentId()).orElseThrow(() -> new NoSuchElementException("xxx"));
        if (comment.getSupportInfo() == null) {
            throw new RuntimeException("supportInfo comment에 대한 요청이 아닙니다.");
        }
        comment.modifyComment(dto.getContent());

        return CommentConverter.toSupportInfoCommentDto(comment);
    }
}
