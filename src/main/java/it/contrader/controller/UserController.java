package it.contrader.controller;

import it.contrader.dto.RegistrationDTO;
import it.contrader.dto.RegistryDTO;
import it.contrader.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import it.contrader.dto.LoginDTO;
import it.contrader.dto.UserDTO;
import it.contrader.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import javax.validation.Valid;

import java.io.IOException;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController extends AbstractController<UserDTO>{
	
	@Autowired
	private UserService userService;private UserDTO RegistrationDTO;

	@PostMapping(value = "/findbyemailandpassword")
	public ResponseEntity<UserDTO> findbyemailandpassword(@RequestBody @Valid LoginDTO loginDTO) {
		return new ResponseEntity<>(userService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()), HttpStatus.OK);
	}

	@PostMapping(value = "/login")
	public ResponseEntity<UserDTO> login(@RequestBody @Valid LoginDTO loginDTO) {
		return new ResponseEntity<>(userService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword()), HttpStatus.OK);
	}

	@PostMapping(value = "/registration")
	public ResponseEntity<UserDTO> registration(@RequestBody @Valid RegistrationDTO registrationDTO){
		registrationDTO.setUsertype(User.Usertype.USER);
		return new ResponseEntity<>(userService.insert(registrationDTO),HttpStatus.OK);
	}

	@PutMapping(value = "/resetPassword")
	public ResponseEntity<UserDTO> resetPassword(@RequestBody @Valid RegistrationDTO registrationDTO) {
		userService.resetPassword(registrationDTO);
		return ResponseEntity.ok().build();
	}
	@PostMapping("/uploadProPic")
	public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file, @RequestParam("id") Long id) throws IOException {
		String uploadImage = userService.uploadImageToFileSystem(file, id);
		return ResponseEntity.status(HttpStatus.OK)
				.body(uploadImage);
	}

	@GetMapping("/viewProPic")
	public ResponseEntity<?> downloadImageFromFileSystem(@RequestParam Long id) throws IOException {
		byte[] imageData=userService.downloadImageFromFileSystem(id);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.valueOf("image/png"))
				.body(imageData);

	}



}