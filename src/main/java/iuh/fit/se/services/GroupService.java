package iuh.fit.se.services;

import iuh.fit.se.dtos.request.CreateGroupRequest;
import iuh.fit.se.dtos.request.UpdateGroupRequest;
import iuh.fit.se.dtos.response.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> getAllGroups();
    GroupResponse getGroupById(String groupId);
    GroupResponse createGroup(CreateGroupRequest request);
    GroupResponse updateGroup(String groupId, UpdateGroupRequest request);
}