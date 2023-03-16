package Main.controller;


import Main.model.User;
import Main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@ComponentScan(value = "Main.service")
public class UserController{
	private UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping(value = "/")
	public String printWelcome(ModelMap model) {
		model.addAttribute("users",userService.getAllUser());
		return "User/Users";
	}
	@PostMapping(value = "/del")
	public String delUser(@RequestParam int id, ModelMap model){
		userService.dell(id);
		model.addAttribute("users",userService.getAllUser());
		return "User/Users";
	}
	@PostMapping(value = "/add")
	public String addUser(@RequestParam String firstName, @RequestParam String lastName, ModelMap model){
		User user = new User(firstName, lastName);
		userService.add(user);
		model.addAttribute("users",userService.getAllUser());
		return "User/Users";
	}
	//Я не могу понять откуда у меня появляються запятые перед firstName & lastName при выполнении update&add
	@PostMapping(value = "/update")
	public String updateUser(@RequestParam String firstName,@RequestParam String lastName,@RequestParam int id, ModelMap model){
		User user = new User(firstName,lastName);
		user.setId((long) id);
		userService.update(user);

		model.addAttribute("users",userService.getAllUser());
		return "User/Users";
	}
	
}