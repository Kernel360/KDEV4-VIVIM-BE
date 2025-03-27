package com.welcommu.moduledomain.projectprogress;

import com.welcommu.moduledomain.project.Project;
import com.welcommu.moduledomain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectProgress {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private float position;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    private boolean isDeleted;

    @OneToOne
    private User user;

    @ManyToOne
    private Project project;
}
