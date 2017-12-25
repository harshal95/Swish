package com.example.bharath.swish;

import java.util.LinkedList;
import java.util.List;

public class TrieNode {


	public TrieNode(char charecter,float prob) {

		this.character = charecter;
		this.prob=prob;
		this.childs = new LinkedList();

	}
	private float prob;
	private int count;
	private char character;
	private List childs;

	public float getProb(){
			return prob;
	}

	public void setProb(float temp){
		prob=temp;
	}

	public char getCharacter() {
		return character;
	}
	public void setCharacter(char character) {
		this.character = character;
	}
	public List getChilds() {
		return childs;
	}
	public void setChilds(List childs) {
		this.childs = childs;
	}

}
