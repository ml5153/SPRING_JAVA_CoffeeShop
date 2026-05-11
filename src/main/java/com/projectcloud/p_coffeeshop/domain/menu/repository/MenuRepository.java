package com.projectcloud.p_coffeeshop.domain.menu.repository;

import com.projectcloud.p_coffeeshop.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
