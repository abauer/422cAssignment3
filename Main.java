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

import java.util.*;
import java.io.*;

public class Main {

	static HashSet<String> dictionary;
    static HashSet<String> breadthVisited;
    static HashSet<String> deapthVisited;
    static HashMap<String,HashSet<String>> adjacents;
    static String startField;
    static String endField;

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
		initialize();           //init
        ArrayList<String> temp = parse(kb); //parse input
        ArrayList<String> result = getWordLadderBFS(temp.get(0),temp.get(1));   //obtain wordladder from BFS
        /*if(result.isEmpty())
		    printLadder(temp);
        else*/
            printLadder(result);       //print result
	}
	
	public static void initialize() {
        Set<String> temp = makeDictionary();        //get Dictionary from provided method
        dictionary = new HashSet<String>();
        deapthVisited = new HashSet<String>();
        breadthVisited = new HashSet<String>();
        adjacents = new HashMap<String,HashSet<String>>();
        temp.stream().forEach(word -> dictionary.add(word.toUpperCase()));        //awesomeness at work //store in global dictionary
        for(String word : dictionary) {                 //for every word
            HashSet<String> words = new HashSet<String>();      //create a Hash set of adjacents
            for(int i =0; i<word.length(); i++) {       //for every letter in the word
                StringBuilder sb = new StringBuilder(word);     //create a temporary String
                for (char alpha = 'A'; alpha <= 'Z'; alpha++) {     //change that letter to one of the other 25
                    if (alpha == word.charAt(i))
                        continue;
                    sb.setCharAt(i, alpha);
                    if (dictionary.contains(sb.toString())) {       //if temporary is in dicitonary
                        words.add(sb.toString().toUpperCase());     //add it to adjacetns
                    }
                }
            }
            HashSet<String> values = adjacents.get(word);   //get the current HashSet of word adjacent to 'word'
            if(values==null){                           //if there are none yet
                adjacents.put(word,words);      //directly put the list into the HashMap
            }
            else {
                values.addAll(words);           //otherwise add the list to the existing list
            }
        }
    }

	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList.
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
        String input = keyboard.nextLine().trim().toUpperCase();    //make all upper case for simplicity
		if(input.contains("/QUIT"))         //look for quit command
		    System.exit(0);
        char[] chars = input.toCharArray();     //prepare to parse for two words
        StringBuilder sb = new StringBuilder(chars.length); //first word
        int i = 0;  //index in input string
        for(; i<chars.length; i++){ //until we see a space (len of first word)
            if(chars[i]=='\n'||chars[i]==9||chars[i]==32){
                break;
            }
            sb.append(chars[i]);        //add to the first word
        }
        String sb2 = new String(input.substring(i+1).trim()); //cut the array so that we get the second word
        ArrayList<String> value = new ArrayList<String>();      //make an arraylist for these words
        value.add(sb.toString()); value.add(sb2);       //add both to arraylist
        return value;       //return arraylist
	}

	public static ArrayList<String> getWordLadderDFS(String start, String end) {
        if (start.equals(end)) {        //if the words are equal (not tested)
            return new ArrayList<String>();     //return empty array
        }
        start = start.toUpperCase(); end = end.toUpperCase();  //make sure these are Upper case
        startField = start;      //set the global fields for printing when no ladder exists
        endField = end;
        deapthVisited.clear();      //clear the list of visited words
        return getWLDFS(start, end);    //start recursion
    }
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {  //iterative
        breadthVisited.clear();     //clear the list of vistied words
        startField = start;      //set the global fields for printing when no ladder exists
        endField = end;
        return getWordLadderBFS(new Node(start.toUpperCase()),new Node(end.toUpperCase())); //start iteration
    }
    
	public static Set<String>  makeDictionary () {  //provided function
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
	
	public static void printLadder(ArrayList<String> ladder) {      //prints the word ladder
        if(ladder.size()==0){       //only if no ladder exists
            System.out.println("no word ladder can be found between "+startField.toLowerCase()+" and "+endField.toLowerCase());
            return;
        }
        System.out.println("a "+(ladder.size()-2)+"-rung word ladder exists between "+startField.toLowerCase()+" and "+endField.toLowerCase()+".");     //0+ ladders print
        for(int i = ladder.size()-1; i>=0; i--){
            System.out.println(ladder.get(i).toLowerCase());    //print all the words of ladder
        }
	}

    private static ArrayList<String> getWordLadderBFS(Node start, Node end){
        if(start.equals(end)){      //if start equals en (not tested)
            return new ArrayList<String>(); // return empty array
        }
        ArrayDeque<Node> Q = new ArrayDeque<Node>();                        //Queue implementation
        Q.push(start);      //add first word to Q
        breadthVisited.add(start.toString());   //add first word to visited
        while(!Q.isEmpty()) {       //while there is a possible path
            Node root = Q.remove();     //remove first element from Q
            if(root.equals(end)){   //if that element is the end
                ArrayList<String> value = new ArrayList<String>();  //create ArrayList
                value.addAll(root.getFamilyTree(root)); //add end and all its parents to Array
                breadthVisited.clear(); //clear the visited list
                return value;       //return the Array
            }
            HashSet<String> nextWords = adjacents.get(root.toString()); //get list of adjacent words from hashmap
            if(nextWords.contains(end.toString())){ //if that set of words contains the end word
                Q.push(end.setParent(root));    //put the end word at the front of the Q (setting parent to this word)
            }
            //for each word that has not been visited yet - mark that word visited and add it to Q (setting parent to this word)
            nextWords.stream().filter(word -> !breadthVisited.contains(word)).forEach(word -> {breadthVisited.add(word); Q.add(new Node(word).setParent(root));});
        }
        return new ArrayList<String>(); //if no ladder is found return empty ArrayList
    }

    private static ArrayList<String> getWLDFS(String start, String end) {   //recursive
        ArrayList<String> value = new ArrayList<String>();      //value to return
        if(start.equals(end)){          //if we found the end
            value.add(end);         //add the end to the list
            deapthVisited.remove(end);      //remove from visited list
            return value;           //return the ArrayList
        }
        if(deapthVisited.contains(start)){      //if weve visited this word return
            return value;
        }
        deapthVisited.add(start);       //mark current word visited
        HashSet<String> nextWords = adjacents.get(start);       //get the adjacent words from hashmap
        if(nextWords.contains(end)){        //if this word is adjacent to the end
            value = getWLDFS(end,end);      //go to the end
            value.add(start);               //add this word to list
            deapthVisited.remove(start);        //remove this word from visited
            return value;               //return the array
        }
        for(String word:nextWords){     //for every adjacent word
            if(!deapthVisited.contains(word)) {     //if we have not visited that word
                value = getWLDFS(word, end);        //call DFS on that word
                if (!value.isEmpty()) {             //if we recieved a return that is not empty (found the end)
                    value.add(start);               //add this word to array
                    deapthVisited.remove(start);    //remove this word from visited
                    return value;                   //return this array
                }
            }
        }
        return value;       //return empty array for no ladder
    }

    private void printAdjacents(){  //helper method for visiualizing graph
        Iterator it = adjacents.entrySet().iterator();
        try {
            PrintStream ps = new PrintStream(new File("adjacents2.txt"));
            System.setOut(ps);
        } catch (IOException e){
            e.printStackTrace();
        }

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.print(pair.getKey()+" : ");
            ((HashSet<String>)pair.getValue()).stream().forEach(word -> System.out.print(word+", "));
            System.out.println();
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
