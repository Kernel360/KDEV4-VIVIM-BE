//package com.welcommu.moduleservice.approval.approvalApprover.dto;
//
//import com.welcommu.moduledomain.projectUser.ProjectUser;
//import lombok.Builder;
//import lombok.Getter;
//
//@Getter
//@Builder
//public class ApproverResponse {
//
//    private Long userId;
//    private String name;
//    private boolean isApproved;
//
//    public static ApproverResponse of(ProjectUser user, boolean isApproved) {
//        return ApproverResponse.builder()
//            .userId(user.getId())
//            .name(user.getUser().getName())
//            .isApproved(isApproved)
//            .build();
//    }
//}
