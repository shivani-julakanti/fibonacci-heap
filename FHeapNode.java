/*Implements a node of the Fibonacci heap. */
 
public class FHeapNode
{
	int key;																						//frequency of hashtag
	String hashtag;			//String hashtag
	int degree; 			//number of children of this node 
    	FHeapNode child; 		//first child node
    	FHeapNode left; 		//left sibling node
    	FHeapNode parent;		//parent node
    	FHeapNode right;		//right sibling node
	boolean childCut; 		
	/** childCut-indicates child cut value-true if this node has 
	* had a child removed since this node was added to its parent*/
	   
     /* Default constructor. Initializes the right and left pointers, making this
      * a circular doubly-linked list. */
	  
    public FHeapNode(String S,int key)
    {
        right = this;
        left = this;
        this.key = key;
	this.hashtag = S;
    }

    //returns key value of the node
    public final int getKey()
    {
        return key;
    }

    //returns hashtag value of the node
    public final String gethashtag()
    {
        return hashtag;
    }
}


