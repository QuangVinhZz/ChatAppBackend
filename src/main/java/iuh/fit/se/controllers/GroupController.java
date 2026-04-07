package iuh.fit.se.controllers;

import iuh.fit.se.dtos.request.CreateGroupRequest;
import iuh.fit.se.dtos.request.UpdateGroupRequest;
import iuh.fit.se.dtos.response.ApiResponse;
import iuh.fit.se.dtos.response.GroupResponse;
import iuh.fit.se.entities.enums.HttpCode;
import iuh.fit.se.services.GroupService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat-management/api/v1/groups")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class GroupController {

    GroupService groupService;

    @GetMapping
    ApiResponse<List<GroupResponse>> getAllGroups() {
        return ApiResponse.<List<GroupResponse>>builder()
                .code(HttpCode.OK.getCODE())
                .message(HttpCode.OK.getMESSAGE())
                .data(groupService.getAllGroups())
                .build();
    }

    @GetMapping("/{groupId}")
    ApiResponse<GroupResponse> getGroupById(@PathVariable String groupId) {
        return ApiResponse.<GroupResponse>builder()
                .code(HttpCode.OK.getCODE())
                .message(HttpCode.OK.getMESSAGE())
                .data(groupService.getGroupById(groupId))
                .build();
    }

    @PostMapping
    ApiResponse<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        return ApiResponse.<GroupResponse>builder()
                .code(HttpCode.CREATED.getCODE())
                .message("Group created successfully")
                .data(groupService.createGroup(request))
                .build();
    }

    @PutMapping("/{groupId}")
    ApiResponse<GroupResponse> updateGroup(@PathVariable String groupId, @Valid @RequestBody UpdateGroupRequest request) {
        return ApiResponse.<GroupResponse>builder()
                .code(HttpCode.OK.getCODE())
                .message("Group updated successfully")
                .data(groupService.updateGroup(groupId, request))
                .build();
    }
}