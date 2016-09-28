/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Anthony Bauer
 * amb6869
 * 16840
 * Bryan Leon
 * bal2457
 * 16840
 * Slip days used: 0
 * Git URL: https://github.com/abauer/422cAssignment3
 * Fall 2016
 */


package assignment3;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static HashSet<String> dictionary;
    static HashSet<String> breadthVisited;
    static HashSet<String> deapthVisited;
    static HashMap<String,ArrayList<String>> adjacents;

    @Test
    public void initTest(){
        initialize();
    }

    @Test
    public void testb1(){
        initialize();
        printLadder(getWordLadderBFS("money","stone"));
    }
    @Test
    public void testb2(){
        initialize();
        printLadder(getWordLadderBFS("smart","money"));
    }
    @Test
    public void testb3(){
        initialize();
        printLadder(getWordLadderBFS("peach","wooed"));
    }
    @Test
    public void testb4(){   //confirms that dfs gives a suboptimal result
        initialize();
        printLadder(getWordLadderBFS("wrote","amigo"));
    }
    @Test
    public void testb5(){   //confirms that DFS result of no ladder is correct
        initialize();
        printLadder(getWordLadderBFS("whump","xylan"));
    }



    @Test
    public void testd1(){//// TODO: 9/27/2016
        initialize();
        printLadder(getWordLadderDFS("SMART","MONEY"));
    }
    @Test
    public void testd2(){//// TODO: 9/27/2016
        initialize();
        printLadder(getWordLadderDFS("MONEY","STONE"));
    }
    @Test
    public void testd3(){
        initialize();
        printLadder(getWordLadderDFS("wrote","amigo"));
    }
    @Test
    public void testd4(){ // test to determine that dfs cannot find this word ladder
        initialize();
        printLadder(getWordLadderDFS("whump","xylan"));
    }
    @Test
    public void testd5(){// TODO: 9/27/2016
        initialize();
        printLadder(getWordLadderDFS("wrote","amigo"));
    }


	public static void main(String[] args) throws Exception {

		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		printLadder(parse(kb));
	}
	
	public static void initialize() {
        Set<String> temp = makeDictionary();
        dictionary = new HashSet<String>();
        deapthVisited = new HashSet<String>();
        breadthVisited = new HashSet<String>();
        adjacents = new HashMap<String,ArrayList<String>>();
        temp.stream().forEach(word -> dictionary.add(word));        //awesomeness at work
        for(String word : temp){
            ArrayList<String> words = new ArrayList<String>();
            for(int i =0; i<word.length(); i++) {
                StringBuilder sb = new StringBuilder(word);
                for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
                    if (alpha == word.charAt(i))
                        continue;
                    sb.setCharAt(i, alpha);
                    if (dictionary.contains(sb.toString())) {
                        words.add(sb.toString().toUpperCase());
                    }
                }
            }
            ArrayList<String> values = adjacents.get(word);
            if(values==null){
                adjacents.put(word,words);
            }
            else{
                values.addAll(words);
            }
        }
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
        String input = keyboard.nextLine().trim().toUpperCase();
		if(input.contains("/QUIT"))         //look for quit command
		    System.exit(0);
        char[] chars = input.toCharArray();     //prepare to parse for two words
        StringBuilder sb = new StringBuilder(chars.length); //first word
        int i = 0;  //index in input string
        for(; i<chars.length; i++){ //until we see a space (len of first word)
            if(chars[i]=='\n'||chars[i]==9||chars[i]==32){
                break;
            }
            sb.append(chars[i]);
        }
        String sb2 = new String(input.substring(i+1).trim());
        ArrayList<String> value = new ArrayList<String>();
        value.add(sb.toString()); value.add(sb2);
        return value;
	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
        if (start.equals(end)) {
            return new ArrayList<String>();
        }
        return getWLDFS(start, end);
    }
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {  //iterative
        return getWordLadderBFS(new Node(start.toUpperCase()),new Node(end.toUpperCase()));
    }
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
        if(ladder.isEmpty()){ //TODO how to get start and finish?
            System.out.println("no word ladder can be found between <start> and <finish>");
            return;
        }
        System.out.println("a "+(ladder.size()-2)+"-rung word ladder exists between "+ladder.get(ladder.size()-1).toLowerCase()+" and "+ladder.get(0).toLowerCase()+".");
        for(int i = ladder.size()-1; i>=0; i--){
            System.out.println(ladder.get(i).toLowerCase());
        }
	}

    private static ArrayList<String> getWordLadderBFS(Node start, Node end){
        if(start.equals(end)){
            return new ArrayList<String>();
        }
        LinkedList<Node> Q = new LinkedList<Node>();                        //Queue implementation
        Q.push(start);
        breadthVisited.add(start.toString());
        while(!Q.isEmpty()) {
            Node root = Q.remove();
            if(root.equals(end)){
                ArrayList<String> value = new ArrayList<String>();
                value.addAll(root.getFamilyTree(root));
                breadthVisited.clear();
                return value;
            }
            ArrayList<String> nextWords = adjacents.get(root.toString());
            if(nextWords.contains(end.toString())){
                Q.push(end.setParent(root));
            }
            nextWords.stream().filter(word -> !breadthVisited.contains(word)).forEach(word -> {breadthVisited.add(word); Q.add(new Node(word).setParent(root));});
        }
        return new ArrayList<String>();
    }

    private static ArrayList<String> getWLDFS(String start, String end) {
        start = start.toUpperCase(); end = end.toUpperCase();
        ArrayList<String> value = new ArrayList<String>();      //value to return
        if(start.equals(end)){
            value.add(end);
            return value;
        }
        if(deapthVisited.contains(start)){
            return value;
        }
        deapthVisited.add(start);
        ArrayList<String> nextWords = adjacents.get(start);
        if(nextWords.contains(end)){
            getWLDFS(end,end);
        }
        for(String word:nextWords){
            if(!deapthVisited.contains(word)) {
                value = getWLDFS(word, end);
                if (!value.isEmpty()) {
                    value.add(start);
                    return value;
                }
            }
        }
        return value;
    }
}
