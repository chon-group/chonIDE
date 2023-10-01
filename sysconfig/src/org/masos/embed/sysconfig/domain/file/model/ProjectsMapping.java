package org.masos.embed.sysconfig.domain.file.model;

import java.util.TreeMap;

public class ProjectsMapping {

    private TreeMap<Long, String> projects;

    public ProjectsMapping() {
        this.projects = new TreeMap<>();
    }

    public long getNextId() {
        if (this.projects.size() == 0) {
            return 1;
        }
        return this.projects.lastKey() + 1;
    }

    /**
     * @return {@link #projects}
     */
    public TreeMap<Long, String> getProjects() {
        return this.projects;
    }

    /**
     * @param projects {@link #projects}
     */
    public void setProjects(TreeMap<Long, String> projects) {
        this.projects = projects;
    }
}
