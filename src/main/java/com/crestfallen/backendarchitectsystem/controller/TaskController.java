package com.crestfallen.backendarchitectsystem.controller;

import com.crestfallen.backendarchitectsystem.Dto.TaskDTO;
import com.crestfallen.backendarchitectsystem.exception.Player.UnauthorisedPlayerException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskAlreadyCompletedException;
import com.crestfallen.backendarchitectsystem.exception.Task.TaskNotPresentException;
import com.crestfallen.backendarchitectsystem.model.Task;
import com.crestfallen.backendarchitectsystem.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // /api/v1/task/{username}
    // retrieve a task by username
    @PostMapping("{username}")
    public ResponseEntity<Task> getTasksByUsername(
            @PathVariable String username,
            @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal UserDetails userDetails)
            throws TaskNotPresentException {
        String usernameFromToken = userDetails.getUsername();
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorised player");
        }
        Task task = taskService.getTaskByUsername(username, taskDTO);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // /api/v1/task/{username}/update-task
    // update a task ( completes the task )
    @PutMapping("{username}/update-task")
    public ResponseEntity<Task> updateTask(
            @PathVariable String username,
            @RequestBody TaskDTO taskDTO,
            @AuthenticationPrincipal UserDetails userDetails)
            throws TaskNotPresentException, TaskAlreadyCompletedException {
        String usernameFromToken = userDetails.getUsername();
        if (!username.equals(usernameFromToken)) {
            throw new UnauthorisedPlayerException("Unauthorized player");
        }
        Task task = taskService.updateTask(username, taskDTO);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    // exception handling

    @ExceptionHandler(UnauthorisedPlayerException.class)
    public ResponseEntity<String> handleUnauthorisedPlayerException(UnauthorisedPlayerException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TaskNotPresentException.class)
    public ResponseEntity<String> handleTaskNotPresentException(TaskNotPresentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskAlreadyCompletedException.class)
    public ResponseEntity<String> handleTaskAlreadyCompletedException(TaskAlreadyCompletedException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
