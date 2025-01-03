package org.jonas.rolemate_backend.character.repository;

import org.jonas.rolemate_backend.character.model.entity.CharacterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {


    List<CharacterEntity> findByOwnerId(Long id);
}
