package com.team.final8teamproject.gymboard.repository;

import com.team.final8teamproject.gymboard.entity.GymBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GymBoardRepository extends JpaRepository<GymBoard, Long> {
    List<GymBoard> findAllByUsername(String username);

    List<GymBoard> findAll();
    Page<GymBoard> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );
    Optional<GymBoard> findById(Long id);
    Optional<GymBoard> findByIdAndUsername(Long id, String username);
    Page<GymBoard> findByUsername(Pageable pageable, String username);

    Optional<GymBoard> findByUsername(String username);
    void deleteById(Long id);

}
