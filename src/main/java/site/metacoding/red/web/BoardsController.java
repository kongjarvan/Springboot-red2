package site.metacoding.red.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.Boards;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.boards.WriteDto;
import site.metacoding.red.web.dto.response.boards.MainDto;
import site.metacoding.red.web.dto.response.boards.PagingDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {

	private final HttpSession session;
	private final BoardsDao boardsDao;
	// @postmapping("/boards/{id}/delete")
	// @postmapping("/boards/{id}/update") -> 자바스크립트로 할게 아니라면 put delete는 post로 해야됨

	@PostMapping("/boards/{id}/delete")
	public String DeleteBoards(@PathVariable Integer id) {
		Boards boardsPS = boardsDao.findById(id);
		// 인증체크)
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "redirect:/loginForm";
		}
		
		// 권한체크 (principal.getId() == boardsPS.getUsersId)
		if (boardsPS == null) { // if는 비정상 logic을 필터링 하는 역할로 사용하는것이 좋다.			
			return "redirect:/boards/" + id;
		}
		
		if(principal.getId() != boardsPS.getUsersId()) {
			System.out.println("ㅁㄴㅇㄹ");
			return "redirect:/boards/" + id;
		}
		
		
		boardsDao.delete(id);
		return "redirect:/";
	}

	@PostMapping("/boards")
	public String WriteBoard(WriteDto writeDto) {
		// 1. 세션에 접근해서 세션값 확인, 그때 Users로 다운캐스팅 하고 키값은 principal로 함
		// 2. principal null인지 확인, null이면 loginForm 리다이렉션 함
		// 3. BoardsDao로 접근해서 insert 메서드 호출
		// 조건: dto를 entity로 변환해서 인수로 담아줌
		// 조건: entity에는 세션의 principal에 getId가 필요하다
		Users principal = (Users) session.getAttribute("principal");

		if (principal == null) {
			return "redirect:/loginForm";
		}

		boardsDao.insert(writeDto.toEntity(principal.getId()));
		return "redirect:/";
	}

	// http://localhost:8000/board/?page=
	@GetMapping({ "/", "/boards" })
	public String getBoardList(Model model, Integer page) { // 0->0, 1->10, 2->20
		if (page == null)
			page = 0; // 한줄짜리는 중괄호 필요 없음
		int startNum = page * 3;
		List<MainDto> boardsList = boardsDao.findAll(startNum);
		PagingDto paging = boardsDao.paging(page);

		final int blockCount = 5;
		int currentBlock = page / blockCount;
		int startPageNum = 1 + blockCount * currentBlock;
		int lastPageNum = 5 + blockCount * currentBlock;

		if (paging.getTotalPage() < lastPageNum) {
			lastPageNum = paging.getTotalPage();
		}

		paging.setBlockCount(blockCount);
		paging.setCurrentBlock(currentBlock);
		paging.setStartPageNum(startPageNum);
		paging.setLastPageNum(lastPageNum);

		model.addAttribute("boardsList", boardsList);
		model.addAttribute("paging", paging);
		return "boards/main";
	}

	@GetMapping("/boards/{id}")
	public String getBoardList(@PathVariable Integer id, Model model) {
		model.addAttribute("boards", boardsDao.findById(id));
		return "boards/detail";

	}

	@GetMapping("/boards/writeForm")
	public String writeForm() {
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "redirect:/loginForm";
		}

		return "boards/writeForm";

	}
}
