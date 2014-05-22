import java.io.IOException;
import java.util.List;


public interface OutputFormatter {
	
	void setAlgoNames(List<String> algoNames);

	void preprocess(int t, int v) throws IOException;

	void output(int k, List<CA> results) throws IOException;

}
