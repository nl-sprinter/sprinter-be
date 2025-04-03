package com.nl.sprinterbe.domain.freespeech.api;

import com.nl.sprinterbe.domain.freespeech.dao.FreeSpeechRepository;
import com.nl.sprinterbe.domain.freespeech.dto.FreeSpeechDto;
import com.nl.sprinterbe.domain.freespeech.entity.FreeSpeech;
import com.nl.sprinterbe.domain.project.dao.ProjectRepository;
import com.nl.sprinterbe.domain.project.entity.Project;
import com.nl.sprinterbe.global.exception.project.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FreeSpeechService {

    private final FreeSpeechRepository freeSpeechRepository;
    private final ProjectRepository projectRepository;

    public List<FreeSpeechDto> getFreeSpeechesByProjectId(Long projectId) {
        List<FreeSpeech> freeSpeechList = freeSpeechRepository.findAllByProject_ProjectId(projectId);
        return freeSpeechList.stream().map(freeSpeech -> FreeSpeechDto.of(freeSpeech) ).toList();
    }

    public void createFreeSpeech(Long projectId, String content) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException());
        FreeSpeech freeSpeech = new FreeSpeech();
        freeSpeech.setProject(project);
        freeSpeech.setContent(content);

        freeSpeechRepository.save(freeSpeech);
    }

    public void deleteFreeSpeech(Long postId) {
        freeSpeechRepository.deleteById(postId);
    }
}
