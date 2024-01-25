package com.encore.board.author.domain;

import com.encore.board.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
//@Builder
//@AllArgsConstructor
//위와 같이 모든 매개변수가 있는 생성자를 생성하는 어노테이션과 Builder를 클래스에 붙여
//모든 매개변수가 있는 생성자가 위에 Builder어노테이션을 붙인것과 같은 효과가 있다.
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

//    author를 조회할 때 객체가 필요할시에 선언
//    mappedBy에 연관관계의 주인을 명시하고, fk를 관리하는 변수명을 명시
//    1:1관계일경우 @OneToOne도 존재
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
//    @Setter // cascade.persist를 위한 테스트
    private List<Post> posts;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;

    public enum Role{
        ADMIN, USER;
    }

//    builder 어노테이션을 통해 빌더 패턴으로 객체생성
//    매개변수의 세팅 순서, 매개변수의 갯수 등을 유연하게 세팅
    @Builder
    public Author(String name, String email, String password, Role role, List<Post> posts){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.posts = posts;
    }

    public void updateAuthor(String name, String password){
        this.name=name;
        this.password=password;
    }
}


