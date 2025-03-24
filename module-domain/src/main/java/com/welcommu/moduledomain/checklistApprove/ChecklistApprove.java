package com.welcommu.moduledomain.checklistApprove;

import com.welcommu.moduledomain.checklist.Checklist;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "checklist_approves")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistApprove {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String approveOption;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;
}
