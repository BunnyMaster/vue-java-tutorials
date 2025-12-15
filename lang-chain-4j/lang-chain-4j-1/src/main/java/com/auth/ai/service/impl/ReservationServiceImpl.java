package com.auth.ai.service.impl;

import com.auth.ai.mapper.ReservationMapper;
import com.auth.ai.model.entity.ReservationEntity;
import com.auth.ai.service.ReservationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, ReservationEntity> implements ReservationService {
}
