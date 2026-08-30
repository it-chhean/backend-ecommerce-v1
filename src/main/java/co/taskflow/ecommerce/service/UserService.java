package co.taskflow.ecommerce.service;

import co.taskflow.ecommerce.dto.request.UserRequest;
import co.taskflow.ecommerce.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest request);

    UserResponse update(Long id , UserRequest request);

    UserResponse getById(Long id);

    List<UserResponse> getAll();

    void updateStatus(Long id);
}
