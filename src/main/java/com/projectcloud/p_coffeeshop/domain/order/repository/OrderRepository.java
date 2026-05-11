package com.projectcloud.p_coffeeshop.domain.order.repository;

import com.projectcloud.p_coffeeshop.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT o.menu.id, o.menu.name, o.menu.price, COUNT(o.id)
        FROM Order o
        WHERE o.orderedAt >= :from
        GROUP BY o.menu.id, o.menu.name, o.menu.price
        ORDER BY COUNT(o.id) DESC
        LIMIT 3
    """)
    List<Object[]> findTop3MenusByOrderCount(@Param("from") LocalDateTime from);
}
