import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GeneBankCreateBTree {
    static final char[] validCharacters = new char[] {'A', 'T', 'C', 'G', 'a', 't', 'c', 'g', 'N', 'n'};
    public static void main(String[] args) {
        boolean fullDebug;
        boolean useCache;
        int cacheSize;

        if(args.length < 5 || args.length > 6) {
            PrintUsage();
            return;
        }

        int cache = Integer.parseInt(args[0]);
        useCache = cache != 0;

        int degree = Integer.parseInt(args[1]);
        if(degree == 0) {
            degree = 102;
        }

        String gbkFile = args[2];

        int sequenceLength = Integer.parseInt(args[3]);
        if(sequenceLength > 31 || sequenceLength < 1) {
            System.out.println("Sequence length out of range: 1-31 supported.");
            return;
        }

        if(useCache) {
            cacheSize = Integer.parseInt(args[4]);
            if(cacheSize == 0) {
                useCache = false;
            }
        } else {
            cacheSize = -1;
        }

        String[] gbkFileParts = gbkFile.split("/");
        String bTreeFileName = gbkFileParts[gbkFileParts.length - 1] + ".btree.data." + sequenceLength + "." + degree;

        if(args.length > 4) {
            if(Integer.parseInt(args[args.length - 1]) == 0) {
                fullDebug = false;
            } else {
                fullDebug = true;
            }
        } else {
            fullDebug = false;
        }

        BTree t = new BTree(degree, sequenceLength, bTreeFileName, useCache, cacheSize);

        Scanner scanner;

        try {
            scanner = new Scanner(new File(gbkFile));
        } catch(FileNotFoundException e) {
            System.out.println("Could not find file: '" + gbkFile + "'");
            return;
        }

        while(scanner.hasNext()) {

            try {
                while(!scanner.nextLine().trim().equals("ORIGIN")) {

                }
            } catch (NoSuchElementException e) {
                System.out.println("Invalid .gbk file provided");
                System.exit(1);
            }

            char[] currentSequence = new char[sequenceLength];
            int currentSequenceLength = 0;

            for(String sequence = scanner.nextLine(); !sequence.trim().equals("//"); sequence = scanner.nextLine()) {
                for(char ch : sequence.toCharArray()) {
                    if(contains(ch, validCharacters)) {
                        if(ch == 'n' || ch == 'N') {
                            currentSequenceLength = 0;
                        } else {
                            currentSequenceLength += 1;
                            shiftSequence(currentSequence, ch);
                            if(currentSequenceLength >= sequenceLength) {
                                t.BTreeIncrementFrequency(sequenceToLong(currentSequence));
                            }
                        }
                    }
                }
            }

        }

        scanner.close();

        t.finalizeFile();

        if(fullDebug) {
            WriteDebugFile(t);
        }

    }

    static void shiftSequence(char[] array, char newChar) {
        for(int i = 0; i < array.length - 1; i++) {
            array[i] = array[i + 1];
        }
        array[array.length - 1] = newChar;
    }

    static boolean contains(char c, char[] array) {
        for(char ch : array) {
            if(ch == c) {
                return true;
            }
        }
        return false;
    }

    static long sequenceToLong(char[] sequence) {
        long val = 0;

        for(int pos = 0; pos < sequence.length; pos++) {
            char c = sequence[pos];
            int bitRepresentation = 0;
            long overlay = 0;
            switch(Character.toUpperCase(c)) {
                case 'A':
                    overlay = 0;
                break;

                case 'C':
                    overlay = 1;
                break;

                case 'G':
                    overlay = 2;
                break;

                case 'T':
                    overlay = 3;
                break;
            }

            val |= overlay << (pos * 2);
        }
        return val;
    }

    static char[] longToSequence(long val, int len) {
        char[] seq = new char[len];

        for(int pos = 0; pos < len; pos++) {
            long bits = (val >> pos * 2) & 3;
            switch((int)bits) {
                case 0:
                    seq[pos] = 'A';
                break;

                case 1:
                    seq[pos] = 'C';
                break;

                case 2:
                    seq[pos] = 'G';
                break;

                case 3:
                    seq[pos] = 'T';
                break;
            }
        }
        return seq;
    }

    static void WriteDebugFile(BTree t) {

        String printString = t.InOrderTraversalToString(t.root);
        try {
            PrintWriter writer = new PrintWriter("dump");
            writer.print(printString);
            writer.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
    
    static void PrintUsage() {
    
        System.out.println("INVALID COMMAND LINE ARGUMENTS, PRINTING USAGE");
        System.out.println("USAGE: java GeneBankCreateBTree <0/1 (no/with cache)> <degree> <gbk file> <sequence length> <cache size> [<debug level>]");
        System.out.println("\t<0/1 (no/with cache)> - To have the program use a cache, enter 1. To have the program not use a chace, enter 0.");
        System.out.println("\t<degree> - The degree for the created BTree file.");
        System.out.println("\t<gdk file> - The file of genes that the program will analyze and make a BTree out of.");
        System.out.println("\t<sequence length> - The length for the sequence of genes that the program will read.");
        System.out.println("\t<cache size> - The size of the cache for the program assuming that cache is wanted.");
        System.out.println("\t[<debug level>] - Optional command line argument. If a 0 is entered, the results will be printed to the error stream and if 1 is entered, a file called dump is created.");
    
    }

}