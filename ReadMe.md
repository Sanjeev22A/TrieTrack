<h1>configure the GitEnvironment.java class as follows</h1>

package ENV;

public class GitEnvironment{

    private static final String GITHUB_TOKEN="YOURSSHKEY";
    private static final String USER="USERNAME";
    private static final String PASSWORD="PASSWORD";

    public static String getGithubToken(){
        return GITHUB_TOKEN;
    }
    public static String getUser(){
        return USER;
    }
    public static String getPassword(){
        return PASSWORD;
    }
}
