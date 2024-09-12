package com.bbteam.budgetbuddies.domain.notice.service;

import com.bbteam.budgetbuddies.domain.notice.converter.NoticeConverter;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeRequestDto;
import com.bbteam.budgetbuddies.domain.notice.dto.NoticeResponseDto;
import com.bbteam.budgetbuddies.domain.notice.entity.Notice;
import com.bbteam.budgetbuddies.domain.notice.repository.NoticeRepository;
import com.bbteam.budgetbuddies.domain.user.entity.User;
import com.bbteam.budgetbuddies.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    @Override
    @Transactional
    public NoticeResponseDto save(NoticeRequestDto dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("member x"));
        Notice notice = NoticeConverter.toEntity(dto, user);
        noticeRepository.save(notice);
        return NoticeConverter.toDto(notice);
    }

    @Override
    public NoticeResponseDto findOne(Long noticeId) {
        Notice notice = findNotice(noticeId);
        return NoticeConverter.toDto(notice);

    }

    @Override
    public List<NoticeResponseDto> findAll(Pageable pageable) {
        return noticeRepository.findAll(pageable).stream()
                .map(NoticeConverter::toDto)
                .toList();
    }

    @Override
    @Transactional
    public NoticeResponseDto update(Long noticeId, NoticeRequestDto dto) {
        Notice notice = findNotice(noticeId);
        notice.update(dto);
        return NoticeConverter.toDto(notice);
    }

    @Override
    @Transactional
    public void delete(Long noticeId) {
        Notice notice = findNotice(noticeId);
        noticeRepository.delete(notice);
    }
    private Notice findNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(() -> new NoSuchElementException("notice x"));
        return notice;
    }


}
