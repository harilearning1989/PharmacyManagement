package com.web.pharma.supplier.repos;

import com.web.pharma.supplier.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    @Query("""
                select c from Supplier c
                where (:q is null
                   or lower(c.name) like lower(concat('%', :q, '%'))
                   or lower(c.phone) like lower(concat('%', :q, '%'))
                   or lower(c.email) like lower(concat('%', :q, '%')))
            """)
    Page<Supplier> search(@Param("q") String q, Pageable pageable);
}
