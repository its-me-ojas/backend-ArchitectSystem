package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.Dto.TaskDTO;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskArgumentNotProvidedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskNotPresentException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskStillPendingException;
import com.crestfallen.backendarchitectsystem.model.Attribute;
import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.model.Quest;
import com.crestfallen.backendarchitectsystem.model.Task;
import com.crestfallen.backendarchitectsystem.repository.QuestRepository;
import com.crestfallen.backendarchitectsystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestService {

    private final QuestRepository questRepository;
    private final PlayerService playerService;
    private final TaskRepository taskRepository;
    private final TaskService taskService;

    @GetMapping
    public Quest getQuestByUsername(String username) {
        return playerService.getPlayerByUsername(username).getQuest();
    }

    public Quest addTaskToQuest(String username, TaskDTO taskDTO)
            throws TaskArgumentNotProvidedException {
        return taskService.addTaskToQuest(username, taskDTO);
    }

    public Quest deleteTaskFromQuest(String username, TaskDTO taskDTO) throws TaskNotPresentException {
        return taskService.deleteTaskFromQuest(username, taskDTO);
    }

    public List<Task> deleteAllTaskFromQuest(String username) {
        return taskService.deleteAllTaskFromQuest(username);
    }

    public Attribute completeQuest(String username) {
        Quest quest = playerService.getPlayerByUsername(username).getQuest();
        List<Task> tasks = quest.getTasks();
        Boolean check = true;
        for (Task task : tasks) {
            if (!task.getIsCompleted()) {
                check = false;
            }
        }
        Player player = playerService.getPlayerByUsername(username);
        Attribute attributes = player.getAttributes();
        if (check) {
            attributes.setDiscipline(attributes.getDiscipline() + 1);
        } else {
            throw new TaskStillPendingException("Task still pending");
        }
        return attributes;
    }
}
