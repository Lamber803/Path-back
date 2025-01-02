package com.example.demo.controller;

import com.example.demo.model.dto.FlashcardDTO;
import com.example.demo.model.entity.Flashcard;
import com.example.demo.service.FlashcardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flashcard")
public class FlashcardController {

    @Autowired
    private FlashcardService flashcardService;

    // 創建新的字卡
    @PostMapping("/create")
    public ResponseEntity<Flashcard> createFlashcard(@RequestBody FlashcardDTO flashcardDTO) {
        try {
            Flashcard createdFlashcard = flashcardService.saveFlashcard(flashcardDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdFlashcard);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // 獲取某字卡組下的所有字卡
    @GetMapping("/group")
    public ResponseEntity<List<FlashcardDTO>> getFlashcardsByGroupId(@RequestParam Integer groupId) {
        List<FlashcardDTO> flashcards = flashcardService.getFlashcardsByGroupId(groupId);
        return ResponseEntity.ok(flashcards);
    }

    // 獲取指定ID的字卡
    @GetMapping
    public ResponseEntity<FlashcardDTO> getFlashcardById(@RequestParam Integer flashcardId) {
        try {
            FlashcardDTO flashcardDTO = flashcardService.getFlashcardById(flashcardId);
            return ResponseEntity.ok(flashcardDTO);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // 切換字卡的收藏狀態
    @PostMapping("/toggle-favorite")
    public ResponseEntity<Flashcard> toggleFavorite(@RequestParam Integer flashcardId, @RequestParam boolean isFavorite) {
        try {
            // 這裡直接傳遞 isFavorite 值，無需切換
            Flashcard updatedFlashcard = flashcardService.setFavorite(flashcardId, isFavorite);
            return ResponseEntity.ok(updatedFlashcard);  // 返回更新後的字卡
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

	// 刪除字卡
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFlashcard(@RequestParam Integer flashcardId) {
        try {
            flashcardService.deleteFlashcard(flashcardId);
            return ResponseEntity.ok("字卡刪除成功");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("字卡不存在");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("刪除字卡時發生錯誤");
        }
    }


    // 獲取某字卡組的所有收藏字卡（現在不需要了）
    // 這個方法可以移除，因為收藏邏輯已經集成到 `Flashcard` 內部的 `isFavorite` 屬性，並且可以直接從 `getFlashcardsByGroupId` 返回已標註為收藏的字卡。
    // 如果需要可以加上一個過濾，只返回 `isFavorite` 為 `true` 的字卡：
    @GetMapping("/favorites")
    public ResponseEntity<List<FlashcardDTO>> getFavoriteFlashcards(@RequestParam Integer groupId) {
        List<FlashcardDTO> favoriteFlashcards = flashcardService.getFlashcardsByGroupId(groupId).stream()
            .filter(flashcardDTO -> flashcardDTO.getIsFavorite() != null && flashcardDTO.getIsFavorite()) // 只過濾收藏的字卡
            .collect(Collectors.toList());

        return ResponseEntity.ok(favoriteFlashcards);
    }
}
