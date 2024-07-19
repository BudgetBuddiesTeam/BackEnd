package com.bbteam.budgetbuddies.domain.comment.service;


import com.bbteam.budgetbuddies.domain.comment.converter.CommentConverter;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentRequestDto;
import com.bbteam.budgetbuddies.domain.comment.dto.CommentResponseDto;
import com.bbteam.budgetbuddies.domain.comment.entity.Comment;
import com.bbteam.budgetbuddies.domain.comment.repository.CommentRepository;
import com.bbteam.budgetbuddies.domain.discountinfo.entity.DiscountInfo;
import com.bbteam.budgetbuddies.domain.discountinfo.repository.DiscountInfoRepository;
import com.bbteam.budgetbuddies.domain.supportinfo.entity.SupportInfo;
import com.bbteam.budgetbuddies.domain.supportinfo.repository.SupportInfoRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// 임시로 유저는 service에서 찾아서 처리하는 로직으로 작성함
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final DiscountInfoRepository discountInfoRepository;
    private final SupportInfoRepository supportInfoRepository;

    @Override
    @Transactional
    public CommentResponseDto.SupportInfoSuccessDto saveSupportComment(Long userId, CommentRequestDto.SupportInfoCommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저 존재 x"));
        SupportInfo supportInfo = supportInfoRepository.findById(dto.getSupportInfoId()).orElseThrow(() -> new NoSuchElementException());

        Comment comment = CommentConverter.toSupportComment(dto, user, supportInfo);
        Comment savedComment = commentRepository.save(comment);

        return CommentConverter.toSupportInfoSuccessDto(savedComment);
    }



    @Override
    @Transactional
    public CommentResponseDto.DiscountInfoSuccessDto saveDiscountComment(Long userId, CommentRequestDto.DiscountInfoCommentDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("유저 존재 x"));
        DiscountInfo discountInfo = discountInfoRepository.findById(dto.getDiscountInfoId()).orElseThrow(() -> new NoSuchElementException());

        Comment comment = CommentConverter.toDiscountComment(dto, user, discountInfo);
        Comment savedComment = commentRepository.save(comment);

        return CommentConverter.toDiscountInfoSuccessDto(savedComment);
    }

    @Override
    public List<CommentResponseDto.DiscountInfoCommentDto> findByDiscountInfo(Long discountInfoId) {
        List<Comment> commentList = commentRepository.findByDiscountInfo(discountInfoId);

        HashMap<Long, Long> anonymousMapping = countAnonymousNumber(commentList);
        List<CommentResponseDto.DiscountInfoCommentDto> collect = commentList.stream()
                .map(comment -> CommentConverter.toDiscountInfoCommentDto(comment, anonymousMapping))
                .collect(Collectors.toList());
        return collect;

    }

    @Override
    public List<CommentResponseDto.SupportInfoCommentDto> findBySupportInfo(Long supportInfoId) {
        List<Comment> commentList = commentRepository.findBySupportInfo(supportInfoId);
        HashMap<Long, Long> anonymousMapping = countAnonymousNumber(commentList);
        List<CommentResponseDto.SupportInfoCommentDto> collect = commentList.stream()
                .map(comment -> CommentConverter.toSupportInfoCommentDto(comment, anonymousMapping))
                .collect(Collectors.toList());
        return collect;
    }

    private static HashMap<Long, Long> countAnonymousNumber(List<Comment> commentList) {
        HashMap<Long, Long> anonymousMapping = new HashMap<>();
        Long count = 1L;
        for (Comment comment : commentList) {
            Long id = comment.getUser().getId();
            if(!anonymousMapping.containsKey(id)){
                anonymousMapping.put(id, count);
                count++;
            }
        }
        return anonymousMapping;
    }

    @Transactional
    public void removeDiscountInfoComment(Long discountInfoId){
        DiscountInfo discountInfo = discountInfoRepository.findById(discountInfoId).orElseThrow(() -> new NoSuchElementException("No such Entity"));
        discountInfoRepository.delete(discountInfo);
        return;
    }

    @Transactional
    public void removeSupportInfoComment(Long supportInfoId){
        SupportInfo supportInfo = supportInfoRepository.findById(supportInfoId).orElseThrow(() -> new NoSuchElementException("No such Entity"));
        supportInfoRepository.delete(supportInfo);
        return;
    }


}
