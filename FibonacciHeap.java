import java.util.*;
import java.util.HashMap;


public class FibonacciHeap
{   
	/** Create a Hashmap containing String(i.e Hastag) as key and reference to Heap Node as Value.
	FHeapNode is Node of Fibonacci Heap.**/
	
    HashMap<String,FHeapNode> hmap = new HashMap<String,FHeapNode>();
  
    private FHeapNode maxNode;														//Points to the Max node in the heap. 
    private int nNodes;																//Number of nodes in the heap.   
	
    public FibonacciHeap()															//Constructor-Constructs a FibonacciHeap object that contains no elements.
    {
    } 
    
     /** Increases the key value for a heap node x, k is the value to be added to the Key of hastag.
		If the value of the increased node is greater than its parent, the incremented
		node will be moved to root, but meld operation will not be performed.**/
	 
    public void increaseKey(FHeapNode x, int k)
    {
        x.key = x.key + k;															//increment value of x.key
        FHeapNode y = x.parent;		

        if ((y != null) && (x.key > y.key))											//remove node x and its children if incremented key value is greater than its parent
		{
            cut(x, y);																//remove node x from parent node y, add x to root list of heap.
            cascadingCut(y);														//check the child cut value of parent y and perform cascading cut if required
        }

        if (x.key > maxNode.key)													//update maxnode pointer if necessary
		{
            maxNode = x;
        }
    }
  
     /**Inserts a new data element into the heap. No meld operation is
        performed at this time, the new node is simply inserted into the root
        list of this heap**/
     
    public FHeapNode insert(String hashtag, int key)
    {		
    	if(hmap.containsKey(hashtag))												//if hashtag is already present in the hashmap, 
    	return hmap.get(hashtag);													//return the value of hashmap i.e the node containing the hashtag.
    	
		FHeapNode newNode=new FHeapNode(hashtag,key);											      											
        if (maxNode != null) 														// add node to root list of fibonacci heap
		{
            newNode.left = maxNode;
            newNode.right = maxNode.right;
            maxNode.right = newNode;
            newNode.right.left = newNode;

            if (key > maxNode.key) 
			{
                maxNode = newNode;
            }
        } 
		else
		{
            maxNode = newNode;														//current node is the first node to be inserted
        }
        hmap.put(hashtag,newNode);													 //add the hashtag and its frequency to hash table
        nNodes++;
        return null;
    }

    /** Removes the smallest element from the heap. This will cause the trees in
       the heap to be consolidated, if necessary.**/
     
    public FHeapNode removeMax()
    {
        FHeapNode z = maxNode;
        //hmap.remove(maxNode.gethashtag());										//remove the node from the hashtable
       
        if (z != null) 
		{
        	 for (HashMap.Entry<String, FHeapNode> entry : hmap.entrySet()) 
             {
             	if(entry.getValue() == maxNode)
             	{
             		hmap.remove(entry.getKey());
             		break;
             	}
             }
        	 
            int numKids = z.degree;
            FHeapNode x = z.child;
            FHeapNode tempRight;

            while (numKids > 0) 													// iterate trough all the children of the max node
			{
                tempRight = x.right;             
                x.left.right = x.right;												// remove x from child list
                x.right.left = x.left;
                
                x.left = maxNode;													// add x to root list of heap
                x.right = maxNode.right;
                maxNode.right = x;
                x.right.left = x;
               
                x.parent = null;													// set parent[x] to null
                x = tempRight;
                numKids--;
            }
			z.left.right = z.right;													// remove z from root list of heap
            z.right.left = z.left;

            if (z == z.right)
			{
                maxNode = null;
            } 
			else 
			{
                maxNode = z.right;
                meld();
            }         
            nNodes--;																// decrement size of heap
        }
        

        return z;
    }  
	
	protected void cut(FHeapNode x, FHeapNode y)				 								//remove x from childlist of y and decrement degree[y]
    {
       
        x.left.right = x.right;																	//update the pointers of nodes to the left and right of x
        x.right.left = x.left;
        y.degree--;																				//decrease the degree of y, to reflect removal of child node x

        if (y.child == x) 																		//reset child pointer of y if necessary
		{
            y.child = x.right;
        }
		if (y.degree == 0) 
		{
            y.child = null;
        }      
        x.left = maxNode;										 								// add x to root list of heap, to the right of maxnode
        x.right = maxNode.right;																// update the pointers of x and maxnode
        maxNode.right = x;
        x.right.left = x;      
        x.parent = null;																		// set parent[x] to nil
        
        x.childCut = false; //?																	// set childCut[x] to false
    }

    /** Performs a cascading cut operation if the value of childcut of node y is true.
	   This cuts y from its parent and then does the same for its parent, and so on up the tree.*/
	   
    protected void cascadingCut(FHeapNode y)
    {
        FHeapNode z = y.parent;																	// node z is the parent of node y
        if (z != null) 																			// if y is not root and y has a parent
		{
            if (!y.childCut) 																	// if childCut value of y is false, set it true.
			{
                y.childCut = true;
            } 
			else 																				// if childcut value is true
			{
                cut(y, z);																		// cut node y from its parent               
                cascadingCut(z);																// check the child cut value of its parent as well and continue cascading cut operation
            }
        }
    }
    protected void meld()
    {		
        int arraySize = ((int) Math.floor(Math.log(nNodes) * (1.0 / Math.log((1.0 + Math.sqrt(5.0)) / 2.0)))) + 1;
        List<FHeapNode> array = new ArrayList<FHeapNode>(arraySize);      						
        for (int i = 0; i < arraySize; i++) 													// Initialize the degree array
		{
            array.add(null);
        }       
        int numRoots = 0;																		// Find the number of root nodes.
        FHeapNode x = maxNode;
        if (x != null)
		{
            numRoots++;
            x = x.right;

            while (x != maxNode)
			{
                numRoots++;
                x = x.right;
            }
        }																						// numRoots contains the number of root nodes
		while (numRoots > 0)																	// iterate trough each node in root list
		{          
            int d = x.degree;																	// Access the degree of the node
            FHeapNode next = x.right;          
            for (;;) 																			// iterate trough the array to see if there's another node of the same degree.
			{ 
                FHeapNode y = array.get(d);
                if (y == null) 
				{
                    break;										
                }
                if (x.key < y.key)																// If there is a node of same degree make one of the nodes a child of the other based on key value
				{
                    FHeapNode temp = y;
                    y = x;
                    x = temp;
                }				
                makeChild(y, x);																// Make node y a child of node x.             
                array.set(d, null);																// This degree is handled, go to next one.
                d++;
            }          
            array.set(d, x);																	// Save this node for later when we might encounter another of the same degree.            
            x = next;																			// Move forward through list.
            numRoots--;
        }

        /** Set max to null (effectively losing the root list) and reconstruct the root list from the array entries in array.**/
        maxNode = null;

        for (int i = 0; i < arraySize; i++)
		{
            FHeapNode y = array.get(i);
            if (y == null)
			{
                continue;
            }            
            if (maxNode != null)								
			{
                y.left.right = y.right;															// First remove node from root list.
                y.right.left = y.left;
                
                y.left = maxNode;																// Now add to root list, again.
                y.right = maxNode.right;
                maxNode.right = y;
                y.right.left = y;

                if (y.key > maxNode.key)														// Check if this is a new max.
				{
                    maxNode = y;
                }
            } 
			else 
			{
                maxNode = y;
            }
        }
    }
	
	/** Make node y a child of node x.**/
    protected void makeChild(FHeapNode y, FHeapNode x)
    {        
        y.left.right = y.right;																		// remove y from root list of heap
        y.right.left = y.left;
        
        y.parent = x;																				// make y a child of x

        if (x.child == null)
		{
            x.child = y;
            y.right = y;
            y.left = y;
        } 
		else
		{
            y.left = x.child;
            y.right = x.child.right;
            x.child.right = y;
            y.right.left = y;
        }			
        x.degree++;																					// increase degree[x]        
        y.childCut = false;																			// set childCut[y] to false
    }

        
}



 