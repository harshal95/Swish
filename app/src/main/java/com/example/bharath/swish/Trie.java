package com.example.bharath.swish;

import java.util.List;
import java.util.ListIterator;
import java.util.*;


class Score implements Comparable<Score> {
    float score;
    String name;

    public Score(String name,float score) {
        this.score = score;
        this.name = name;
    }

    @Override
    public int compareTo(Score o) {
        return score > o.score ? -1 : score < o.score ? 1 : 0;
    }

		public String toString() {
    return "Word: " + this.name +
           ", Count: " + this.score;
}
}

public class Trie {

  public static float dec_prob=0.0f;
	private static float dec_temp=0.0f;

	private TrieNode root;

	/**
	 *  Add the give word into the Trie
	 * @param word
	 */
	public void addWord(String word,float prob)
	{
		if(root == null)
		{
			root = new TrieNode(' ',0.0f);
		}
		TrieNode start = root;
		char[] charecters = word.toCharArray();
		for(char c : charecters)
		{
			if( start.getChilds().size() == 0)
			{
				TrieNode newNode = new TrieNode(c,prob);
				start.getChilds().add(newNode);
				start = newNode;
			}
			else
			{
				ListIterator iterator = start.getChilds().listIterator();
				TrieNode node=null;
				while(iterator.hasNext())
				{
					node = (TrieNode)iterator.next();
					if(node.getCharacter() >= c)
						break;
				}
				if(node.getCharacter() == c)
				{
					start = node;
					start.setProb(prob+start.getProb());
				}
				else
				{
					TrieNode newNode = new TrieNode(c,prob);
					iterator.add(newNode);
					start = newNode;

				}
			}
		}

	}

	/**
	 *  This method takes and prefix string and returns all possible string that can be formed from the given prefix
	 * @param prefix
	 * @return
	 */
/*	public List search(String prefix)
	{
		if(prefix == null || prefix.isEmpty())
			return null;

		char[] chars = prefix.toCharArray();
		TrieNode start = root;
		boolean flag = false;
		for(char c : chars)
		{
			if(start.getChilds().size() > 0)
			{
				ListIterator iterator = start.getChilds().listIterator();
				TrieNode node=null;
				while(iterator.hasNext())
				{
					node = (TrieNode)iterator.next();
					if(node.getCharacter() == c)
					{
						start = node;
            dec_prob=dec_prob+start.getProb();
						flag=true;
						break;
					}
				}
			}
			else
			{
				flag = false;
				break;
			}
		}

		if(flag)
		{
			dec_temp=dec_prob;
			List matches = getAllWords(start,prefix);
			return matches;
		}

		return null;
	}

	/**
	 *  This method returns list string that can be formed from the given node
	 * @param start : Node from to start
	 * @param prefix : String prefix that was formed till start node
	 * @return
	 */
	/*private List getAllWords(TrieNode start,final String prefix)
	{

		if(start.getChilds().size() == 0)
		{
			List<String,int> list = new LinkedList();
			list.add(prefix);

			System.out.println("     "+dec_prob);
			System.out.println("List "+list);
			return list;
		}
		else
		{
			List list = new LinkedList();
			ListIterator iterator = start.getChilds().listIterator();
			TrieNode n=null;
			while(iterator.hasNext())
			{
				n = (TrieNode)iterator.next();
       	dec_prob=dec_temp+n.getProb();
				dec_temp=dec_prob;
				list.addAll(getAllWords(n, prefix+n.getCharacter()));
				//System.out.println(dec_prob);
			}

			return list;
		}*/

		public List search(String prefix)
		{
			if(prefix == null || prefix.isEmpty())
				return null;

			char[] chars = prefix.toCharArray();
			TrieNode start = root;
			boolean flag = false;
			for(char c : chars)
			{
				if(start.getChilds().size() > 0)
				{
					ListIterator iterator = start.getChilds().listIterator();
					TrieNode node=null;
					while(iterator.hasNext())
					{
						node = (TrieNode)iterator.next();
						if(node.getCharacter() == c)
						{
							start = node;
	            dec_prob=dec_prob+start.getProb();
							flag=true;
							break;
						}
						else
							flag=false;
					}
				}
				else
				{
					flag = false;
					break;
				}
			}

			if(flag)
			{
				dec_temp=dec_prob;
				List<Score> matches = getAllWords(start,prefix);
				return matches;
			}

			return null;
		}

		/**
		 *  This method returns list string that can be formed from the given node
		 * @param start : Node from to start
		 * @param prefix : String prefix that was formed till start node
		 * @return
		 */
		private List getAllWords(TrieNode start,final String prefix)
		{

			if(start.getChilds().size() == 0)
			{
				List<Score> list = new ArrayList<Score>();
				list.add(new Score(prefix,dec_prob));

				System.out.println("     "+dec_prob);
				System.out.println("List "+list);
				return list;
			}
			else
			{
				List<Score> list = new ArrayList<Score>();
				ListIterator iterator = start.getChilds().listIterator();
				TrieNode n=null;
				while(iterator.hasNext())
				{
					n = (TrieNode)iterator.next();
	       	dec_prob=dec_temp+n.getProb();
					dec_temp=dec_prob;
					list.addAll(getAllWords(n, prefix+n.getCharacter()));
					//System.out.println(dec_prob);
				}

				return list;
			}

}
}
