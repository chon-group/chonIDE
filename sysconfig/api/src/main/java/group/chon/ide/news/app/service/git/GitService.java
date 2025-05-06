package group.chon.ide.news.app.service.git;

import java.util.HashMap;
import java.util.Map;

import group.chon.ide.news.domain.model.Project;
import group.chon.ide.news.domain.service.path.ProjectPathHandlerManager;
import group.chon.ide.news.infra.accessmethods.LocalCommandPromptRunner;

public class GitService {
    private final LocalCommandPromptRunner localCommandPromptRunner = new LocalCommandPromptRunner();
    private final Project project;

    public GitService(Project project) {
        this.project = project;
    }

    private String getRepositoryPath() {
        return ProjectPathHandlerManager
                .getInstance()
                .getProjectPathHandler()
                .mountProjectPath(project);
    }

    public String formatCommand(String command) {
        String repositoryPath = getRepositoryPath();
        String gitCommand = "git -C " + repositoryPath + " " + command;
        return gitCommand;
    }

    public static String formatUrl(String url) {
        if (url == null) {
            throw new IllegalArgumentException("A URL não pode ser nula.");
        }
        
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        
        if (url.endsWith(".git")) {
            url = url.substring(0, url.length() - 4);
        }
        
        String[] parts = url.split("/");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Formato da URL inválido: " + url);
        }
        
        String username = parts[parts.length - 2];
        String repositoryName = parts[parts.length - 1];

        return "ssh://git@ssh.github.com:443/" + username + "/" + repositoryName + ".git";
    }   

    // git status
    public String getStatus() {
        String command = formatCommand("status");
        return localCommandPromptRunner.execute(command);
    }

    // git init
    public String initializeRepository() {
        String command = formatCommand("init");
        return localCommandPromptRunner.execute(command);
    }

    // git add
    public String addFile(String files) {
        String command = formatCommand("add " + files);
        String response = localCommandPromptRunner.execute(command);
        if (!response.isEmpty()) {
            return response;
        } else {
            return "File(s) added successfully.";
        }
    }

    public String commitRepository(String message) {
        String command = formatCommand("commit -m " + message);
        return localCommandPromptRunner.execute(command);
    }    
     

    // git push
    public String pushRemoteRepository(String remoteUrl, String branchName) {
        if (remoteUrl == null || remoteUrl.trim().isEmpty()) {
            return "Remote URL é obrigatória para o push.";
        }
        if (branchName == null || branchName.trim().isEmpty()) {
            return "Branch name é obrigatória para o push.";
        }
        String credentialedUrl = formatUrl(remoteUrl);
        String command = formatCommand("push " + credentialedUrl + " " + branchName);
        return localCommandPromptRunner.execute(command);
    }


    // git log
    public String logRepository() {
        String command = formatCommand("log");
        return localCommandPromptRunner.execute(command);
    }

    // git branch (list)
    public String listBranches() {
        String command = formatCommand("branch");
        String response =  localCommandPromptRunner.execute(command);
        if (!response.isEmpty()) {
            return response;
        } else {
            return "No branches have been created yet.";
        }
    }

    // git branch (create)
    public String createBranch(String branchName) {
        String command = formatCommand("branch " + branchName);
        String response = localCommandPromptRunner.execute(command);
        if (!response.isEmpty()) {
            return response;
        } else {
            return "Branch created successfully.";
        }
    }

    // git checkout
    public String checkoutBranch(String branchName) {
        String command = formatCommand("checkout " + branchName);
        return localCommandPromptRunner.execute(command);
    }

    // git merge
    public String mergeBranch(String branchName) {
        String command = formatCommand("merge " + branchName);
        return localCommandPromptRunner.execute(command);
    }

    // git pull
    public String pullRemoteRepository(String remoteUrl, String branchName) {
        if (remoteUrl == null || remoteUrl.trim().isEmpty()) {
            return "Remote URL é obrigatória para o pull.";
        }
        if (branchName == null || branchName.trim().isEmpty()) {
            return "Branch name é obrigatória para o pull.";
        }
        String credentialedUrl = formatUrl(remoteUrl);
        String command = formatCommand("pull " + credentialedUrl + " " + branchName);
        return localCommandPromptRunner.execute(command);
    }


    // git clone
    public String cloneRemoteRepository(String remoteUrl) {
        if (remoteUrl == null || remoteUrl.trim().isEmpty()) {
            return "Remote URL é obrigatória para o clone.";
        }
        String credentialedUrl = formatUrl(remoteUrl);
        String repositoryPath = getRepositoryPath();
        String command = "git clone " + credentialedUrl + " " + repositoryPath;
        return localCommandPromptRunner.execute(command);
    }
    

    // git stash
    public String stashRepository() {
        String command = formatCommand("stash");
        return localCommandPromptRunner.execute(command);
    }

    // git stash apply
    public String applyStash() {
        String command = formatCommand("stash apply");
        return localCommandPromptRunner.execute(command);
    }

    // git rebase
    public String rebaseBranch(String branchName) {
        String command = formatCommand("rebase " + branchName);
        return localCommandPromptRunner.execute(command);
    }

    // git remote (list)
    public String listRemotes() {
        String command = formatCommand("remote -v");
        String response = localCommandPromptRunner.execute(command);
        if (!response.isEmpty()) {
            return response;
        } else {
            return "No remotes have been created yet.";
        }
    }

    // git remote add
    public String addRemote(String remoteName, String remoteUrl) {
        if (remoteName == null || remoteName.trim().isEmpty()) {
            return "Remote name é obrigatória para addRemote.";
        }
        if (remoteUrl == null || remoteUrl.trim().isEmpty()) {
            return "Remote URL é obrigatória para addRemote.";
        }
        String credentialedUrl = formatUrl(remoteUrl);
        String command = formatCommand("remote add " + remoteName + " " + credentialedUrl);
        return localCommandPromptRunner.execute(command);
    }
    

    // git tag (list)
    public String listTags() {
        String command = formatCommand("tag");
        String response = localCommandPromptRunner.execute(command);
        if (!response.isEmpty()) {
            return response;
        } else {
            return "No tags have been created yet.";
        }
    }

    // git tag (create)
    public String createTag(String tagName) {
        String command = formatCommand("tag " + tagName);
        return localCommandPromptRunner.execute(command);
    }
}
