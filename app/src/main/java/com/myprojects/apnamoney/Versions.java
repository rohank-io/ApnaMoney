package com.myprojects.apnamoney;

public class Versions {
    private String codeName,version,apiLevel,description;
    private Boolean expandable;

    public Boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(Boolean expandable) {
        this.expandable = expandable;
    }

    public Versions(String codeName, String version, String apiLevel, String description) {
        this.codeName = codeName;
        this.version = version;
        this.apiLevel = apiLevel;
        this.description = description;

        this.expandable = false;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Versions{" +
                "codeName='" + codeName + '\'' +
                ", version='" + version + '\'' +
                ", apiLevel='" + apiLevel + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
