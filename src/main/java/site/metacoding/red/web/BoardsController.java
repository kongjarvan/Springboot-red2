package site.metacoding.red.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardsController {
	
	// @postmapping("/boards/{id}/delete")
	// @postmapping("/boards/{id}/update") -> 자바스크립트로 할게 아니라면 put delete는 post로 해야됨

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
		return "boards/writeForm";
	}
}
