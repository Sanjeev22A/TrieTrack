package GitHubService;

import ENV.GitEnvironment;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class GitHub {

    private static Git initializeGitRepo(String repoDir) throws IOException,GitAPIException {
        File localRepo = new File(repoDir);
        Repository repo = new RepositoryBuilder().setGitDir(new File(localRepo, ".git"))
                .readEnvironment()
                .findGitDir()
                .build();
        Git git;
        if (!repo.getDirectory().exists()) {
            git = Git.init().setDirectory(localRepo).call();
        } else {
            git = Git.open(localRepo);
        }
        return git;
    }

    private static void PushSingleFile(String repoDir, String filename, final String URI, String userMessage, String branchName) {
        try {
            Git git = initializeGitRepo(repoDir);
            checkoutOrCreateBranch(git, branchName);
            addRemoteIfNeeded(git, URI);
            git.add().addFilepattern(filename).call();
            commitAndPush(git, userMessage, branchName);
        } catch (IOException | URISyntaxException | GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    private static void PushDirectory(String repoDir, final String URI, String userMessage, String branchName) {
        try {
            Git git = initializeGitRepo(repoDir);
            checkoutOrCreateBranch(git, branchName);
            addRemoteIfNeeded(git, URI);
            git.add().addFilepattern(".").call();
            commitAndPush(git, userMessage, branchName);
        } catch (IOException | URISyntaxException | GitAPIException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void checkoutOrCreateBranch(Git git, String branchName) throws GitAPIException {
        boolean branchExists = git.branchList().call().stream()
                .anyMatch(ref -> ref.getName().equals("refs/heads/" + branchName));

        if (!branchExists) {
            try {

                git.checkout().setCreateBranch(true).setName(branchName).call();
                System.out.println("Branch '" + branchName + "' created and checked out.");
            } catch (GitAPIException e) {

                git.commit().setMessage("Initial commit").call();
                git.checkout().setCreateBranch(true).setName(branchName).call();
                System.out.println("No commits found. Created an initial commit and checked out branch '" + branchName + "'.");
            }
        } else {

            git.checkout().setName(branchName).call();
        }
    }

    private static void addRemoteIfNeeded(Git git, String URI) throws URISyntaxException, GitAPIException {
        if (git.remoteList().call().stream().noneMatch(remoteConfig -> remoteConfig.getName().equals("origin"))) {
            git.remoteAdd().setName("origin").setUri(new URIish(URI)).call();
        }
    }

    private static void commitAndPush(Git git, String userMessage, String branchName) throws GitAPIException {
        if (userMessage == null || userMessage.equalsIgnoreCase("")) {
            userMessage = "Committing some changes";
        }
        git.commit().setMessage(userMessage).call();

        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(
                GitEnvironment.getUser(), GitEnvironment.getGithubToken()
        );
        PushCommand push = git.push();
        push.setCredentialsProvider(credentialsProvider);
        RefSpec refSpec = new RefSpec("refs/heads/" + branchName + ":refs/heads/" + branchName);
        push.setRefSpecs(refSpec);
        push.call();
    }

    public static void push(String repoDir, final String URI, String userMessage, String branchName) {
        File selectedFile = new File(repoDir);
        if (selectedFile.isFile()) {
            String filename = selectedFile.getName();
            String dirPath = selectedFile.getParent();
            PushSingleFile(dirPath, filename, URI, userMessage, branchName);
        } else if (selectedFile.isDirectory()) {
            PushDirectory(repoDir, URI, userMessage, branchName);
        } else {
            throw new IllegalArgumentException("The provided path is neither a valid file nor directory.");
        }
    }
}
