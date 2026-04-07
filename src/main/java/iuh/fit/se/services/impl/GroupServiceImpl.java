package iuh.fit.se.services.impl;

import iuh.fit.se.dtos.request.CreateGroupRequest;
import iuh.fit.se.dtos.request.UpdateGroupRequest;
import iuh.fit.se.dtos.response.GroupResponse;
import iuh.fit.se.entities.Conversation;
import iuh.fit.se.entities.Group;
import iuh.fit.se.entities.User;
import iuh.fit.se.entities.enums.ConversationType;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.exceptions.AppException;
import iuh.fit.se.mapper.GroupMapper;
import iuh.fit.se.repositories.ConversationRepository;
import iuh.fit.se.repositories.GroupRepository;
import iuh.fit.se.repositories.UserRepository;
import iuh.fit.se.services.GroupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GroupServiceImpl implements GroupService {

    GroupRepository groupRepository;
    UserRepository userRepository;
    ConversationRepository conversationRepository;
    GroupMapper groupMapper;

    @Override
    public List<GroupResponse> getAllGroups() {
        return groupRepository.findAll().stream()
                .map(groupMapper::toGroupResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GroupResponse getGroupById(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND));
        return groupMapper.toGroupResponse(group);
    }

    @Override
    public GroupResponse createGroup(CreateGroupRequest request) {
        // Get current user from security context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findUserByEmail(email);
        if (creator == null) {
            throw new AppException(HttpCode.NOT_FOUND);
        }

        Group group = groupMapper.toGroup(request);
        group.setCreatedBy(creator);
        group.setCreatedAt(LocalDateTime.now());

        // Set members
        if (request.getMemberIds() != null) {
            Set<User> members = request.getMemberIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND)))
                    .collect(Collectors.toSet());
            members.add(creator); // Creator is member
            group.setMembers(members);
        } else {
            group.setMembers(Set.of(creator));
        }

        // Set admins (creator is admin)
        group.setAdmins(Set.of(creator));

        // Create conversation for the group
        Conversation conversation = Conversation.builder()
                .type(ConversationType.GROUP)
                .name(group.getName())
                .participants(group.getMembers())
                .group(group)
                .build();
        conversation = conversationRepository.save(conversation);
        group.setConversation(conversation);

        group = groupRepository.save(group);
        return groupMapper.toGroupResponse(group);
    }

    @Override
    public GroupResponse updateGroup(String groupId, UpdateGroupRequest request) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND));

        groupMapper.updateGroupFromRequest(request, group);

        // Update members if provided
        if (request.getMemberIds() != null) {
            Set<User> members = request.getMemberIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND)))
                    .collect(Collectors.toSet());
            group.setMembers(members);
        }

        // Update admins if provided
        if (request.getAdminIds() != null) {
            Set<User> admins = request.getAdminIds().stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() -> new AppException(HttpCode.NOT_FOUND)))
                    .collect(Collectors.toSet());
            group.setAdmins(admins);
        }

        group = groupRepository.save(group);
        return groupMapper.toGroupResponse(group);
    }
}