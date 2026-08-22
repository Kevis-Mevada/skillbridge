package com.skillbridge.model;

import java.io.Serializable;

/**
 * JobFilterCriteria encapsulates search query parameters for multi-filter job discovery.
 */
public class JobFilterCriteria implements Serializable {
    private static final long serialVersionUID = 1L;

    private String keyword;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private String location;
    private Integer skillId;
    private Boolean activeOnly = true;
    private int page = 1;
    private int limit = 10;

    public JobFilterCriteria() {}

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getSkillId() {
        return skillId;
    }

    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }

    public Boolean getActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(Boolean activeOnly) {
        this.activeOnly = activeOnly;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = (page < 1) ? 1 : page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = (limit < 1) ? 10 : limit;
    }

    public int getOffset() {
        return (page - 1) * limit;
    }
}
