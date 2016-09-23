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
import java.lang.reflect.Array;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static HashSet<String> dictionary;
    static HashSet<String> breadthVisited;
    static HashSet<String> deapthVisited;

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
        dictionary.addAll(temp);
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		if(keyboard.nextLine().equals("/quit"))
		    return new ArrayList<String>();
        ArrayList<String> value = new ArrayList<String>();
        value.add(keyboard.next()); value.add(keyboard.next());
		return null;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) { //recursive
		if(end.equals(start)) {
            ArrayList<String> value = new ArrayList<String>();
            value.add(end);
            return value;
        }
        ArrayList<String> value = new ArrayList<String>();
        String friend = "";
        deapthVisited.add(start);       //mark current word visited
        for(int i = 0; i<start.length(); i++){              //for every letter in start word
            StringBuilder sb = new StringBuilder(start);        //create a temp string
            for(char alpha = 'A'; alpha<='Z'; alpha++) {        //try all letters in alphabet
                if(alpha==sb.charAt(i)) //skip original word
                    continue;
                sb.setCharAt(i,alpha);                      //change the temp word
                friend = sb.toString();
                if (dictionary.contains(friend) && !deapthVisited.contains(friend)){        //if the temp word exists
                    value.addAll(getWordLadderDFS(friend,end));                        //go down that path to find end
                    if(!value.isEmpty()){       //if we found the end
                        value.add(start);
                        //resetvisited
                        return value;
                    }
                }
            }
        }
        return value;


        //find adjacent word

        //mark current word visited
        //got adjacent word
        //return this word plus adjacent word
        //if not a path return nothing


		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		//what? Set<String> dict = makeDictionary();
		//return null; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {  //iterative
        return getWordLadderBFS(new Node(start),new Node(end));
    }

    public static ArrayList<String> getWordLadderBFS(Node start, Node end){
        LinkedList<Node> Q = new LinkedList<Node>();                        //Queue implementation
        Q.push(start);
        while(!Q.isEmpty()) {
            Node root = Q.pop();
            breadthVisited.add(root.toString());
            for(int i = 0; i<root.length; i++) {              //for every letter in current word
                StringBuilder sb = new StringBuilder(root.toString());        //create a temp string
                for (char alpha = 'A'; alpha <= 'Z'; alpha++) {
                    if (alpha == sb.charAt(i)) //skip original word
                        continue;
                    sb.setCharAt(i, alpha);                      //change the temp word
                    Node neighbor = new Node(sb.toString());
                    if (dictionary.contains(neighbor.toString()) && !breadthVisited.contains(neighbor.toString())) {        //if the temp word exists
                        neighbor.setParent(root);
                        if (neighbor.equals(end)) {
                            ArrayList<String> value = new ArrayList<String>();
                            value.add(end.toString());
                            value.addAll(neighbor.getFamilyTree(root));
                            //resetvisited
                            return value;
                        }
                        Q.push(neighbor);
                    }
                }
            }
        }


        //create linked list (queue)
        //add start to queue
        //while not empty
        //pop()
        //for each neighbor
        //set parent to popped
        //if(neighbor == end)
        //return
        //else push neighbor






        /*
        create empty queue Q
 8
 9     root.distance = 0
10     Q.enqueue(root)
11
12     while Q is not empty:
13
14         current = Q.dequeue()
15
16         for each node n that is adjacent to current:
17             if n.distance == INFINITY:
18                 n.distance = current.distance + 1
19                 n.parent = current
20                 Q.enqueue(n)

*/











        //find all adjacent words
        //check to see if any are answer
        //mark this as visited
        //pick one and repeat

		//Set<String> dict = makeDictionary();
		
		return null; // replace this line later with real return
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
		
	}
	// TODO
	// Other private static methods here
}
