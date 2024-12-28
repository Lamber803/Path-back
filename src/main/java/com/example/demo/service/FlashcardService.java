package com.example.demo.service;

import com.example.demo.model.dto.FlashcardDTO;
import com.example.demo.model.entity.Flashcard;
import com.example.demo.model.entity.FlashcardGroup;
import com.example.demo.model.entity.FlashcardGroupFavorite;
import com.example.demo.repository.FlashcardRepository;
import com.example.demo.repository.FlashcardGroupRepository;
import com.example.demo.repository.FlashcardGroupFavoriteRepository;
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

    @Autowired
    private FlashcardGroupFavoriteRepository flashcardGroupFavoriteRepository;

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

        return flashcardRepository.save(flashcard);
    }

    // 根據字卡組ID查詢所有字卡
    public List<FlashcardDTO> getFlashcardsByGroupId(Integer groupId) {
        List<Flashcard> flashcards = flashcardRepository.findByFlashcardGroup_GroupId(groupId);
        return flashcards.stream()
                .map(flashcard -> new FlashcardDTO(
                        flashcard.getFlashcardGroup().getGroupId(),
                        flashcard.getWord(),
                        flashcard.getDefinition()
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
        return new FlashcardDTO(flashcard.getFlashcardId(),
                flashcard.getFlashcardGroup().getGroupId(),
                flashcard.getWord(),
                flashcard.getDefinition());
    }

    // 收藏字卡
    public void addFlashcardToFavorites(Integer groupId, Integer flashcardId) {
        if (flashcardGroupFavoriteRepository.existsByFlashcardGroup_GroupIdAndFlashcard_FlashcardId(groupId, flashcardId)) {
            throw new RuntimeException("字卡已經在收藏庫中");
        }

        Optional<FlashcardGroup> flashcardGroupOptional = flashcardGroupRepository.findById(groupId);
        if (!flashcardGroupOptional.isPresent()) {
            throw new RuntimeException("字卡組不存在");
        }

        Optional<Flashcard> flashcardOptional = flashcardRepository.findById(flashcardId);
        if (!flashcardOptional.isPresent()) {
            throw new RuntimeException("字卡不存在");
        }

        FlashcardGroup flashcardGroup = flashcardGroupOptional.get();
        Flashcard flashcard = flashcardOptional.get();

        FlashcardGroupFavorite favorite = new FlashcardGroupFavorite();
        favorite.setFlashcardGroup(flashcardGroup);
        favorite.setFlashcard(flashcard);
        flashcardGroupFavoriteRepository.save(favorite);
    }

    // 查詢某字卡組的收藏字卡
    public List<FlashcardDTO> getFavoriteFlashcards(Integer groupId) {
        List<FlashcardGroupFavorite> favorites = flashcardGroupFavoriteRepository.findByFlashcardGroup_GroupId(groupId);
        return favorites.stream()
                .map(favorite -> new FlashcardDTO(
                        favorite.getFlashcard().getFlashcardId(),
                        favorite.getFlashcardGroup().getGroupId(),
                        favorite.getFlashcard().getWord(),
                        favorite.getFlashcard().getDefinition()
                ))
                .collect(Collectors.toList());
    }
}
