package com.nl.sprinterbe.domain.freespeech.dao;

import com.nl.sprinterbe.domain.freespeech.entity.FreeSpeech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreeSpeechRepository extends JpaRepository<FreeSpeech, Long> {

    List<FreeSpeech> findAllByProject_ProjectId(Long projectId);

}
