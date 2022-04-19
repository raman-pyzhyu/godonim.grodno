package com.godonim.repository;

import com.godonim.model.Label;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabelRepo extends CrudRepository<Label, Long> {
}
