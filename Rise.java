import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Rise extends Task {
    private int size;
    private int n;
    private ArrayList<HashSet<Integer>> sets;
    private HashSet<Integer> solution;
    private ArrayList<String> cards;
    private HashSet<String> ownedCards;

    public Rise() {
        sets = new ArrayList<>();
        solution = new HashSet<>();
        cards = new ArrayList<>();
        ownedCards = new HashSet<>();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Rise instance = new Rise();
        instance.readProblemData();
        instance.solve();
        instance.writeAnswer();
    }

    private int getIndexOfCard(String name) {
        if (cards.contains(name)) {
            return cards.indexOf(name) + 1;
        }
        return 0;
    }

    @Override
    public void readProblemData() throws IOException {
        Scanner in = new Scanner(System.in);

        int nrOwned = in.nextInt();
        int nrTotal = in.nextInt();
        n = in.nextInt();
        in.nextLine();

        for (int i = 0; i < nrOwned; i++) {
            ownedCards.add(in.nextLine());
        }

        for (int i = 0; i < nrTotal; i++) {
            cards.add(in.nextLine());
        }

        cards.removeAll(ownedCards);
        size = cards.size();

        for (int i = 1; i <= n; i++) {
            HashSet<Integer> newSet = new HashSet<>();
            int nrCards = in.nextInt();
            in.nextLine();
            for (int j = 0; j < nrCards; j++) {
                String cardName = in.nextLine();
                int index = getIndexOfCard(cardName);
                if (index == 0) {
                    continue;
                }
                newSet.add(Integer.valueOf(index));
            }
            sets.add(newSet);
        }

        in.close();
    }

    @Override
    public void formulateOracleQuestion(StringBuilder out) throws IOException {
        for (int e = 1; e <= size; e++) {
            HashSet<Integer> setsElem = new HashSet<>();
            for (int i = 0; i < n; i++) {
                if (sets.get(i).contains(Integer.valueOf(e))) {
                    setsElem.add(Integer.valueOf(i));
                }
            }

            for (Integer i : setsElem) {
                out.append(i.intValue() + 1 + " ");
            }

            out.append("0 \n");
        }
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

        for (int i = 1; i <= n; i++) {
            int currentVariable = Integer.parseInt(variables.nextToken());

            if (currentVariable < 0) {
                continue;
            }

            solution.add(Integer.valueOf(i));
        }

        reader.close();
    }

    @Override
    public void solve() throws IOException, InterruptedException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("sat.cnf"));
        StringBuilder out = new StringBuilder("p cnf ");

        out.append(n + " ");
        int nrClauses = size;
        out.append(nrClauses + "\n");

        formulateOracleQuestion(out);

        writer.write(out.toString());
        writer.close();
        
        askOracle();

        decipherOracleAnswer();
    }

    @Override
    public void writeAnswer() throws IOException {
        System.out.println(solution.size());
        
        StringBuilder out = new StringBuilder();
        for (Integer i : solution) {
            out.append(i.intValue() + " ");
        }
        System.out.println(out);
    }
}
