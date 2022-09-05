package site.metacoding.red.web;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.boards.WriteDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {
	
	private final HttpSession session;
	private final BoardsDao boardsDao;
	// @postmapping("/boards/{id}/delete")
	// @postmapping("/boards/{id}/update") -> 자바스크립트로 할게 아니라면 put delete는 post로 해야됨

	
	@PostMapping("/boards")
	public String WriteBoard(WriteDto writeDto) {
		// 1. 세션에 접근해서 세션값 확인, 그때 Users로 다운캐스팅 하고 키값은 principal로 함
		// 2. principal null인지 확인, null이면 loginForm 리다이렉션 함
		// 3. BoardsDao로 접근해서 insert 메서드 호출
		// 조건: dto를 entity로 변환해서 인수로 담아줌
		// 조건: entity에는 세션의 principal에 getId가 필요하다
		
		Users principal = (Users)session.getAttribute("principal");
		
		if(principal==null) {
			return "redirect:/loginForm";
		}
		
		boardsDao.insert(writeDto.toEntity(principal.getId()));
		return "redirect:/";
	}
	
	@GetMapping({"/","/boards"})
	public String getBoardList() {
		return "boards/main";
	}
	
	@GetMapping("/boards/{id}")
	public String getBoardList(@PathVariable Integer id) {
		return "boards/detail";
	}
	
	@GetMapping("/boards/writeForm")
	public String writeForm() {
		Users principal = (Users)session.getAttribute("principal");
		if(principal==null) {
			return "redirect:/loginForm";
		}
		
		return "boards/writeForm";
		
	}
}
