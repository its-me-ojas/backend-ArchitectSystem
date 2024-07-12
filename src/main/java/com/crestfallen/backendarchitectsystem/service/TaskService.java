package com.crestfallen.backendarchitectsystem.service;

import com.crestfallen.backendarchitectsystem.Dto.TaskDTO;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskAlreadyCompletedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskArgumentNotProvidedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskNotPresentException;
import com.crestfallen.backendarchitectsystem.model.Attribute;
import com.crestfallen.backendarchitectsystem.model.Player;
import com.crestfallen.backendarchitectsystem.model.Quest;
import com.crestfallen.backendarchitectsystem.model.Task;
import com.crestfallen.backendarchitectsystem.repository.AttributeRepository;
import com.crestfallen.backendarchitectsystem.repository.QuestRepository;
import com.crestfallen.backendarchitectsystem.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final PlayerService playerService;
    private final TaskRepository taskRepository;
    private final QuestRepository questRepository;
    private final AttributeRepository attributeRepository;

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

    public List<Task> deleteAllTaskFromQuest(String username) {
        Quest quest = playerService.getPlayerByUsername(username).getQuest();
        List<Task> tasks = new ArrayList<>(quest.getTasks());
        for (Task task : tasks) {
            quest.removeTask(task);
            taskRepository.delete(task);
        }
        questRepository.save(quest);
        return quest.getTasks();
    }

    public Task getTaskByUsername(String username, TaskDTO taskDTO)
            throws TaskNotPresentException {
        Player player = playerService.getPlayerByUsername(username);
        List<Task> tasks = player.getQuest().getTasks();
        for (Task task : tasks) {
            if (task.getDescription().equals(taskDTO.getDescription()))
                return task;
        }
        throw new TaskNotPresentException("This task is not associated with this player");

    }

    public Task updateTask(String username, TaskDTO taskDTO)
            throws TaskNotPresentException, TaskAlreadyCompletedException {
        Player player = playerService.getPlayerByUsername(username);
        List<Task> tasks = player.getQuest().getTasks();
        for (Task task : tasks) {
            if (task.getDescription().equals(taskDTO.getDescription())) {
                if (task.getIsCompleted() == true)
                    throw new TaskAlreadyCompletedException("Task already completed");
                task.setIsCompleted(true);
                taskRepository.save(task);
                Attribute attributes = player.getAttributes();
                switch (task.getCategory()) {
                    case "strength":
                        attributes.setStrength(attributes.getStrength() + 1);
                        break;
                    case "intelligence":
                        attributes.setIntelligence(attributes.getIntelligence() + 1);
                        break;
                    case "agility":
                        attributes.setAgility(attributes.getAgility() + 1);
                        break;
                    case "stamina":
                        attributes.setStamina(attributes.getStamina() + 1);
                        break;
                }
                attributeRepository.save(attributes);
                return task;
            }
        }
        throw new TaskNotPresentException("Task not present");
    }

    @Scheduled(cron = "0 20 23 * * ?")
    public void resetTasks() {
        try {
            taskRepository.resetAllTasks();
            logger.info("All tasks reset to \"not completed\" successfully");
        } catch (Exception e) {
            logger.error("Error occurred while resetting tasks: ", e);
        }
    }

}
