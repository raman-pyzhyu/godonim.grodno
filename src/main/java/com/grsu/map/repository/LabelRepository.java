package com.grsu.map.repository;

import com.grsu.map.domain.Label;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
