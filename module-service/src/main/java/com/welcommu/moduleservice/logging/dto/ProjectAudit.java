package com.welcommu.moduleservice.logging.dto;

import com.welcommu.moduledomain.project.Project;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProjectAudit {

    private final String name;
    private final String description;
    private final String startDate;
    private final String endDate;

    public static ProjectAudit from(Project project) {
        return new ProjectAudit(
            project.getName(),
            project.getDescription(),
            project.getStartDate() != null ? project.getStartDate().toString() : null,
            project.getEndDate() != null ? project.getEndDate().toString() : null
        );
    }

    public Map<String, String[]> compare(ProjectAudit after) {
        Map<String, String[]> changes = new HashMap<>();

        if (!Objects.equals(this.name, after.name)) {
            changes.put("name", new String[]{this.name, after.name});
        }

        if (!Objects.equals(this.description, after.description)) {
            changes.put("description", new String[]{this.description, after.description});
        }

        if (!Objects.equals(this.startDate, after.startDate)) {
            changes.put("startDate", new String[]{this.startDate, after.startDate});
        }

        if (!Objects.equals(this.endDate, after.endDate)) {
            changes.put("endDate", new String[]{this.endDate, after.endDate});
        }

        return changes;
    }

}
