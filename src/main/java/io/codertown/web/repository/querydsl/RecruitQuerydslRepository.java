package io.codertown.web.repository.querydsl;

import io.codertown.web.entity.recruit.Recruit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitQuerydslRepository {

    Page<Recruit> findByType(String dType, Pageable pageable, String keyword, String loginId, String url);
}
