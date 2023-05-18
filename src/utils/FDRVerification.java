package utils;

import uk.ac.ox.cs.fdr.Assertion;
import uk.ac.ox.cs.fdr.FileLoadError;
import uk.ac.ox.cs.fdr.InputFileError;
import uk.ac.ox.cs.fdr.Session;
import uk.ac.ox.cs.fdr.fdr;

public class FDRVerification {

    public static boolean[] testFDR(String path){
        boolean bool[]=new boolean[2];
        try {
            Session session = new Session();
            session.loadFile(path);
            int i=-1;
            for (Assertion assertion : session.assertions()) {
                i++;
                assertion.execute(null);
                bool[i]=assertion.passed();
            }
            return bool;
        }
        catch (InputFileError error) {
            System.out.println(error);
        }
        catch (FileLoadError error) {
            System.out.println(error);
        }

        fdr.libraryExit();

        return new boolean[]{false};
    }
}
