package com.example.barointern;

import com.example.barointern.common.encoder.PasswordEncoder;
import com.example.barointern.common.enums.UserRole;
import com.example.barointern.domain.admin.dto.request.ChangeUserRoleRequest;
import com.example.barointern.domain.auth.dto.request.SigninRequest;
import com.example.barointern.domain.auth.dto.request.SignupAdminRequest;
import com.example.barointern.domain.auth.dto.request.SignupUserRequest;
import com.example.barointern.domain.auth.service.AuthService;
import com.example.barointern.domain.user.entity.User;
import com.example.barointern.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    void 회원가입_성공_USER() throws Exception {
        // given
        SignupUserRequest request = new SignupUserRequest("example@example.com", "password1234");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("example@example.com"))
                .andExpect(jsonPath("$.userRole").value("USER"));
    }

    @Test
    void 회원가입_성공_ADMIN() throws Exception {
        // given
        SignupAdminRequest request = new SignupAdminRequest("admin@example.com", "password1234", "1q2w3e4r");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("admin@example.com"))
                .andExpect(jsonPath("$.userRole").value("ADMIN"));
    }

    @Test
    public void 이미_가입된_사용자_이메일로_회원가입시_실패_USER() throws Exception {
        // given
        User user = User.of(1L, "alreadyExistUser@example.com", "password1234", UserRole.ADMIN);
        userRepository.save(user);
        SignupUserRequest request = new SignupUserRequest("alreadyExistUser@example.com", "password1234");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 이메일은 이미 존재합니다."));
    }

    @Test
    public void 이미_가입된_사용자_이메일로_회원가입시_실패_ADMIN() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        User user = User.of(1L, "alreadyExistAdmin@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(user);
        SignupAdminRequest request = new SignupAdminRequest("alreadyExistAdmin@example.com", "password1234", "1q2w3e4r");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 이메일은 이미 존재합니다."));
    }

    @Test
    public void 관리자_회원가입시_검증_비밀번호가_다를_경우_실패() throws Exception {
        // given
        SignupAdminRequest request = new SignupAdminRequest("admin1@example.com", "password1234", "asdf");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("관리자 검증 비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void 잘못된_형식의_이메일로_회원가입시_실패() throws Exception {
        // given
        SignupUserRequest request = new SignupUserRequest("notemail", "password1234");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("이메일 형식에 맞지 않습니다."));
    }

    @Test
    public void 잘못된_형식의_비밀번호로_회원가입시_실패() throws Exception {
        // given
        SignupUserRequest request = new SignupUserRequest("user1@example.com", "1");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호 형식에 맞지 않습니다."));
    }

    @Test
    public void 로그인_성공() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        Long id = userRepository.generateId();
        User user = User.of(id, "admin2@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(user);
        SigninRequest request = new SigninRequest("admin2@example.com", "password1234");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(Matchers.startsWith("Bearer ")));
    }

    @Test
    public void 다른_비밀번호로_로그인시_실패() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        Long id = userRepository.generateId();
        User user = User.of(id, "user2@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(user);
        SigninRequest request = new SigninRequest("user2@example.com", "wrong1234");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("비밀번호가 일치하지 않습니다."));
    }

    @Test
    public void 존재하지_않는_이메일로_로그인시_실패() throws Exception {
        // given
        SigninRequest request = new SigninRequest("notexist@example.com", "password1234");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 이메일을 가진 유저가 존재하지 않습니다."));
    }

    @Test
    public void 역할_변경_성공() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        Long id = userRepository.generateId();
        User user = User.of(id, "user3@example.com", encodedPassword, UserRole.USER);
        userRepository.save(user);
        id = userRepository.generateId();
        User admin = User.of(id, "admin3@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(admin);
        String token = authService.signin("admin3@example.com", "password1234");
        ChangeUserRoleRequest request = new ChangeUserRoleRequest("user3@example.com", UserRole.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/v1/admin/roles")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user3@example.com"))
                .andExpect(jsonPath("$.userRole").value("ADMIN"));
    }

    @Test
    public void 관리자가_아닌_사용자가_역할_변경시_실패() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        Long id = userRepository.generateId();
        User user = User.of(id, "user4@example.com", encodedPassword, UserRole.USER);
        userRepository.save(user);
        id = userRepository.generateId();
        User user2 = User.of(id, "user5@example.com", encodedPassword, UserRole.USER);
        userRepository.save(user2);
        String token = authService.signin("user4@example.com", "password1234");
        ChangeUserRoleRequest request = new ChangeUserRoleRequest("user5@example.com", UserRole.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/v1/admin/roles")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("이 작업을 수행할 권한이 없습니다."));
    }

    @Test
    public void 존재하지_않는_사용자_역할_변경시_실패() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        Long id = userRepository.generateId();
        User user = User.of(id, "admin6@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(user);
        String token = authService.signin("admin6@example.com", "password1234");
        ChangeUserRoleRequest request = new ChangeUserRoleRequest("user10@example.com", UserRole.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/v1/admin/roles")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("해당 이메일을 가진 유저가 존재하지 않습니다."));
    }

    @Test
    public void 사용자_역할_변경시_이미_해당_역할일_경우_실패() throws Exception {
        // given
        String encodedPassword = passwordEncoder.encode("password1234");
        Long id = userRepository.generateId();
        User admin = User.of(id, "admin4@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(admin);
        id = userRepository.generateId();
        User admin2 = User.of(id, "admin5@example.com", encodedPassword, UserRole.ADMIN);
        userRepository.save(admin2);
        String token = authService.signin("admin4@example.com", "password1234");
        ChangeUserRoleRequest request = new ChangeUserRoleRequest("admin5@example.com", UserRole.ADMIN);

        // when & then
        mockMvc.perform(patch("/api/v1/admin/roles")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("해당 유저는 이미 해당 역할을 가지고 있습니다."));
    }
}