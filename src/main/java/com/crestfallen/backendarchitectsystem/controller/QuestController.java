package com.crestfallen.backendarchitectsystem.controller;

import com.crestfallen.backendarchitectsystem.Dto.TaskDTO;
import com.crestfallen.backendarchitectsystem.exception.Player.UnauthorisedPlayerException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskArgumentNotProvidedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskNotPresentException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskStillPendingException;
import com.crestfallen.backendarchitectsystem.model.Attribute;
import com.crestfallen.backendarchitectsystem.model.Quest;
import com.crestfallen.backendarchitectsystem.model.Task;
import com.crestfallen.backendarchitectsystem.service.QuestService;
import com.crestfallen.backendarchitectsystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quest")
@RequiredArgsConstructor
public class QuestController {

    private final QuestService questService;
    private final TaskService taskService;

    // /api/v1/quest/{username}
    // retrieve a quest by username
    @GetMapping("{username}")
    public ResponseEntity<Quest> getQuestsByUsername(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        System.out.println("username: " + username);
        String usernameFromToken = userDetails.getUsername();
        System.out.println("usernameFromToken: " + usernameFromToken);
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorised player");
        }
        Quest quest = questService.getQuestByUsername(username);
        return new ResponseEntity<>(quest, HttpStatus.OK);
    }

    // /api/v1/quest/{username}/add-task
    // add a task to a quest
    @PostMapping("{username}/add-task")
    public ResponseEntity<Quest> addTaskToQuest(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody TaskDTO taskDTO) throws UnauthorisedPlayerException,
            TaskArgumentNotProvidedException {
        String usernameFromToken = userDetails.getUsername();
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorised player");
        }

        Quest quest = questService.addTaskToQuest(username, taskDTO);
        return new ResponseEntity<>(quest, HttpStatus.OK);
    }

    // /api/v1/quest/{username}/delete-task/
    // delete a task from a quest
    @DeleteMapping("{username}/delete-task")
    public ResponseEntity<Quest> deleteTaskFromQuest(
            @PathVariable String username,
            @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal UserDetails userDetails)
            throws UnauthorisedPlayerException, TaskNotPresentException {
        String usernameFromToken = userDetails.getUsername();
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorised player");
        }
        Quest quest = questService.deleteTaskFromQuest(username, taskDTO);
        return new ResponseEntity<>(quest, HttpStatus.OK);
    }

    // /api/v1/quest/{username}/clear-tasks
    // clear all tasks from a quest
    @DeleteMapping("{username}/clear-tasks")
    public ResponseEntity<List<Task>> deleteAllTasks(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        String usernameFromToken = userDetails.getUsername();
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorised player");
        }
        List<Task> tasks = questService.deleteAllTaskFromQuest(username);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // /api/v1/quest/{username}/complete
    // complete full quest
    @PutMapping("{username}/complete")
    public ResponseEntity<Attribute> completeQuest(
            @PathVariable String username,
            @AuthenticationPrincipal UserDetails userDetails) {
        String usernameFromToken = userDetails.getUsername();
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorised player");
        }
        Attribute attributes = questService.completeQuest(username);
        return new ResponseEntity<>(attributes, HttpStatus.OK);
    }

    // exception handler

    @ExceptionHandler(UnauthorisedPlayerException.class)
    public ResponseEntity<String> handleUnauthorisedPlayerException(UnauthorisedPlayerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TaskArgumentNotProvidedException.class)
    public ResponseEntity<String> handleTaskArgumentNotProvidedException(TaskArgumentNotProvidedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskNotPresentException.class)
    public ResponseEntity<String> handleTaskNotPresentException(TaskNotPresentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskStillPendingException.class)
    public ResponseEntity<String> handleTaskStillPendingException(TaskStillPendingException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
