package org.example.boardexam.service;

import lombok.RequiredArgsConstructor;
import org.example.boardexam.domain.Board;
import org.example.boardexam.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.engine.IElementDefinitionsAware;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    //게시판 모두 조회(페이징)
    @Transactional(readOnly = true)
    public Page<Board> findAllBoards(Pageable pageable) {
        Pageable sortedByDescId = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "id"));
        return boardRepository.findAll(sortedByDescId);
    }

    //게시물 등록
    @Transactional
    public Board saveBoard(Board board) {
        if (board.getId() == null) {
            board.setCreated_at(LocalDateTime.now());
        }
        board.setUpdated_at(LocalDateTime.now());
        return boardRepository.save(board);
    }

    //상세페이지
    @Transactional(readOnly = true)
    public Board findBoardById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    //게시물 삭제
    @Transactional
    public boolean deleteBoardById(Long id, String password) {
        Board board = findBoardById(id);
        if (board != null && board.getPassword().equals(password)) {
            boardRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
