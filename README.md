# 게시판 프로젝트 [미니]

## 0524 백엔드 스쿨 10기 게시판 프로젝트 설명
기본적인 CRUD가 이루어지는 게시판을 Spring JDBC / Spring MVC를 이용하여 구현해보기

## 구현 기능
- 게시판 전체 목록 페이징 조회
- 게시글 등록
- 게시글 수정 (비밀번호 입력)
- 게시글 삭제 (비밀번호 입력)


## 구현 과정
### 비밀번호 일치/불일치
```html
<input type="password" id="password" name="password" required>
```

```java
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
```

```java
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
```

비밀번호 일치 여부를 isDeleted에 담아 해당 값을 통해 비밀번호가 일치하는지 일치하지 않는지 확인 후 삭제할 수 있도록 구현

### 게시글 작성일자/수정일자
```java
package org.example.boardexam.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Board {
    @Id
    private Long id;
    private String name;
    private String title;
    private String password;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
```
```html
<td th:if="${board.updated_at != null}" th:text="${#temporals.format(board.updated_at, 'yyyy/MM/dd')}"></td>
<td th:unless="${board.updated_at != null}" th:text="${#temporals.format(board.created_at, 'yyyy/MM/dd')}"></td>
```

작성일자와 수정일자를 LocalDateTime으로 불러온 후 실제 화면에서는 #temporals.format를 이용해 형식 포맷
