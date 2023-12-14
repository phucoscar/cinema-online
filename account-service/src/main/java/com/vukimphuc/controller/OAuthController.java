package com.vukimphuc.controller;

import com.google.gson.Gson;
import com.phucvukimcore.base.Result;
import com.phucvukimcore.util.JsonUtil;
import com.vukimphuc.dto.request.RegisterDto;
import com.vukimphuc.dto.response.google.GoogleAccessTokenResponse;
import com.vukimphuc.dto.response.google.GoogleData;
import com.vukimphuc.entity.User;
import com.vukimphuc.service.UserService;
import com.vukimphuc.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/SSO")
public class OAuthController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/sign-in-google")
    public Result signInWithGoogle(@RequestParam("code") String code) throws UnsupportedEncodingException {

        // Gửi refreshToken để lấy accessToken từ Google OAuth API
        String tokenUrl = "https://oauth2.googleapis.com/token";
        String redirectUri = "http://localhost:8082/SSO/sign-in-google"; // Redirect URI đã cấu hình trên Google Developer Console

        String clientIdEncoded = URLEncoder.encode(clientId, String.valueOf(StandardCharsets.UTF_8));
        String clientSecretEncoded = URLEncoder.encode(clientSecret, String.valueOf(StandardCharsets.UTF_8));
        String redirectUriEncoded = URLEncoder.encode(redirectUri, String.valueOf(StandardCharsets.UTF_8));
        String codeEncoded = URLEncoder.encode(code, String.valueOf(StandardCharsets.UTF_8));

        String requestBody = "code=" + codeEncoded +
                "&client_id=" + clientIdEncoded +
                "&client_secret=" + clientSecretEncoded +
                "&redirect_uri=" + redirectUriEncoded +
                "&grant_type=authorization_code";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                tokenUrl, HttpMethod.POST, requestEntity, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String tokenStringResponse = responseEntity.getBody();
            GoogleAccessTokenResponse tokenResponse = JsonUtil.parseObject(tokenStringResponse, GoogleAccessTokenResponse.class);

           if (tokenResponse != null && tokenResponse.getAccess_token() != null) {

               // Những fields muốn get từ google
               String fields = "names,emailAddresses";

               // Call API để lấy thông tin người dùng
               String apiUrl = "https://people.googleapis.com/v1/people/me?personFields=" + fields;

               String userInfo = restTemplate.getForObject(apiUrl + "&access_token=" + tokenResponse.getAccess_token(), String.class);

               Gson gson = new Gson();
               GoogleData googleData = gson.fromJson(userInfo, GoogleData.class);
               String displayName = googleData.getNames().get(0).getDisplayName();
               String email = googleData.getEmailAddresses().get(0).getValue();

               User user = userService.findByEmail(email);
               if (user != null){
                   String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                   return Result.success("Success", userService.convertToLoginResp(user, token));
               } else {
                   RegisterDto dto = new RegisterDto();
                   dto.setEmail(email);
                   dto.setFullname(displayName);
                   return Result.success("Success", userService.loginByGoogle(dto));
               }

           }

        }
        return Result.fail("Có lỗi xảy ra trong quá trình đăng nhập");

    }
}
/* https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email%20https://www.googleapis.com/auth/userinfo.profile&redirect_uri=http://localhost:8082/SSO/sign-in-google&response_type=code&client_id=716722995140-f0oierl8k6kuvc1tvu40lgli1o84cbmd.apps.googleusercontent.com
* */