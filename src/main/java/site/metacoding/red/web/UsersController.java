package site.metacoding.red.web;


import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.domain.users.UsersDao;
import site.metacoding.red.web.dto.request.users.JoinDto;
import site.metacoding.red.web.dto.request.users.LoginDto;

@RequiredArgsConstructor
@Controller
public class UsersController {
	
	private final HttpSession session; // 스프링이 서버 시작시 IOC 컨테이너에 보관함
	private final UsersDao usersDao;
	
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
