import java.util.*;

// Time complexity: O(n* k * log(k)) where n is the number of sentences and k is a constant so O(N) -> big N
// Space complexity: O(n) where n is the number of sentences
class AutocompleteSystem {
    String searchterm;
    private HashMap<String, Integer> map;
    PriorityQueue<String> pq = new PriorityQueue<String>((a, b) -> {
        int cntA = map.get(a);
        int cntB = map.get(b);

        if (cntA == cntB) {
            int comp = b.compareTo(a);
            return comp;
        }

        return cntA - cntB;
    });

    public AutocompleteSystem(String[] sentences, int[] times) {
        this.map = new HashMap<>();
        searchterm = "";
        for (int i = 0; i < sentences.length; i++) {
            String curr = sentences[i];
            map.put(curr, map.getOrDefault(curr, 0) + times[i]);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            map.put(searchterm, map.getOrDefault(searchterm, 0) + 1);
            this.searchterm = "";
            return new ArrayList<>();
        }
        this.searchterm += c;

        for (String sentence : map.keySet()) {
            if (sentence.startsWith(searchterm)) {
                pq.add(sentence);
                if (pq.size() > 3) {
                    pq.poll();
                }
            }
        }

        List<String> li = new ArrayList();
        while (!pq.isEmpty()) {
            li.add(0, pq.poll());
        }
        return li;
    }
}

// Trie solution
// Time complexity: O(M log M + L) → O(1) where M ≤ 100 (max number of matching
// sentences per prefix), L is the length of the input string
// Space complexity: O(N) where N is the number of sentences
// This solution can be further optimized by caching top 3 results at each Trie
// node, so input() becomes O(L) — just walking down the trie to get the result
class AutocompleteSystem_Trie {
    private String searchTerm;
    private HashMap<String, Integer> map;
    private Trie trie;

    public AutocompleteSystem_Trie(String[] sentences, int[] times) {
        this.map = new HashMap<>();
        this.trie = new Trie();
        this.searchTerm = "";
        for (int i = 0; i < sentences.length; i++) {
            String sentence = sentences[i];
            trie.insert(sentence);
            map.put(sentence, times[i]);
        }
    }

    public List<String> input(char c) {
        if (c == '#') {
            map.put(searchTerm, map.getOrDefault(searchTerm, 0) + 1);
            trie.insert(searchTerm);
            searchTerm = "";
            return new ArrayList<>();
        }

        searchTerm += c;

        List<String> candidates = trie.search(searchTerm);

        PriorityQueue<String> pq = new PriorityQueue<>((a, b) -> {
            int countA = map.get(a);
            int countB = map.get(b);
            if (countA == countB) {
                return a.compareTo(b);
            }
            return countB - countA; // descending frequency
        });

        for (String sentence : candidates) {
            pq.offer(sentence);
        }

        List<String> result = new ArrayList<>();
        int k = 3;
        while (!pq.isEmpty() && k-- > 0) {
            result.add(pq.poll());
        }

        return result;
    }
}

class TrieNode {
    Map<Character, TrieNode> children;
    Set<String> sentences;

    public TrieNode() {
        children = new HashMap<>();
        sentences = new HashSet<>();
    }
}

class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public void insert(String sentence) {
        TrieNode curr = root;
        for (char ch : sentence.toCharArray()) {
            curr.children.putIfAbsent(ch, new TrieNode());
            curr = curr.children.get(ch);
            curr.sentences.add(sentence); // Add sentence at every step
        }
    }

    public List<String> search(String prefix) {
        TrieNode curr = root;
        for (char ch : prefix.toCharArray()) {
            if (!curr.children.containsKey(ch)) {
                return new ArrayList<>();
            }
            curr = curr.children.get(ch);
        }
        return new ArrayList<>(curr.sentences);
    }
}
