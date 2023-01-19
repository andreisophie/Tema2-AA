import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Trial extends Task {
    private int size;
    private int n;
    private int k;
    private ArrayList<HashSet<Integer>> sets;
    private HashSet<Integer> solution;

    public Trial() {
        sets = new ArrayList<>();
        solution = new HashSet<>();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Trial instance = new Trial();
        instance.readProblemData();
        instance.solve();
        instance.writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        Scanner in = new Scanner(System.in);

        size = in.nextInt();
        n = in.nextInt();
        k = in.nextInt();

        for (int i = 0; i < n; i++) {
            int setSize = in.nextInt();
            HashSet<Integer> newSet = new HashSet<>();
            for (int j = 0; j < setSize; j++) {
                int element = in.nextInt();
                newSet.add(Integer.valueOf(element));
            }
            sets.add(newSet);
        }

        in.close();
    }

    private int yToInt(int i, int r) {
        return 1 + i * k + r;
    }

    private void firstClauses(StringBuilder out) {
        for (int r = 0; r < k; r++) {
            for (int i = 0; i < n; i++) {
                out.append(yToInt(i, r) + " ");
            }
            out.append("0\n");
        }
    }

    private void secondClauses(StringBuilder out) {
        for (int i = 0; i < n; i++) {
            for (int r = 0; r < k; r++) {
                for (int s = r + 1; s < k; s++) {
                    out.append(-yToInt(i, r) + " ");
                    out.append(-yToInt(i, s) + " ");
                    out.append("0 \n");
                }
            }
        }
    }

    private void thirdClauses(StringBuilder out) {
        for (int r = 0; r < k; r++) {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    out.append(-yToInt(i, r) + " ");
                    out.append(-yToInt(j, r) + " ");
                    out.append("0 \n");
                }
            }
        }
    }

    private void finalClauses(StringBuilder out) {
        for (int e = 1; e <= size; e++) {
            HashSet<Integer> setsElem = new HashSet<>();
            for (int i = 0; i < n; i++) {
                if (sets.get(i).contains(Integer.valueOf(e))) {
                    setsElem.add(Integer.valueOf(i));
                }
            }

            for (Integer i : setsElem) {
                for (int r = 0; r < k; r++) {
                    out.append(yToInt(i.intValue(), r) + " ");
                }
            }

            out.append("0 \n");
        }
    }

    @Override
    public void formulateOracleQuestion(StringBuilder out) throws IOException {
        firstClauses(out);
        secondClauses(out);
        thirdClauses(out);
        finalClauses(out);
    }

    private int indexFromY(int y) {
        return (y - 1) / k + 1;
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("sat.sol"));
        String result = reader.readLine();

        if (result.equals("False")) {
            reader.close();
            return;
        }

        // read number of variables
        reader.readLine();

        StringTokenizer variables = new StringTokenizer(reader.readLine());

        for (int i = 1; i <= n * k; i++) {
            int currentVariable = Integer.parseInt(variables.nextToken());

            if (currentVariable < 0) {
                continue;
            }

            solution.add(indexFromY(Integer.valueOf(i)));
        }

        reader.close();
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("sat.cnf"));
        StringBuilder out = new StringBuilder("p cnf ");

        out.append(n * k + " ");
        int nrClauses = k + n * k * (k - 1) / 2 + n * (n - 1) / 2 * k + size;
        out.append(nrClauses + "\n");

        formulateOracleQuestion(out);

        writer.write(out.toString());
        writer.close();
        
        askOracle();

        decipherOracleAnswer();
    }

    @Override
    public void writeAnswer() throws IOException {
        if (solution.size() == 0) {
            System.out.println("False");
            return;
        }
        System.out.println("True");
        System.out.println(solution.size());
        
        StringBuilder out = new StringBuilder();
        for (Integer i : solution) {
            out.append(i.intValue() + " ");
        }
        System.out.println(out);
    }
}
