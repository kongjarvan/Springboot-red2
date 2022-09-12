package site.metacoding.red.web;


import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;
import site.metacoding.red.web.dto.request.users.UpdateDto;

@RequiredArgsConstructor
@Controller
public class UsersController {
	
	private final HttpSession session; // 스프링이 서버 시작시 IOC 컨테이너에 보관함
	private final UsersDao usersDao;
	
	
	@PostMapping("/users/{id}/delete")
	public String deleteUsers(@PathVariable Integer id) {
		Users usersPS = usersDao.findById(id);
		Users principal = (Users) session.getAttribute("principal");
		
		if (usersPS == null) {
			return "errors/badPage";
		}
		
		if(principal == null) {
			return "redirect:/loginForm";
			}
		
		if (principal.getId() != usersPS.getId()) {
			return "errors/badPage";
		}
		
		usersDao.delete(id);
		session.invalidate();
		return "redirect:/";
	}
	
	@PostMapping("/users/{id}/update")
	public String update(@PathVariable Integer id, UpdateDto updateDto) {
		Users usersPS = usersDao.findById(id);
		Users principal = (Users) session.getAttribute("principal");
		
		if (usersPS == null) {
			return "errors/badPage";
		}
		
		if(principal == null) {
			return "redirect:/loginForm";
			}
		
		if (principal.getId() != usersPS.getId()) {
			return "errors/badPage";
		}
		
		usersPS.유저수정(updateDto);		
		usersDao.update(usersPS);
		return "redirect:/";
	}
	
	@GetMapping("/users/{id}/updateForm") // 로그인만 예외로 select인데 post로 함 (보안상의 이유로 body에 담음)
	public String updateForm(@PathVariable Integer id, Model model) {
		Users usersPS = usersDao.findById(id);
		Users principal = (Users) session.getAttribute("principal");
		
		if (usersPS == null) {
			return "errors/badPage";
		}
		
		if(principal == null) { // 인증안됨
			return "redirect:/loginForm";
			}
		
		if (principal.getId() != usersPS.getId()) {
			return "errors/badPage";
		}
		
		model.addAttribute(usersPS);	
		return "users/updateForm";
		}
	
	
	
	@GetMapping("/logout")
	public String logout() {
		session.invalidate(); // 세션 영역에 있는 키값을 날림
		return "redirect:/";
	}
	
	
	@PostMapping("/login") // 로그인만 예외로 select인데 post로 함 (보안상의 이유로 body에 담음)
	public String login(LoginDto loginDto) {
		Users usersPS = usersDao.login(loginDto);
		if(usersPS != null) { // 인증됨
			session.setAttribute("principal", usersPS); // principal: 인증 된 유저
			return "redirect:/";
		}else { // 인증안됨
			return "redirect:/loginForm";
		}
	}
	
	@PostMapping("/join")
	public String join(JoinDto joinDto) {
		usersDao.insert(joinDto);
		return "redirect:/loginForm";
	}

	@GetMapping("/loginForm")
	public String loginForm() {
		return "users/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "users/joinForm";
	}
	

}
