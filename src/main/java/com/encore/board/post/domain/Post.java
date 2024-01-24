package com.encore.board.post.domain;

import com.encore.board.author.domain.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 3000)
    private String contents;

//    author_id는 DB의 컬럼명, 별다른 옵션이 없을 시 author의 pk에 fk가 설정
//    post 객체 입장에서는 한사람이 여러개의 글을 쓸 수 있으므로 N:1
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
//    @JoinColumn(nullable = false, name = "author_email", referencedColumnName = "email")
    private Author author;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;

    @Builder
    public Post(String title, String contents, Author author) {
        this.title = title;
        this.contents = contents;
        this.author = author;
//        author 객체의 posts를 초기화 시켜준 후
//        this.author.getPosts().add(this);
    }
    public void updatePost(String title, String contents){
        this.title=title;
        this.contents=contents;
    }
}
