package event.reminder.reminder.controller;

import event.reminder.reminder.dto.AuthRequest;
import event.reminder.reminder.dto.AuthResponse;
import event.reminder.reminder.entity.AppUser;
import event.reminder.reminder.repository.UserRepository;
import event.reminder.reminder.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final String SECRET = "natarajan-secret-key-which-is-long-enough!";
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        var userOpt = userRepo.findByEmail(request.getUsername());
        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String accessToken = jwtUtil.generateToken(request.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(request.getUsername());

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");

        try {

            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            String username = claims.getSubject();
            String newAccessToken = jwtUtil.generateToken(username);

            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String register(@RequestBody AppUser user) {
        System.out.println("Registration called with " + user.getEmail());
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            return "email already taken";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        return "User registered successfully";
    }


}

