package com.example.demo.service;

import com.example.demo.model.dto.FlashcardDTO;
import com.example.demo.model.dto.FlashcardGroupDTO;
import com.example.demo.model.entity.FlashcardGroup;
import com.example.demo.model.entity.User;
import com.example.demo.repository.FlashcardGroupRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlashcardGroupService {

    @Autowired
    private FlashcardGroupRepository flashcardGroupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlashcardService flashcardService;

    // 創建字卡組
    public FlashcardGroup createFlashcardGroup(FlashcardGroupDTO flashcardGroupDTO) {
        Optional<User> userOptional = userRepository.findById(flashcardGroupDTO.getUserId());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用戶不存在");
        }

        User user = userOptional.get();
        FlashcardGroup flashcardGroup = new FlashcardGroup();
        flashcardGroup.setGroupName(flashcardGroupDTO.getGroupName());
        flashcardGroup.setUser(user);
        
        return flashcardGroupRepository.save(flashcardGroup);
    }

    // 查詢某用戶的所有字卡組
    public List<FlashcardGroupDTO> getAllFlashcardGroupsByUserId(Integer userId) {
        List<FlashcardGroup> flashcardGroups = flashcardGroupRepository.findByUser_UserId(userId);
        return flashcardGroups.stream()
                .map(group -> new FlashcardGroupDTO(
                        group.getGroupId(),
                        group.getUser().getUserId(),
                        group.getGroupName(),
                        flashcardService.getFlashcardsByGroupId(group.getGroupId()),
                        flashcardService.getFavoriteFlashcards(group.getGroupId())
                ))
                .collect(Collectors.toList());
    }

    // 查詢字卡組
    public FlashcardGroupDTO getFlashcardGroupById(Integer groupId) {
        Optional<FlashcardGroup> flashcardGroupOptional = flashcardGroupRepository.findById(groupId);
        if (!flashcardGroupOptional.isPresent()) {
            throw new RuntimeException("字卡組不存在");
        }

        FlashcardGroup flashcardGroup = flashcardGroupOptional.get();
        return new FlashcardGroupDTO(
                flashcardGroup.getGroupId(),
                flashcardGroup.getUser().getUserId(),
                flashcardGroup.getGroupName(),
                flashcardService.getFlashcardsByGroupId(flashcardGroup.getGroupId()),
                flashcardService.getFavoriteFlashcards(flashcardGroup.getGroupId())
        );
    }
}
