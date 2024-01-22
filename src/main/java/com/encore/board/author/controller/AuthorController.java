package com.encore.board.author.controller;

import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("author/save")
    public String authorSaveScreen(){
        return "author/author-save";
    }

    @PostMapping("author/save")
    public String authorSave(AuthorSaveReqDto authorSaveReqDto){
        authorService.save(authorSaveReqDto);
        return "redirect:/author-list";
    }


    @GetMapping("author/detail/{id}")
    public String authorDetail(@PathVariable Long id, Model model){
        model.addAttribute("author", authorService.findById(id));
        return "author/author-detail";
    }
}
