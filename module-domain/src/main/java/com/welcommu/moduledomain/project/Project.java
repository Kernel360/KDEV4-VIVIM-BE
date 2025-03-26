package com.welcommu.moduledomain.project;

import com.welcommu.moduledomain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private Boolean isDeleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id", nullable = false) // 외래키 컬럼명 지정
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifire_id", referencedColumnName = "id") // 외래키 컬럼명 지정
    private User modifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deleter_id", referencedColumnName = "id") // 외래키 컬럼명 지정
    private User deleter;


}
