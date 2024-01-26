package com.encore.board.post.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.dto.PostUpDateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public List<PostListResDto> findAll(Pageable pageable){
        List<Post> posts = postRepository.findAllFetchJoin();
        List<PostListResDto> postListResDtos = new ArrayList<>();
        for(Post post : posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            postListResDto.setAuthor_email(post.getAuthor() == null? "익명유저" : post.getAuthor().getEmail());
            postListResDtos.add(postListResDto);
        }
        return postListResDtos;
    }

    public Page<PostListResDto> findAllPaging(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostListResDto> postListResDtos = posts.map(p-> new PostListResDto(p.getId(), p.getTitle(),p.getAuthor() == null? "익명유저" : p.getAuthor().getEmail()));
        return postListResDtos;
    }

    public Page<PostListResDto> findbyAppointment(Pageable pageable){
        Page<Post> posts = postRepository.findByAppointment(null, pageable);
        Page<PostListResDto> postListResDtos = posts.map(p-> new PostListResDto(p.getId(), p.getTitle(),p.getAuthor() == null? "익명유저" : p.getAuthor().getEmail()));
        return postListResDtos;
    }


    public void save(PostSaveReqDto postSaveReqDto) throws IllegalArgumentException{
//        Post post = new Post(postSaveReqDto.getTitle(), postSaveReqDto.getContents());
        Author author = authorRepository.findByEmail(postSaveReqDto.getEmail()).orElse(null);
        LocalDateTime localDateTime = null;
        String appointment = null;
        if(postSaveReqDto.getAppointment().equals("Y")&& !postSaveReqDto.getAppointmentTime().isEmpty()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            localDateTime = LocalDateTime.parse(postSaveReqDto.getAppointmentTime(), dateTimeFormatter);
            LocalDateTime now = LocalDateTime.now();
            if(localDateTime.isBefore(now)){
                throw new IllegalArgumentException("시간정보 잘못 입력");
            }
            appointment = "Y";
        }
        Post post = Post.builder()
                .title(postSaveReqDto.getTitle())
                .contents(postSaveReqDto.getContents())
                .author(author)
                .appointment(appointment)
                .appointmentTime(localDateTime)
                .build();
//        더티체킹 테스트
//        author.updateAuthor("dirty checking test", "1234");
        postRepository.save(post);
    }

    public Post findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(()->new EntityNotFoundException("검색하신 글이 없습니다."));
        return post;
    }

    public PostDetailResDto findPostDetail(Long id){
        Post post = this.findById(id);
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        postDetailResDto.setCreatedTime(post.getCreatedTime());
        return postDetailResDto;
    }

    public void postUpdate(Long id, PostUpDateReqDto postUpDateReqDto){
        Post post = this.findById(id);
        post.updatePost(postUpDateReqDto.getTitle(),postUpDateReqDto.getContents());
        postRepository.save(post);
    }

    public void postDelete(Long id){
        postRepository.deleteById(id);
    }
}
