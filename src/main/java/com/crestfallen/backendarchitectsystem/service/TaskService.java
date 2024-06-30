package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.Dto.TaskDTO;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskArgumentNotProvidedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskNotPresentException;
import com.crestfallen.backendarchitectsystem.model.Quest;
import com.crestfallen.backendarchitectsystem.model.Task;
import com.crestfallen.backendarchitectsystem.repository.QuestRepository;
import com.crestfallen.backendarchitectsystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {


    private final PlayerService playerService;
    private final TaskRepository taskRepository;
    private final QuestRepository questRepository;

    public Quest addTaskToQuest(String username, TaskDTO taskDTO) throws TaskArgumentNotProvidedException {

        Quest quest = playerService.getPlayerByUsername(username).getQuest();
        Task task = new Task();

        if (taskDTO.getDescription() == null) throw new TaskArgumentNotProvidedException("Description not provided");
        if (taskDTO.getCategory() == null) throw new TaskArgumentNotProvidedException("Category not provided");
        task.setDescription(taskDTO.getDescription());
        task.setCategory(taskDTO.getCategory());
        task.setQuest(quest);
        taskRepository.save(task);
        quest.addTask(task);
        questRepository.save(quest);
        return quest;
    }

    public Quest deleteTaskFromQuest(String username, TaskDTO taskDTO) throws TaskNotPresentException {
        Quest quest = playerService.getPlayerByUsername(username).getQuest();
        for (Task task : quest.getTasks()) {
            if (task.getDescription().equals(taskDTO.getDescription())) {
                quest.removeTask(task);
                questRepository.save(quest);
                taskRepository.delete(task);
                return quest;
            }
        }
        throw new TaskNotPresentException("Task not present in quest");
    }
}
