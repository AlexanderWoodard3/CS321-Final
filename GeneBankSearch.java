import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The driver class to search through an already-created B-Tree (using GeneBankCreateBTree) to find frequency of certain strings
 * sequences.
 */
public class GeneBankSearch {

	public static void main(String[] args) {

		/* Validate arguments */
		if (args.length < 3 || args.length > 5) {
			// probably should give feedback
		}

		boolean useCache = (Integer.parseInt(args[0]) == 1)? true : false;

		String bTreeFileName = args[1];

		/* Process query file into arrayList queries */
		String queryFileString = args[2];
		File queryFile = new File(queryFileString);
		ArrayList<Long> queries = new ArrayList<Long>();
		try {
			Scanner readQuery = new Scanner(queryFile);
			while(readQuery.hasNext())
			{
				String nextQuery = readQuery.next().toLowerCase();
				nextQuery = nextQuery.replaceAll("a", "00");
				nextQuery = nextQuery.replaceAll("t", "01");
				nextQuery = nextQuery.replaceAll("c", "10");
				nextQuery = nextQuery.replaceAll("g", "11");
				queries.add(Long.parseLong(nextQuery, 2));
			}
			readQuery.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		int cacheSize = 0;
        int debugLevel = 0;
        if(useCache)
        {
            cacheSize = Integer.parseInt(args[3]);
            if(args.length >= 5)
            {
                debugLevel = Integer.parseInt(args[4]);
            }
        }
        else if(args.length >= 4)
        {
            debugLevel = Integer.parseInt(args[3]);
		}
		
		/* Create B Tree from file and search for each query */
		BTree searchTree = new BTree(bTreeFileName);
		StringBuilder dumpBuilder = new StringBuilder();
		for(Long l : queries)
		{
			int frequency = searchTree.search(l);
			if(debugLevel == 1)
			{
				dumpBuilder.append(BTree.longToString(l) + ": " + frequency);
			}
			else
			{
				System.out.println(BTree.longToString(l) + ": " + frequency);
			}
		}

		if(debugLevel == 1)
		{
			try
			{
				File newFile = new File(bTreeFileName + ".queries.dump");
				newFile.createNewFile();

				FileWriter dumpWriter = new FileWriter(bTreeFileName + ".queries.dump");
				dumpWriter.write(dumpBuilder.toString());
				dumpWriter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
