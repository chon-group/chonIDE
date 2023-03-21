package org.masos.embed.sysconfig.domain.file.model;

import java.util.HashMap;
import java.util.Map;

public class ProjectsMapping {

    private Map<Long, String> projects;

    public ProjectsMapping() {
        this.projects = new HashMap<>();
    }

    /**
     * @return {@link #projects}
     */
    public Map<Long, String> getProjects() {
        return this.projects;
    }

    /**
     * @param projects {@link #projects}
     */
    public void setProjects(Map<Long, String> projects) {
        this.projects = projects;
    }
}
