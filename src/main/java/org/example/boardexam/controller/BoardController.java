package org.example.boardexam.controller;

import lombok.RequiredArgsConstructor;
import org.example.boardexam.domain.Board;
import org.example.boardexam.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("boards")
public class BoardController {
    private final BoardService boardService;

    //게시판 모두 조회(페이징)
    @GetMapping
    public String boards(Model model, @RequestParam(defaultValue = "1")int page,
                         @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Board> boards = boardService.findAllBoards(pageable);
        model.addAttribute("boards", boards);
        model.addAttribute("currentPage", page);
        return "boards/list";
    }

    //게시물 등록
    @GetMapping("/write")
    public String writeForm(Model model) {
        model.addAttribute("board", new Board());
        return "boards/writeform";
    }

    @PostMapping("/write")
    public String writeBoard(@ModelAttribute Board board, RedirectAttributes redirectAttributes) {
        boardService.saveBoard(board);
        redirectAttributes.addFlashAttribute("message", "게시물 등록 성공");
        return "redirect:/boards";
    }

    //상세페이지
    @GetMapping("/{id}")
    public String detailForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "boards/view";
    }

    //게시물 삭제
    @GetMapping("/delete/{id}")
    public String deleteForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "boards/deleteform";
    }

    @PostMapping("/delete/{id}")
    public String deleteBoard(@PathVariable Long id, @RequestParam String password, RedirectAttributes redirectAttributes) {
        boolean isDeleted = boardService.deleteBoardById(id, password);
        if (isDeleted) {
            redirectAttributes.addFlashAttribute("message", "게시물 삭제 성공");
            return "redirect:/boards";
        } else {
            redirectAttributes.addFlashAttribute("error", "비밀번호를 다시 확인해주세요.");
            return "redirect:/boards/delete/" + id;
        }
    }

    //게시물 수정
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "boards/updateform";
    }

    @PostMapping("/edit/{id}")
    public String editBoard(@PathVariable Long id, @RequestParam String password, @ModelAttribute Board board, RedirectAttributes redirectAttributes) {
        Board existingBoard = boardService.findBoardById(id);

        if (existingBoard != null && existingBoard.getPassword().equals(password)) {
            board.setId(id);
            boardService.saveBoard(board);
            redirectAttributes.addFlashAttribute("message", "게시물 수정 성공");
            return "redirect:/boards";
        } else {
            redirectAttributes.addFlashAttribute("error", "비밀번호를 다시 확인해주세요.");
            return "redirect:/boards/edit/" + id;
        }
    }
}
