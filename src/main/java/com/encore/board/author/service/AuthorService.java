package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    AuthorService(AuthorRepository authorRepository, PasswordEncoder passwordEncoder){
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) throws IllegalArgumentException{
        if(authorRepository.findByEmail(authorSaveReqDto.getEmail()).isPresent()) throw new IllegalArgumentException("중복이메일");
        Author.Role role = null;
        if(authorSaveReqDto.getRole().equals("admin")){
            role = Author.Role.ADMIN;
        }else{
            role = Author.Role.USER;
        }
//        일반 생성자 방식
//        Author author = new Author(authorSaveReqDto.getName(), authorSaveReqDto.getEmail(), authorSaveReqDto.getPassword(), role);
//        빌더 패턴 방식
        Author author = Author.builder()
                .email(authorSaveReqDto.getEmail())
                .name(authorSaveReqDto.getName())
                .password(passwordEncoder.encode(authorSaveReqDto.getPassword()))
                .role(role)
                .build();

//        cascade.persist 테스트
//        부모테리블을 통해 자식 테이블에 객체 동시에 생성
//        List<Post> posts = new ArrayList<>();
//        Post post = Post.builder()
//                .title("등 장" + author.getName())
//                .contents("반갑습니다. cascade test is ...")
//                .author(author)
//                .build();
//        posts.add(post);
//        author.setPosts(posts);
        authorRepository.save(author);
    }

    public List<AuthorListResDto> authors(){
        List<Author> authors = authorRepository.findAll();
        List<AuthorListResDto> authorListResDtos = new ArrayList<>();
        for(Author author : authors){
            AuthorListResDto authorListResDto = new AuthorListResDto();
            authorListResDto.setId(author.getId());
            authorListResDto.setName(author.getName());
            authorListResDto.setEmail(author.getEmail());
            authorListResDtos.add(authorListResDto);
        }
        return authorListResDtos;
    }
    public AuthorDetailResDto findAuthorDetail(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(()->new EntityNotFoundException("검색하신 ID의 Member가 없습니다."));
        String role = null;
        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        if (author.getRole() == null || author.getRole().equals(Author.Role.USER)){
            authorDetailResDto.setRole("일반유저");
        }else{
            authorDetailResDto.setRole("관리자");
        }
        authorDetailResDto.setPostCounts(author.getPosts().size());
        return authorDetailResDto;
    }

    public Author findById(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(()->new EntityNotFoundException("검색하신 ID의 Member가 없습니다."));
        return author;
    }


    public void authorUpdate(Long id, AuthorUpdateReqDto authorUpdateReqDto){
        Author author = this.findById(id);
        author.updateAuthor(authorUpdateReqDto.getName(),authorUpdateReqDto.getPassword());
//        명시적으로 save를 하지 않더라도, jpa의 영속성 컨텍스트를 통해, 객체에 변경이 감지되면(dirtychecking)되면, 트랜잭션이 완료되는 시점에 save 동작
//        authorRepository.save(author);
    }

    public void authorDelete(Long id){
        authorRepository.deleteById(id);
    }


}
