package group.chon.ide.api.api.controller.git;

import group.chon.ide.api.api.authentication.AuthenticatedUser;
import group.chon.ide.api.api.controller.ApiController;
import group.chon.ide.api.api.controller.ResponseEntity;
import group.chon.ide.news.app.service.git.GitService;
import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.service.project.ProjectService;
import group.chon.ide.news.domain.service.project.StandardProjectService;
import group.chon.ide.news.domain.service.projectfile.ProjectFileService;
import group.chon.ide.news.domain.repository.FileRepository;
import group.chon.ide.news.domain.service.projectfile.StandardProjectFileService;
import group.chon.ide.news.infra.repository.LocalFileRepository;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@WebServlet("/api/git")
public class GitController extends ApiController {

    private final ProjectService projectService;

    public GitController() {
        FileRepository fileRepository = new LocalFileRepository();
        ProjectFileService projectFileService = new StandardProjectFileService(fileRepository);
        this.projectService = new StandardProjectService(projectFileService);
    }

    private Project resolveProject(String projectName) {
        if (projectName == null || projectName.trim().isEmpty()) {
            return null;
        }
        Project project = projectService.getByName(projectName);
        if (project == null) {
            project = new Project(projectName, null);
        }
        return project;
    }
    
    @Override
    protected ResponseEntity post(AuthenticatedUser authenticatedUser, Map<String, Object> parameters) {
        String projectName = parameters.get("projectName").toString();
        String action = parameters.get("action").toString();
        Project project = resolveProject(projectName);
        GitService gitService = new GitService(project);
        switch (action) {
            case "status":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.getStatus());
            case "init":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.initializeRepository());
            case "add": {
                String files = parameters.get("files").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.addFile(files));
            }
            case "commit": {
                String message = parameters.get("message").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.commitRepository(message));
            }
            case "push": {
                String remoteURL = parameters.get("remoteURL").toString();
                String branchName = parameters.get("branchName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.pushRemoteRepository(remoteURL, branchName));
            }
            case "log":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.logRepository());
            case "listBranches":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.listBranches());
            case "createBranch": {
                String branchName = parameters.get("branchName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.createBranch(branchName));
            }
            case "checkout": {
                String branchName = parameters.get("branchName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.checkoutBranch(branchName));
            }
            case "merge": {
                String mergeBranch = parameters.get("branchName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.mergeBranch(mergeBranch));
            }
            case "pull": {
                String pullRemote = parameters.get("remoteURL").toString();
                String pullBranch = parameters.get("branchName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.pullRemoteRepository(pullRemote, pullBranch));
            }
            case "clone": {
                String remoteUrl = parameters.get("remoteURL").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.cloneRemoteRepository(remoteUrl));
            }
            case "stash":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.stashRepository());
            case "applyStash":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.applyStash());
            case "rebase": {
                String rebaseBranch = parameters.get("branchName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.rebaseBranch(rebaseBranch));
            }
            case "listRemotes":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.listRemotes());
            case "addRemote": {
                String remoteName = parameters.get("remoteName").toString();
                String remoteURL = parameters.get("remoteURL").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.addRemote(remoteName, remoteURL));
            }
            case "listTags":
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.listTags());
            case "createTag": {
                String tagName = parameters.get("tagName").toString();
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_OK)
                        .message(gitService.createTag(tagName));
            }
            default:
                return ResponseEntity.get()
                        .status(HttpServletResponse.SC_BAD_REQUEST)
                        .message("Invalid POST action for Git.");
        }
    }
}
