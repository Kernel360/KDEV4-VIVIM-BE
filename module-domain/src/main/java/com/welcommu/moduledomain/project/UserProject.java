package com.welcommu.moduledomain.project;

import com.welcommu.moduledomain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProject {

    @EmbeddedId
    private UserProjectId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("projectId")
    @JoinColumn(name = "projects_id")
    private Project project;

    @Builder
    public UserProject(User user, Project project) {
        this.user = user;
        this.project = project;
        this.id = new UserProjectId(user.getId(), project.getId());
    }
}
