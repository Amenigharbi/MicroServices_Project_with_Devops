package com.example.springsecurity.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.example.springsecurity.models.*;
import com.example.springsecurity.payload.request.VerificationRequest;
import com.example.springsecurity.security.services.UserDetailsServiceImpl;
import com.example.springsecurity.services.Userservice;
import com.example.springsecurity.utils.EmailService;
import com.example.springsecurity.utils.SmsService;
import com.example.springsecurity.utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.springsecurity.exception.TokenRefreshException;
import com.example.springsecurity.payload.request.LoginRequest;
import com.example.springsecurity.payload.request.SignupRequest;
import com.example.springsecurity.payload.request.TokenRefreshRequest;
import com.example.springsecurity.payload.response.JwtResponse;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.payload.response.TokenRefreshResponse;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.jwt.JwtUtils;
import com.example.springsecurity.security.services.RefreshTokenService;
import com.example.springsecurity.security.services.UserDetailsImpl;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  Userservice userservice;
  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  private SmsService smsservice;

  @Autowired
  JwtUtils jwtUtils;
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  RefreshTokenService refreshTokenService;
  @Autowired
  private JavaMailSender emailSender ;
  @Autowired
  private EmailService emailService;
  @GetMapping("/all")
  public List<User> getAllusers() {
    return userRepository.findAll();
  }
/*
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (user.getConfirm()) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getImage(),
                roles));
      } else {
        throw new RuntimeException("User not confirmed");
      }
    } else {
      throw new RuntimeException("User not found");
    }
  }
*/
  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    Optional<User> u = userRepository.findByUsername(loginRequest.getUsername());
    if (u.get().getConfirm() == true) {
     /* u.get().setActivation(true);
      userRepository.save(u.get());*/
      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      String jwt = jwtUtils.generateJwtToken(userDetails);

      List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
      return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
              userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              userDetails.getImage(),
              roles));

    } else {
      throw new RuntimeException("user not confirmed");
    }
  }
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest,
                                        HttpServletRequest request) throws MessagingException {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),signUpRequest.getPhoneNumber(),
        encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {

          case "customer":
            Role customerRole = roleRepository.findByName(ERole.ROLE_TOURIST)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(customerRole);
            break;
            default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);
    //String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    //System.out.println(appUrl);

    //mail confirmation
    //user.setConfirm(false);
    String from ="admin@gmail.com" ;
    String to = signUpRequest.getEmail();
    MimeMessage message = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message);
    helper.setSubject("Complete Registration!");
    helper.setFrom(from);
    helper.setTo(to);
    //helper.setText("<HTML><body> <a href=\"http://localhost:8085/auth/confirm?email="
            //+signUpRequest.getEmail()+"\">VERIFY</a></body></HTML>",true);
    helper.setText("votre code de confirmation est :"+signUpRequest.getEmail());
    emailSender.send(message);

    return ResponseEntity.ok(new MessageResponse("User registered successfully! check your email for confirmation"));
  }




  @GetMapping ("/confirm")
  public ResponseApiObject confirmEmail(@RequestParam String email) {

    ResponseApiObject responseApiObject= new ResponseApiObject();

    try {
      User user = userRepository.findByEmail(email);
      if (user != null) {
        user.setConfirm(true);
        responseApiObject.setMessage("user confirmed ");
        responseApiObject.setData(userRepository.save(user));
        responseApiObject.setStatus(200);
      }
    }catch (Exception e){
      responseApiObject.setStatus(406);
      responseApiObject.setMessage("something wrong");
    }

    return responseApiObject;


  }

  @GetMapping("/confirrm")
  public ResponseEntity<?> confirmuser(@RequestParam String email){
    User user = userRepository.findFirstByEmail(email);
      if (user != null) {
        user.setConfirm(true);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User confirmed"));
      }
    else{
        return ResponseEntity.ok(new MessageResponse("Error"));
  }
  }
//Desactivation un compte

  /*@PutMapping("/{userId}")
  public ResponseEntity<?> disableAccount(@PathVariable Long userId) {
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
      user.setActive(false);
      userRepository.save(user);
      return ResponseEntity.ok(new MessageResponse("Account desactivated "));
    } else{
      return ResponseEntity.ok(new MessageResponse("Error"));
    }
  }*/

/// desactivation account  ////////
  @PutMapping("/{userId}")
  public ResponseEntity<?> disableAccount(@PathVariable Long userId, @RequestParam("password") String password) {
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
      // Vérifier le mot de passe fourni
      if (encoder.matches(password, user.getPassword())) {
        user.setActive(false);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("Account desactivated"));
      } else {
        return ResponseEntity.badRequest().body(new MessageResponse("Mot de passe incorrect"));
      }
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  //////////////////////////////////
  // confirm avec Envoyer un e-mail à USER
  @GetMapping("/confirmm")
  public ResponseEntity<?> confirmUserr(@RequestParam String email) {
    User user = userRepository.findFirstByEmail(email);
    if (user != null) {
      user.setConfirm(true);
      userRepository.save(user);

      // Envoyer un e-mail au tourist
      String message = "Votre compte a été confirmé.";
      String subject = "Confirmation de compte";
      emailService.sendEmail(user.getEmail(), subject, message);

      return ResponseEntity.ok(new MessageResponse("User confirmed"));
    } else {
      return ResponseEntity.ok(new MessageResponse("Error"));
    }
  }
  @GetMapping("/c")
  public HashMap<String,String> confirmu(@RequestParam String email){
    HashMap message = new HashMap();
    try {
      User user = userRepository.findByEmail(email);
      if (user != null)
        user.setConfirm(true);
        userRepository.save(user);
        message.put("etat", "user confirmed ");
        return message;

    } catch(Exception e){
      message.put("etat","Error");
      return  message;
    }
  }


  @PostMapping("/refreshtoken")
  public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtUtils.generateTokenFromUsername(user.getUsername());
          return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }
  
/*  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  }*/
@PostMapping("/signout")
public ResponseEntity<?> logoutUser() {
  Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

  if (principal instanceof UserDetailsImpl) {
    UserDetailsImpl userDetails = (UserDetailsImpl) principal;
    Long userId = userDetails.getId();
    refreshTokenService.deleteByUserId(userId);
    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
  } else {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new MessageResponse("401: UNAUTHORIZED"));
  }
}

  //forget password
  @PostMapping("/forgetpassword")
  public HashMap<String,String> resetPassword(String email) throws MessagingException {
    HashMap message = new HashMap();
    User userexisting = userRepository.findByEmail(email);
    if (userexisting == null) {
      message.put("user", "user not found");
      return message;
    }
    UUID token = UUID.randomUUID();
    userexisting.setPasswordResetToken(token.toString());
    userexisting.setId(userexisting.getId());
    String from ="chetouiiftikhar@gmail.com" ;
    String to = userexisting.getEmail();
    MimeMessage messagee = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(messagee);
    helper.setSubject("Complete Registration!");
    helper.setFrom(from);
    helper.setTo(to);
    helper.setText("<HTML><body> <a href=\"http://localhost:4200/reset-password/"+userexisting.getPasswordResetToken()+"\">Reset-now</a></body></HTML>",true);

    helper.setText("<HTML><body> <a href=\"http://localhost:4200/reset-password/"+userexisting.getPasswordResetToken()+
            "\">client Reset-now </a><br><a href=\"http://localhost:4300/reset-password/"
            +userexisting.getPasswordResetToken()+"\"> Admin Reset-now (port 4300)</a></body></HTML>",true);

  //  String serverPort = "4200";

//    String resetLink;
//    if ("4200".equals(serverPort)) {
//      resetLink = "http://localhost:4200/reset-password/" + userexisting.getPasswordResetToken();
//    } else {
//      resetLink = "http://localhost:4300/reset-password/" + userexisting.getPasswordResetToken();
//    }
//    helper.setText("<HTML><body> <a href=\"" + resetLink + "\">Reset-now</a></body></HTML>", true);

   // helper.setText("votre code est :   "+userexisting.getPasswordResetToken(),true);
    emailSender.send(messagee);
    userRepository.saveAndFlush(userexisting);
    message.put("user", "user found , check your email");
/*
   Mail mail = new Mail();
    mail.setContent("<HTML><body><a href=\"http://localhost:4200/resetpass/"+userexisting.getPasswordResetToken()+">Reset-now</a></body></HTML>");
    mail.setFrom("itgate@gmail.com");
    mail.setTo(userexisting.getEmail());
    mail.setSubject("Reset password");
    emailService.sendSimpleMessage(mail);
    userRepository.saveAndFlush(userexisting);
    message.put("user", "user found , check your email");
*/
    return message;

  }


  //reset password
  @PostMapping("/resetPassword/{passwordResetToken}")
  public HashMap<String,String> resetPassword(@PathVariable String passwordResetToken, String newPassword) {

    User userexisting = userRepository.findByPasswordResetToken(passwordResetToken);
    HashMap message = new HashMap();

    if (userexisting != null) {
      userexisting.setId(userexisting.getId());
      userexisting.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        userexisting.setPasswordResetToken(null);
      userRepository.save(userexisting);
      message.put("resetpassword", "proccesed");
      return message;

    } else {
      message.put("resetpassword", "failed");
      return message;

    }



  }
  @PostMapping("/changepassword")
  public HashMap<String,String> changePassword(String email, String oldPassword, String newPassword) {
    HashMap message = new HashMap();
    User user = userRepository.findByEmail(email);
    // Vérification que l'utilisateur existe et que son ancien mot de passe est correct
    if (user == null || !encoder.matches(oldPassword, user.getPassword())) {
      message.put("oops","somthing wrong");
      return message;
    }
    // Encodage du nouveau mot de passe et mise à jour de l'utilisateur en base de données
    String encodedPassword = encoder.encode(newPassword);
    user.setPassword(encodedPassword);
    userRepository.save(user);
    message.put("well", "your password chandes succssefly");
    return message;
  }

  @PutMapping("/changepass")
  public ResponseEntity<?> changePasswoord(String oldPassword,String newPassword,
                                            @RequestParam("token") String token) {
    // Vérifier si le token est valide et récupérer l'utilisateur correspondant
    String username = jwtUtils.getUserNameFromJwtToken(token);
    User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

    // Vérifier si l'ancien mot de passe est correct
    if (!encoder.matches(oldPassword, user.getPassword())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Incorrect old password"));
    }
    // Check if the new password is different from the old password
    if (newPassword.equals(oldPassword)) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: New password must be different from old password!"));
    }

    // Changer le mot de passe
    user.setPassword(encoder.encode(newPassword));
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
  }



  // Méthode pour générer un code de vérification aléatoire
  private String generateVerificationCode() {
    Random random = new Random();
    int code = 100000 + random.nextInt(900000);
    return String.valueOf(code);
  }











@Autowired
private StorageService storageService;
  @GetMapping("/files/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename) {
    Resource file = storageService.loadFile(filename);
    HttpHeaders headers = new HttpHeaders();
    Map<String, String> extensionToContentType = new HashMap<>();
    extensionToContentType.put("pdf", "application/pdf");
    extensionToContentType.put("jpg", "image/jpeg");
    extensionToContentType.put("jpeg", "image/jpeg");
    extensionToContentType.put("png", "image/png");
    // Obtenez l'extension du fichier à partir du nom de fichier
    String fileExtension = FilenameUtils.getExtension(filename);
    // Obtenez le type de contenu à partir de la correspondance
    String contentType = extensionToContentType.getOrDefault(fileExtension.toLowerCase(),
            MediaType.APPLICATION_OCTET_STREAM_VALUE);
    // Définissez le type de contenu dans les en-têtes de réponse
    headers.setContentType(MediaType.parseMediaType(contentType));
    return ResponseEntity.ok().headers(headers).body(file);
  }







































/* forget password par sms*/
@PostMapping("/forgetpasswordbysms")
public HashMap<String,String> forgetpassword(  String email) throws MessagingException {
  HashMap message = new HashMap();
  User userexisting = userRepository.findByEmail(email);
  if (userexisting == null) {
    message.put("user", "user not found");
    return message;
  }
  Random random = new Random();
  int code = 10000000 + random.nextInt(90000000);
  userexisting.setPasswordResetToken(String.valueOf(code));
  userexisting.setId(userexisting.getId());


  String phoneNumber = userexisting.getPhoneNumber(); // Numéro de téléphone de l'utilisateur
  String resetCode = userexisting.getPasswordResetToken();

  smsservice.sendSMS(phoneNumber, resetCode);
  String from ="admin@gmail.com" ;
  String to = userexisting.getEmail();
  MimeMessage messagee = emailSender.createMimeMessage();
  MimeMessageHelper helper = new MimeMessageHelper(messagee);
  helper.setSubject("Complete Registration!");
  helper.setFrom(from);
  helper.setTo(to);
  //  helper.setText("<HTML><body> <a href=\"http://localhost:4200/resetpass/"+userexisting.getPasswordResetToken()+"\">Reset-now</a></body></HTML>",true);
  helper.setText("votre code est :   "+userexisting.getPasswordResetToken(),true);
  emailSender.send(messagee);
  userRepository.saveAndFlush(userexisting);
  message.put("user", "user found , check your email");
/*
   Mail mail = new Mail();
    mail.setContent("<HTML><body><a href=\"http://localhost:4200/resetpass/"+userexisting.getPasswordResetToken()+">Reset-now</a></body></HTML>");
    mail.setFrom("itgate@gmail.com");
    mail.setTo(userexisting.getEmail());
    mail.setSubject("Reset password");
    emailService.sendSimpleMessage(mail);
    userRepository.saveAndFlush(userexisting);
    message.put("user", "user found , check your email");
*/
  return message;

}

/* login avec double verification */
@PostMapping("/loginsms")
public ResponseEntity<?> authenticateUsers(@Valid @RequestBody LoginRequest loginRequest) {
  Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());
  System.out.println("rest log :"+loginRequest.getUsername());
  System.out.println("rest saisie :"+userOptional.get().getPassword());
  System.out.println("rest  base :"+loginRequest.getPassword());
  System.out.println("rest  mdb :"+encoder.matches(loginRequest.getPassword(),userOptional.get().getPassword()) );
  if (userOptional.isPresent() && encoder.matches(loginRequest.getPassword(),userOptional.get().getPassword()) ) {
    if (userOptional.get().getConfirm()) {
      // Génération d'un code aléatoire à 6 chiffres
      String verificationCode = generateVerificationCode();

      // Envoi du code par SMS (utilisation de Twilio ou autre service)
      smsservice.sendSMS(userOptional.get().getPhoneNumber(), verificationCode);

      // Enregistrement du code dans la base de données (à des fins de vérification ultérieure)
      userOptional.get().setVerificationCode(verificationCode);
      userRepository.save(userOptional.get());

      // Rediriger vers une nouvelle méthode pour saisir le code SMS
      return ResponseEntity.ok("Veuillez saisir le code SMS pour continuer");

    } else {
      return ResponseEntity.badRequest().body("L'utilisateur n'est pas confirmé");
    }
  } else {
    return ResponseEntity.badRequest().body("Mot de passe incorrect ou utilisateur non trouvé");
  }
}

/*
  @PostMapping("/verify-sms")
  public ResponseEntity<?> verifySMSCode(@RequestBody VerificationRequest verificationRequest) {

    Optional<User> userOptional = userRepository.findByUsername(verificationRequest.getUsername());
    System.out.println("rest log :"+verificationRequest.getUsername());
    System.out.println("rest saisie :"+verificationRequest.getVerificationCode());
    System.out.println("rest  base :"+verificationRequest.getVerificationCode());
    System.out.println("rest  mdb :"+encoder.matches(userOptional.get().getVerificationCode(),verificationRequest.getVerificationCode()));
    if ( userOptional.isPresent()&& userOptional.get().getVerificationCode().equals(verificationRequest.getVerificationCode())) {

      // Le code SMS est correct, générer le JWT et les informations utilisateur
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(verificationRequest.getUsername(), userOptional.get().getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      String jwt = jwtUtils.generateJwtToken(userDetails);

      List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

      // Réinitialiser le code de vérification dans la base de données
      userOptional.get().setVerificationCode(null);
      userRepository.save(userOptional.get());

      return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
              userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              userDetails.getImage(),
              roles));
    } else {
      throw new RuntimeException("Code SMS incorrect");
    }
  }
*/

  @PostMapping("/verify-sms")
  public ResponseEntity<?> verifySMSCode( VerificationRequest code) {
    // Recherche de l'utilisateur correspondant au code de vérification
    Optional<User> userOptional = userRepository.findByVerificationCode(code.getVerificationCode());
 //   System.out.println("Nom d'utilisateur: " + userOptional.get().getPassword());
   // System.out.println("Email: " + userOptional.get().getUsername());
    if (userOptional.isPresent()) {
      System.out.println("rep boolean: " + userOptional.isPresent());
      User user = userOptional.get();
      if (user.getVerificationCode().equals(code.getVerificationCode())) {
        System.out.println("rep condition: " + user.getVerificationCode().equals(code.getVerificationCode()));
      // Le code SMS est correct, générer le JWT et les informations utilisateur
        String passwordFromDB =encoder.encode(userOptional.get().getPassword()) ;
        System.out.println("rep decodage: " + passwordFromDB);
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(userOptional.get().getUsername(), userOptional.get().getPassword()));

      SecurityContextHolder.getContext().setAuthentication(authentication);

      UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

      String jwt = jwtUtils.generateJwtToken(userDetails);

      List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());

      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
      return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
              userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              userDetails.getImage(),
              roles));

    } else {
      throw new RuntimeException("Code SMS incorrect ou expiré");
    }}
     else {
        throw new RuntimeException("Utilisateur non trouvé pour ce code de vérification");
      }
  }










}

