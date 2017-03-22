package importExcel;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author Snowolf
 * @version 1.0
 * @since 1.0
 */
public class Usertest {
	public static void main(String[] args) {
		String path = "user.xls";
		List<String> list = null;
		try {
			list = ImportUser.exportListFromExcel(new File(path), 0);
			System.out.println(list);
		} catch (IOException e) {
		}

	}
}

