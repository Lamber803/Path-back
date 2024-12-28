package com.example.demo.controller;

import com.example.demo.model.dto.FlashcardDTO;
import com.example.demo.model.entity.Flashcard;
import com.example.demo.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flashcard")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    // 创建新的字卡
    @PostMapping("/create")
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody FlashcardDTO flashcardDTO) {
        try {
            Flashcard createdFlashcard = flashcardService.saveFlashcard(flashcardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFlashcard);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 获取某字卡组下的所有字卡
    @GetMapping("/group")
    public ResponseEntity<List<FlashcardDTO>> getFlashcardsByGroupId(@RequestParam Integer groupId) {
        List<FlashcardDTO> flashcards = flashcardService.getFlashcardsByGroupId(groupId);
        return ResponseEntity.ok(flashcards);
    }

    // 获取指定ID的字卡
    @GetMapping
    public ResponseEntity<FlashcardDTO> getFlashcardById(@RequestParam Integer flashcardId) {
        try {
            FlashcardDTO flashcardDTO = flashcardService.getFlashcardById(flashcardId);
            return ResponseEntity.ok(flashcardDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 将字卡添加到收藏
    @PostMapping("/add-to-favorites")
    public ResponseEntity<Void> addFlashcardToFavorites(@RequestParam Integer groupId, @RequestParam Integer flashcardId) {
        try {
            flashcardService.addFlashcardToFavorites(groupId, flashcardId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 获取某字卡组的所有收藏字卡
    @GetMapping("/favorites")
    public ResponseEntity<List<FlashcardDTO>> getFavoriteFlashcards(@RequestParam Integer groupId) {
        List<FlashcardDTO> favoriteFlashcards = flashcardService.getFavoriteFlashcards(groupId);
        return ResponseEntity.ok(favoriteFlashcards);
    }
}
