/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.joeyoung.githubsearch;

import java.util.Date;

/**
 * Represents a Git Hub search result entry
 * @author joeyoung
 */
public class SearchResultEntry {
    
    private String name;
    private String fullName;
    private String htmlUrl;
    private String description;
    private Date updatedAt;
    private String cloneUrl;
    private int stargazersCount;
    private String language;
    private int forksCount;

    /**
     * 
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return
     */
    public String getHtmlUrl() {
        return htmlUrl;
    }

    /**
     *
     * @param htmlUrl
     */
    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     */
    public Date getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     */
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     */
    public String getCloneUrl() {
        return cloneUrl;
    }

    /**
     *
     * @param cloneUrl
     */
    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    /**
     *
     * @return
     */
    public int getStargazersCount() {
        return stargazersCount;
    }

    /**
     *
     * @param stargazersCount
     */
    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     *
     * @return
     */
    public int getForksCount() {
        return forksCount;
    }

    /**
     *
     * @param forksCount
     */
    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }
}
