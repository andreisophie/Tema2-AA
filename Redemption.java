import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Redemption {
    private HashSet<String> ownedCards;
    private HashSet<String> neededCards;
    private ArrayList<HashSet<String>> decks;
    private ArrayList<Integer> indexDecksSolution;
    private HashMap<HashSet<String>, Integer> decksMap;

    public Redemption() {
        ownedCards = new HashSet<>();
        neededCards = new HashSet<>();
        decks = new ArrayList<>();
        indexDecksSolution = new ArrayList<>();
        decksMap = new HashMap<>();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Redemption instance = new Redemption();
        instance.readProblemData();
        instance.solve();
        instance.writeAnswer();
    }
    
    public void readProblemData() throws IOException {
        Scanner in = new Scanner(System.in);

        int nrOwned;
        int nrTotal;
        int nrDecks;
        nrOwned = in.nextInt();
        nrTotal = in.nextInt();
        nrDecks = in.nextInt();

        in.nextLine();

        for (int i = 0; i < nrOwned; i++) {
            ownedCards.add(in.nextLine());
        }

        for (int i = 0; i < nrTotal; i++) {
            neededCards.add(in.nextLine());
        }

        for (int i = 1; i <= nrDecks; i++) {
            HashSet<String> newDeck = new HashSet<>();
            int nrCards = in.nextInt();
            in.nextLine();
            for (int j = 0; j < nrCards; j++) {
                newDeck.add(in.nextLine());
            }
            decks.add(newDeck);
            decksMap.put(newDeck, Integer.valueOf(i));
        }

        in.close();
    }

    private void sortDecks() {
        Comparator<HashSet<String>> comparator = new Comparator<HashSet<String>>() {
            @Override
            public int compare(HashSet<String> o1, HashSet<String> o2) {
                HashSet<String> o1copy = new HashSet<>(o1);
                HashSet<String> o2copy = new HashSet<>(o2);
                o1copy.retainAll(neededCards);
                o2copy.retainAll(neededCards);
                return o2copy.size() - o1copy.size();
            }
        };

        Collections.sort(decks, comparator);
    }

    public void solve() throws IOException, InterruptedException {
        neededCards.removeAll(ownedCards);

        while (neededCards.size() > 0) {
            sortDecks();
            HashSet<String> bestDeck = decks.get(0);
            indexDecksSolution.add(decksMap.get(bestDeck));
            ownedCards.addAll(bestDeck);
            decks.remove(0);
            neededCards.removeAll(ownedCards);
        }
    }

    public void writeAnswer() throws IOException {
        System.out.println(indexDecksSolution.size());

        StringBuilder out = new StringBuilder();
        for (int i = 0; i < indexDecksSolution.size(); i++) {
            out.append(indexDecksSolution.get(i));
            out.append(" ");
        }

        System.out.println(out.toString());
    }
}
