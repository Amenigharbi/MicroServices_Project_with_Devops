package com.example.springsecurity.controllers;

import com.example.springsecurity.clientConfig.CommentRestClient;
import com.example.springsecurity.clientConfig.ReservationRestClient;
import com.example.springsecurity.models.*;
import com.example.springsecurity.payload.request.SignupRequest;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.jwt.JwtUtils;
import com.example.springsecurity.security.services.RefreshTokenService;
import com.example.springsecurity.security.services.UserDetailsServiceImpl;
import com.example.springsecurity.services.TouristService;
import com.example.springsecurity.services.Userservice;
import com.example.springsecurity.utils.EmailService;
import com.example.springsecurity.utils.SmsService;
import com.example.springsecurity.utils.StorageService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;


@RestController

@RequestMapping("/tourists")
public class TouristController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private SmsService smsservice;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    public JavaMailSender emailSender ;
    final CommentRestClient commentRestClient;

    final AuthenticationManager authenticationManager;
    final UserRepository userRepository;


    final  Userservice userservice;

    final RoleRepository roleRepository;

   final JwtUtils jwtUtils;


    final  RefreshTokenService refreshTokenService;


   final EmailService emailService;

    final TouristService touristService;
    final StorageService storageService;
    final ReservationRestClient reservationRestClient;

    public TouristController(TouristService touristService, StorageService storageService,
                             ReservationRestClient reservationRestClient,
                             EmailService emailService,
                             CommentRestClient commentRestClient,
                             AuthenticationManager authenticationManager,
                             UserRepository userRepository,
                             Userservice userservice,
                             RoleRepository roleRepository,
                             JwtUtils jwtUtils,
                             RefreshTokenService refreshTokenService


                            ) {
        this.touristService = touristService;
        this.storageService = storageService;
        this.reservationRestClient = reservationRestClient;
        this.emailService = emailService;
        this.commentRestClient = commentRestClient;
        this.authenticationManager =  authenticationManager;
        this.userRepository = userRepository;
        this.userservice  = userservice;
        this.roleRepository  = roleRepository;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;


    }


    @PostMapping("/register")
    public ResponseEntity<Tourist> create(@RequestParam("file") MultipartFile file, Tourist tourist) {
        String filename = storageService.store(file);
        tourist.setImage(filename);
        Tourist createTourist = touristService.createTourist(tourist);
        return ResponseEntity.ok().body(createTourist);
    }


    @GetMapping("/all")
    public List<Tourist> AllTourist() {

        return touristService.findAllTourist();
    }


    @GetMapping("/getonet/{id}")
    public Tourist getoneById(@PathVariable Long id) {

        return touristService.findTouristById(id);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Tourist> UpdateAdmin(@PathVariable Long id, MultipartFile file, Tourist tourist) {
        String filename = storageService.store(file);
        tourist.setImage(filename);
        Tourist updateTourist = touristService.updateTourist(id, tourist);
        if (tourist == null) {
            return ResponseEntity.notFound().build();
        }
        tourist.setId(id);
        tourist.setImage(tourist.getImage());
        tourist.setAddresse(tourist.getAddresse());
        tourist.setPassport_num(tourist.getPassport_num() == 0 ? updateTourist.getPassport_num() : tourist.getPassport_num());
        tourist.setImage(tourist.getImage() == null ? updateTourist.getImage() : tourist.getImage());

        List<String> filenames = new ArrayList<>();

        storageService.store(file);

        Tourist tourist1 = touristService.createTourist(tourist);

        return ResponseEntity.ok().body(updateTourist);
    }


    @DeleteMapping("/deleteTRC/{touristId}")
    public HashMap<String,String>  deleteTourist_Com_Res(@PathVariable Long touristId){
        return touristService.deleteTouristAndRelatedData(touristId);
    }



    @GetMapping("/file/{filename:.+}")
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
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerTourist(@Valid  SignupRequest signUpRequest,@RequestParam("file") MultipartFile file, HttpServletRequest request) throws MessagingException, MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        Tourist tourist = new Tourist(signUpRequest.getUsername(), signUpRequest.getEmail(),signUpRequest.getPhoneNumber(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getAddresse());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

                        Role customerRole = roleRepository.findByName(ERole.ROLE_TOURIST)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(customerRole);

        String img = storageService.store(file);
        tourist.setImage(img);
        tourist.setRoles(roles);
        userRepository.save(tourist);
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
        helper.setText("<HTML><body> <a href=\"http://localhost:8084/auth/confirrm?email="
        +signUpRequest.getEmail()+"\">VERIFY</a></body></HTML>",true);
       // helper.setText("votre code de confirmation est :"+signUpRequest.getEmail());
        emailSender.send(message);

        return ResponseEntity.ok(new MessageResponse("Tourist registered successfully! check your email for confirmation"));
    }




}
