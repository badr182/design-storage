package com.org.design.repositories;

import com.org.design.entities.Chunks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunksRepository extends JpaRepository<Chunks, Long> {
}
