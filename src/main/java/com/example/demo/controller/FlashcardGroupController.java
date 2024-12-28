package com.example.demo.controller;

import com.example.demo.model.dto.FlashcardGroupDTO;
import com.example.demo.model.entity.FlashcardGroup;
import com.example.demo.service.FlashcardGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcard-group")
public class FlashcardGroupController {

    @Autowired
    private FlashcardGroupService flashcardGroupService;

    // 创建新的字卡组
    @PostMapping("/create")
    public ResponseEntity<FlashcardGroup> createFlashcardGroup(@RequestBody FlashcardGroupDTO flashcardGroupDTO) {
        try {
            FlashcardGroup createdGroup = flashcardGroupService.createFlashcardGroup(flashcardGroupDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 获取用户所有字卡组
    @GetMapping("/user")
    public ResponseEntity<List<FlashcardGroupDTO>> getAllFlashcardGroupsByUserId(@RequestParam Integer userId) {
        List<FlashcardGroupDTO> flashcardGroups = flashcardGroupService.getAllFlashcardGroupsByUserId(userId);
        return ResponseEntity.ok(flashcardGroups);
    }

    // 获取指定字卡组详细信息
    @GetMapping
    public ResponseEntity<FlashcardGroupDTO> getFlashcardGroupById(@RequestParam Integer groupId) {
        try {
            FlashcardGroupDTO flashcardGroupDTO = flashcardGroupService.getFlashcardGroupById(groupId);
            return ResponseEntity.ok(flashcardGroupDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
