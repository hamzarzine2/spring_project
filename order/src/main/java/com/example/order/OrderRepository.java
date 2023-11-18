package com.example.order;

import com.example.order.Model.Order;
import com.example.order.Model.Side;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order,String> {
    List<Order> findAllByOwner(String username);

    @Query("from orders where ticker =:tickerParam AND side =:sideParam AND filled != quantity")
    List<Order> findNotDoneTicker(@Param("tickerParam") String tickerParam,@Param("sideParam") Side sideParam);}
