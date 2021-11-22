package com.hanu.reddit.services;

import com.hanu.reddit.dto.LoginRequest;
import com.hanu.reddit.dto.RegisterRequest;
import com.hanu.reddit.exception.SpringRedditException;
import com.hanu.reddit.model.NotificationEmail;
import com.hanu.reddit.model.User;
import com.hanu.reddit.model.VerificationToken;
import com.hanu.reddit.repository.UserRepository;
import com.hanu.reddit.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void signup(RegisterRequest registerRequest) throws SpringRedditException {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);
        String token = generateVerificationToken(user);
        mailService.sendMail
                (new NotificationEmail(
                        "ACTIVATE YOUR ACCOUNT",
                        user.getEmail(),
                        "Hello you are sign up a new account. " +
                                "Click this link to activate your account: " +
                                "http://localhost:8080/api/auth/accountVerification/" + token));

    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        try {
            verificationToken.orElseThrow(() -> new SpringRedditException("Invalid Token"));
            fetchUserAndEnable(verificationToken.get());
        } catch (SpringRedditException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        String userName = verificationToken.getUser().getUsername();
        try {
            User user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new SpringRedditException("Not found user with name " + userName));
            user.setEnabled(true);
            userRepository.save(user);
        } catch (SpringRedditException e) {
            e.printStackTrace();
        }
    }

    public void login(LoginRequest loginRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUserName(),
                loginRequest.getPassword()));
    }
}
