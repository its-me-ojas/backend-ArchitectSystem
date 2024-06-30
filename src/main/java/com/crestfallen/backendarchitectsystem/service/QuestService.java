package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.Dto.TaskDTO;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskArgumentNotProvidedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskNotPresentException;
import com.crestfallen.backendarchitectsystem.model.Quest;
import com.crestfallen.backendarchitectsystem.repository.QuestRepository;
import com.crestfallen.backendarchitectsystem.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class QuestService {

    @Autowired
    private QuestRepository questRepository;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskService taskService;

    @GetMapping
    public Quest getQuestByUsername(String username) {
        return playerService.getPlayerByUsername(username).getQuest();
    }

    public Quest addTaskToQuest(String username, TaskDTO taskDTO)
            throws TaskArgumentNotProvidedException {
        Quest quest = taskService.addTaskToQuest(username, taskDTO);
        return quest;
    }

    public Quest deleteTaskFromQuest(String username, TaskDTO taskDTO) throws TaskNotPresentException {
        Quest quest = taskService.deleteTaskFromQuest(username, taskDTO);
        return  quest;
    }
}
