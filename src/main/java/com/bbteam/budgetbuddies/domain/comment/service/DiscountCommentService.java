package com.bbteam.budgetbuddies.domain.comment.service;

import com.bbteam.budgetbuddies.domain.comment.converter.CommentConverter;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("discountCommentService")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiscountCommentService implements CommentService<CommentRequestDto.DiscountInfoCommentDto,
        CommentResponseDto.DiscountInfoCommentDto>{
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DiscountInfoRepository discountInfoRepository;

    @Override
    @Transactional
    public CommentResponseDto.DiscountInfoCommentDto saveComment(Long userId, CommentRequestDto.DiscountInfoCommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저 존재 x"));
        DiscountInfo info = discountInfoRepository.findById(dto.getDiscountInfoId()).orElseThrow(() -> new NoSuchElementException("정보 존재 x")); // dto에서 infoId를 추출하여 찾는 메서드
        int anonymousNumber = getAnonymousNumber(user, info);
        Comment comment = CommentConverter.toDiscountComment(dto, user, info, anonymousNumber);
        Comment savedComment = commentRepository.save(comment);

        return CommentConverter.toDiscountInfoCommentDto(savedComment);
    }

    private int getAnonymousNumber(User user, DiscountInfo info) {
        int anonymousNumber;
        Optional<Comment> foundComment = commentRepository.findTopByUserAndDiscountInfo(user, info);
        if (foundComment.isEmpty()) {
            anonymousNumber = info.addAndGetAnonymousNumber();
        } else {
            anonymousNumber = foundComment.get().getAnonymousNumber();
        }
        return anonymousNumber;
    }

    @Override
    public List<CommentResponseDto.DiscountInfoCommentDto> findByInfo(Long infoId) {
        List<Comment> commentList = commentRepository.findByDiscountInfo(infoId);
        List<CommentResponseDto.DiscountInfoCommentDto> collect = commentList.stream()
                .map(CommentConverter::toDiscountInfoCommentDto)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public Page<CommentResponseDto.DiscountInfoCommentDto> findByInfoWithPaging(Long infoId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByDiscountInfoWithPaging(infoId, pageable);
        Page<CommentResponseDto.DiscountInfoCommentDto> result = commentPage.map(CommentConverter::toDiscountInfoCommentDto);
        return result;
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("No such id"));
        commentRepository.delete(comment);
    }

    @Override
    public CommentResponseDto.DiscountInfoCommentDto findCommentOne(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("No such comment"));
        if(comment.getDiscountInfo() == null){
            throw new RuntimeException("DiscountInfo comment에 대한 요청이 아닙니다.");
        }
        return CommentConverter.toDiscountInfoCommentDto(comment);
    }

    @Override
    @Transactional
    public CommentResponseDto.DiscountInfoCommentDto modifyComment(CommentRequestDto.DiscountInfoCommentDto dto) {
        Comment comment = commentRepository.findById(dto.getDiscountInfoId()).orElseThrow(() -> new NoSuchElementException("xxx"));
        if (comment.getDiscountInfo() == null) {
            throw new RuntimeException("DiscountInfo comment에 대한 요청이 아닙니다.");
        }
        comment.modifyComment(dto.getContent());

        return CommentConverter.toDiscountInfoCommentDto(comment);
    }
}
