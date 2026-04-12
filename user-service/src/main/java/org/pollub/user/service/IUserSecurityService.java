package org.pollub.user.service;

import org.pollub.user.dto.ApiTextResponse;
import org.pollub.user.dto.ChangePasswordDto;
import org.pollub.user.dto.ResetPasswordRequestDto;
import org.pollub.user.dto.ResetPasswordResponseDto;
import org.pollub.user.model.User;

//Lab5 : ISP 1 Start
public interface IUserSecurityService {
    ApiTextResponse changePassword(String username, ChangePasswordDto passwordDto);
    User validateCredentials(String usernameOrEmail, String password);
    ResetPasswordResponseDto resetPassword(ResetPasswordRequestDto request);
}
//Lab5 : ISP 1 End
