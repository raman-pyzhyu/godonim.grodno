package com.grsu.map.repository;

import com.grsu.map.domain.Label;
import com.grsu.map.domain.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LabelRepository extends JpaRepository<Label, Long> {

    @Query("select l from Label l where " +
            "(:#{#searchType.toString()} = 'UNKNOWN' or :searchType = l.type)" +
            "and (:search = '' or " +
            "(lower(l.name) like lower(concat('%', concat(:search, '%')))) " +
            "or (lower(l.description) like lower(concat('%', concat(:search, '%'))))" +
            "or (lower(l.street) like lower(concat('%', concat(:search, '%')))))")
    List<Label> getLabels(@Param("search") String search, @Param("searchType") Type searchType);
}
