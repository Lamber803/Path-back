
package com.example.demo.service;

import com.example.demo.exception.DocumentGroupNotFoundException;
import com.example.demo.model.dto.DocumentGroupDTO;
import com.example.demo.model.entity.Document;
import com.example.demo.model.entity.DocumentGroup;
import com.example.demo.model.entity.User;
import com.example.demo.repository.DocumentGroupRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentGroupService {

	@Autowired
	private DocumentGroupRepository documentGroupRepository;

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private DocumentService documentService;

	// 創建文檔組
	public DocumentGroup createDocumentGroup(DocumentGroupDTO documentGroupDTO) {
		Optional<User> userOptional = userRepository.findById(documentGroupDTO.getUserId());
		if (!userOptional.isPresent()) {
			throw new RuntimeException("用戶不存在");
		}

		User user = userOptional.get();
		DocumentGroup documentGroup = new DocumentGroup();
		documentGroup.setGroupName(documentGroupDTO.getGroupName());
		documentGroup.setUser(user);

		return documentGroupRepository.save(documentGroup);
	}

	// 查詢某用戶的所有文檔組
	public List<DocumentGroupDTO> getAllDocumentGroupsByUserId(Integer userId) {
		List<DocumentGroup> documentGroups = documentGroupRepository.findByUser_UserId(userId);
		return documentGroups.stream()
				.map(group -> new DocumentGroupDTO(group.getGroupId(), group.getUser().getUserId(),
						group.getGroupName(), documentService.getDocumentsByGroupId(group.getGroupId())))
				.collect(Collectors.toList());
	}
	
	// 查詢文檔組
    public DocumentGroupDTO getDocumentGroupById(Integer groupId) {
        Optional<DocumentGroup> documentGroupOptional = documentGroupRepository.findById(groupId);
        if (!documentGroupOptional.isPresent()) {
            throw new RuntimeException("字卡組不存在");
        }

        DocumentGroup documentGroup = documentGroupOptional.get();
        return new DocumentGroupDTO(
        		documentGroup.getGroupId(),
        		documentGroup.getUser().getUserId(),
        		documentGroup.getGroupName(),
        		documentService.getDocumentsByGroupId(documentGroup.getGroupId())
        );
    }

	// 刪除群組及其所有文檔
	public void deleteDocumentGroup(Integer groupId) {
		Optional<DocumentGroup> documentGroupOptional = documentGroupRepository.findById(groupId);
		if (!documentGroupOptional.isPresent()) {
			throw new DocumentGroupNotFoundException("文檔群組不存在");
		}

		DocumentGroup documentGroup = documentGroupOptional.get();

		// 刪除群組中的所有文檔
		List<Document> documents = documentRepository.findByDocumentGroup_GroupId(groupId);
		for (Document document : documents) {
			documentRepository.delete(document); // 刪除文檔
		}

		// 刪除文檔群組
		documentGroupRepository.delete(documentGroup);
	}
}
