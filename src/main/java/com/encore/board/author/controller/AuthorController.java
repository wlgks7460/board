package com.encore.board.author.controller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthorController {
    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService){
        this.authorService = authorService;
    }

    @GetMapping("author/list")
    public String authorList(Model model){
        model.addAttribute("authorList", authorService.authors());
        return "author/author-list";
    }

    @GetMapping("author/create")
    public String authorSaveScreen(){
        return "author/author-create";
    }

    @PostMapping("author/create")
    public String authorSave(Model model, AuthorSaveReqDto authorSaveReqDto){
        try {
            authorService.save(authorSaveReqDto);
            return "redirect:/author/list";
        }catch (IllegalArgumentException e){
            model.addAttribute("errorMessage",e.getMessage());
            return  "/author/author-create";

        }
    }
    @GetMapping("author/detail/{id}")
    public String authorDetail(@PathVariable Long id, Model model){
        model.addAttribute("author", authorService.findAuthorDetail(id));
        return "author/author-detail";
    }

    @PostMapping("author/{id}/update")
    public String memberUpdate(@PathVariable Long id , AuthorUpdateReqDto authorUpdateReqDto){
        authorService.authorUpdate(id, authorUpdateReqDto);
        return "redirect:/author/detail/"+id;
    }

    @GetMapping("author/delete/{id}")
    public String authorDelete(@PathVariable Long id){
        authorService.authorDelete(id);
        return "redirect:/author/list";
    }

//    엔티티 순환 참조 이슈 테스트
    @GetMapping("author/{id}/circle/issue")
    @ResponseBody
//    연관관계가 있는 Author엔티티를 json으로 직렬화를 하게될 경우
//    순환참조 이슈 발생하므로, dto 사용 필요
    public Author circleEntity(@PathVariable Long id){
        return authorService.findById(id);
    }

    @GetMapping("author/{id}/circle/dto")
    @ResponseBody
    public AuthorDetailResDto circleDto(@PathVariable Long id){
        return authorService.findAuthorDetail(id);
    }
}
