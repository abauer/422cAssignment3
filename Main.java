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

    @Test
    public void test1(){
        initialize();
        ArrayList<String> ladder = new ArrayList<String>();
        ladder.add("money"); ladder.add("stone");
        printLadder(ladder);
    }

    @Test
    public void test2(){
        initialize();
        ArrayList<String> ladder = new ArrayList<String>();
        ladder.add("smart"); ladder.add("money");
        printLadder(ladder);
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
		
		// TODO methods to read in words, output ladder
	}
	
	public static void initialize() {
        Set<String> temp = makeDictionary();
        dictionary = new HashSet<String>();
        deapthVisited = new HashSet<String>();
        breadthVisited = new HashSet<String>();
        dictionary.addAll(temp);
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
        char[] input = keyboard.nextLine().trim().toCharArray();
		if(String.copyValueOf(input).equals("/quit"))
		    System.exit(0);
        StringBuilder sb = new StringBuilder(input.length);
        StringBuilder sb2 = new StringBuilder(input.length);
        int i = 0;
        for(; i<input.length; i++){
            if(input[i]=='\n'||input[i]==9||input[i]==32){
                break;
            }
            sb.append(input[i]);
        }
        for(; i<input.length; i++){
            if(input[i]=='\n'||input[i]==9||input[i]==32){
                break;
            }
            sb2.append(input[i]);
        }
        ArrayList<String> value = new ArrayList<String>();
        value.add(sb.toString().toUpperCase()); value.add(sb2.toString().toUpperCase());
        return value;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) { //recursive
        return getWordLadderDFS(start.toUpperCase(), end.toUpperCase(), 0);
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
		List<String> value = getWordLadderBFS(ladder.get(0),ladder.get(1));
        //System.out.println(ladder.get(0));
        for(int i = value.size()-1; i>0; i--){
            System.out.println(value.get(i));
        }
	}

    private static ArrayList<String> getWordLadderDFS(String start, String end, int lastChanged){
        if(end.equals(start)) {
            ArrayList<String> value = new ArrayList<String>();
            value.add(end);
            return value;
        }
        ArrayList<String> value = new ArrayList<String>();
        String friend = "";
        deapthVisited.add(start);       //mark current word visited
        for(int index = 0; index<start.length(); index++){              //for every letter in start word
            int i = (index+lastChanged)%start.length();
            StringBuilder sb = new StringBuilder(start);        //create a temp string
            for(char alpha = 'A'; alpha<='z'; alpha++) {        //try all letters in alphabet
                if(alpha==sb.charAt(i)) //skip original word
                    continue;
                sb.setCharAt(i,alpha);                      //change the temp word
                friend = sb.toString().toUpperCase();
                if (dictionary.contains(friend) && !deapthVisited.contains(friend)){        //if the temp word exists
                    value.addAll(getWordLadderDFS(friend,end,i));                        //go down that path to find end
                    if(!value.isEmpty()){       //if we found the end
                        value.add(start);
                        //resetvisited
                        return value;
                    }
                }
            }
        }
        return value;
    }

    private static ArrayList<String> getWordLadderBFS(Node start, Node end){
        LinkedList<Node> Q = new LinkedList<Node>();                        //Queue implementation
        Q.push(start);
        breadthVisited.add(start.toString());
        while(!Q.isEmpty()) {
            Node root = Q.remove();
            if(root.equals(end)){
                ArrayList<String> value = new ArrayList<String>();
                value.add(end.toString());
                value.addAll(root.getFamilyTree(root));
                breadthVisited.clear();
                return value;
            }
            for(int i = 0; i<root.length; i++) {              //for every letter in current word
                StringBuilder sb = new StringBuilder(root.toString());        //create a temp string
                for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
                    if (alpha == root.toString().charAt(i)) //skip original word
                        continue;
                    sb.setCharAt(i, alpha);                      //change the temp word
                    Node neighbor = new Node(sb.toString().toUpperCase());
                    if (dictionary.contains(neighbor.toString())) {
                        if (!breadthVisited.contains(neighbor.toString())) {        //if the temp word exists
                            neighbor.setParent(root);
                            Q.add(neighbor);
                            breadthVisited.add(neighbor.toString());
                        }
                    }
                }
            }
        }
        return new ArrayList<String>();
    }
}
