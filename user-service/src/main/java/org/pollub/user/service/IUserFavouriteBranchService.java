package org.pollub.user.service;
import org.pollub.common.dto.BranchDto;
import org.pollub.user.model.User;
public interface IUserFavouriteBranchService {
    User updateFavouriteBranch(String username, Long branchId);
    Long getFavouriteBranchId(Long userId);
    BranchDto getFavouriteBranch(Long userId);
}
