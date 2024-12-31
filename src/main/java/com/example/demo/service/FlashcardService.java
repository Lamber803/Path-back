package com.example.demo.service;

import com.example.demo.model.dto.FlashcardDTO;
import com.example.demo.model.entity.Flashcard;
import com.example.demo.model.entity.FlashcardGroup;
import com.example.demo.repository.FlashcardRepository;
import com.example.demo.repository.FlashcardGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlashcardService {

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private FlashcardGroupRepository flashcardGroupRepository;

    // 創建新的字卡
    public Flashcard saveFlashcard(FlashcardDTO flashcardDTO) {
        Optional<FlashcardGroup> flashcardGroupOptional = flashcardGroupRepository.findById(flashcardDTO.getGroupId());
        if (!flashcardGroupOptional.isPresent()) {
            throw new RuntimeException("字卡組不存在");
        }

        FlashcardGroup flashcardGroup = flashcardGroupOptional.get();
        Flashcard flashcard = new Flashcard();
        flashcard.setWord(flashcardDTO.getWord());
        flashcard.setDefinition(flashcardDTO.getDefinition());
        flashcard.setFlashcardGroup(flashcardGroup);
        flashcard.setIsFavorite(false);  // 新建字卡默認不為收藏

        return flashcardRepository.save(flashcard);
    }

    // 根據字卡組ID查詢所有字卡
    public List<FlashcardDTO> getFlashcardsByGroupId(Integer groupId) {
        List<Flashcard> flashcards = flashcardRepository.findByFlashcardGroup_GroupId(groupId);
        return flashcards.stream()
                .map(flashcard -> new FlashcardDTO(
                        flashcard.getFlashcardId(),  // 加入 flashcardId
                        flashcard.getFlashcardGroup().getGroupId(),
                        flashcard.getWord(),
                        flashcard.getDefinition(),
                        flashcard.getIsFavorite()  // 返回收藏狀態
                ))
                .collect(Collectors.toList());
    }


    // 根據字卡ID查詢字卡
    public FlashcardDTO getFlashcardById(Integer flashcardId) {
        Optional<Flashcard> flashcardOptional = flashcardRepository.findByFlashcardId(flashcardId);
        if (!flashcardOptional.isPresent()) {
            throw new RuntimeException("字卡不存在");
        }

        Flashcard flashcard = flashcardOptional.get();
        return new FlashcardDTO(
                flashcard.getFlashcardId(),
                flashcard.getFlashcardGroup().getGroupId(),
                flashcard.getWord(),
                flashcard.getDefinition(),
                flashcard.getIsFavorite());  // 返回字卡收藏狀態
    }

    // 切換字卡的收藏狀態
 // 切換字卡的收藏狀態
    public Flashcard setFavorite(Integer flashcardId, boolean isFavorite) {
        // 查找指定的字卡
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new RuntimeException("字卡不存在"));

        // 設置字卡的收藏狀態
        flashcard.setIsFavorite(isFavorite);

        // 保存並返回更新後的字卡
        return flashcardRepository.save(flashcard);
    }

    // 删除指定字卡
    public void deleteFlashcard(Integer flashcardId) {
        try {
            // 查找字卡
            Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);
            if (!flashcardOptional.isPresent()) {
                throw new RuntimeException("字卡不存在");
            }

            // 删除字卡
            Flashcard flashcard = flashcardOptional.get();
            flashcardRepository.delete(flashcard);
        } catch (Exception e) {
            System.err.println("刪除字卡時發生錯誤: " + e.getMessage());
            throw e;
        }
    }

}
