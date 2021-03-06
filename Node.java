/* WORD LADDER Node.java
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

import java.util.ArrayList;

public class Node{
    Node parent;
    String s;
    int length;
    public Node(String s){
        this.s = s;
        length = s.length();
    }

    public Node setParent(Node n){
        parent = n;
        return this;
    }

    public String toString(){
        return s;
    }

    public boolean equals(Object o){
        if(!(o instanceof Node))
            return false;
        return(((Node) o).s.equals(s));
    }

    public ArrayList<String> getFamilyTree(Node child){
        ArrayList<String> value = new ArrayList<String>();
        value.add(child.toString());
        if(child.parent!=null)
            value.addAll(getFamilyTree(child.parent));
        return value;
    }
}
