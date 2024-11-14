package Service;

import Service.ContentHolder;
import Service.FileDS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

class FileDSTest {

    private FileDS fileDS;

    @BeforeEach
    void setUp() {
        List<String> mainBranch = Arrays.asList("A", "B", "C", "D");
        fileDS = new FileDS(mainBranch, 1);
    }

    @Test
    void testCreateNewBranch() {
        List<String> newBranch = Arrays.asList("A", "B", "X", "Y");
        int versionNumber = fileDS.createNewBranch(newBranch);
        assertTrue(fileDS.versionNumbers().contains(versionNumber));
    }

    @Test
    void testTraverse() {
        List<String> newBranch = Arrays.asList("A", "B", "X", "Y");
        int versionNumber = fileDS.createNewBranch(newBranch);

        List<String> result = fileDS.traverse(versionNumber);
        assertEquals(newBranch, result);
    }

    @Test
    void testDeleteBranch() {
        List<String> newBranch = Arrays.asList("A", "B", "X", "Y");
        int versionNumber = fileDS.createNewBranch(newBranch);
        fileDS.deleteBranch(versionNumber);

        List<String> result = fileDS.traverse(versionNumber);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateBranch() {
        List<String> newBranch = Arrays.asList("A", "B", "X", "Y");
        int versionNumber = fileDS.createNewBranch(newBranch);
        List<String> updatedBranch = Arrays.asList("A", "B", "X", "Z");

        fileDS.updateBranch(updatedBranch, versionNumber);
        List<String> result = fileDS.traverse(versionNumber);
        assertEquals(updatedBranch, result);
    }

    @Test
    void testVersionNumbers() {
        List<String> newBranch1 = Arrays.asList("A", "B", "X");
        List<String> newBranch2 = Arrays.asList("A", "B", "C", "E");
        int version1 = fileDS.createNewBranch(newBranch1);
        int version2 = fileDS.createNewBranch(newBranch2);

        HashSet<Integer> allVersions = fileDS.versionNumbers();
        assertTrue(allVersions.contains(1)); // Initial version
        assertTrue(allVersions.contains(version1));
        assertTrue(allVersions.contains(version2));
    }

    @Test
    void testTraverseAllBranches() {
        List<String> newBranch1 = Arrays.asList("A", "B", "X");
        List<String> newBranch2 = Arrays.asList("A", "B", "C", "E");
        fileDS.createNewBranch(newBranch1);
        fileDS.createNewBranch(newBranch2);

        List<ContentHolder> branches = fileDS.traverseAllBranches();
        assertFalse(branches.isEmpty());

        boolean foundInitial = false;
        for (ContentHolder ch : branches) {
            if (ch.versionNumber == 1) {
                foundInitial = true;
                assertEquals(Arrays.asList("A", "B", "C", "D"), ch.content);
            }
        }
        assertTrue(foundInitial);
    }
}
