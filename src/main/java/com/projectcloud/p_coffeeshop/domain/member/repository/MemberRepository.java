package com.projectcloud.p_coffeeshop.domain.member.repository;

import com.projectcloud.p_coffeeshop.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
}
