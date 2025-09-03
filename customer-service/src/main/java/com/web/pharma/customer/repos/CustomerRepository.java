package com.web.pharma.customer.repos;

import com.web.pharma.customer.entities.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("""
        select c from Customer c
        where (:q is null
           or lower(c.name) like lower(concat('%', :q, '%'))
           or lower(c.phone) like lower(concat('%', :q, '%'))
           or lower(c.email) like lower(concat('%', :q, '%'))
           or cast(c.custId as string) like concat('%', :q, '%'))
    """)
    Page<Customer> search(@Param("q") String q, Pageable pageable);
}
