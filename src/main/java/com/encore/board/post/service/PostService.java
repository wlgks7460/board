package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.dto.PostUpDateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    @Autowired
    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    public List<PostListResDto> posts(){
        List<Post> posts = postRepository.findAll();
        List<PostListResDto> postListResDtos = new ArrayList<>();
        for(Post post : posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            postListResDtos.add(postListResDto);
        }
        return postListResDtos;
    }

    public void save(PostSaveReqDto postSaveReqDto){
        Post post = new Post(postSaveReqDto.getTitle(), postSaveReqDto.getContents());
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
