package com.org.design.repositories;

import com.org.design.entities.OriginalFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OriginalFileRepository extends JpaRepository<OriginalFile,Long> {
    // Flux<OriginalFile> findById(long id);
}