package com.example.bharath.swish;

import android.os.Environment;
import android.util.Log;

import java.util.*;
import java.io.IOException;
import java.io.FileReader;
import java.io.*;
import java.io.BufferedReader;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;

public class SearchTest {

	Trie trie = new Trie();

	String fileName = Environment.getExternalStorageDirectory()+"/dict.txt";
	String fileName1 = Environment.getExternalStorageDirectory()+"/proab.txt";

	public void createTrie(){

						try {

	            FileReader fileReader =new FileReader(fileName);
	            BufferedReader bufferedReader =new BufferedReader(fileReader);

							FileReader fileReader1 =new FileReader(fileName1);
							BufferedReader bufferedReader1 =new BufferedReader(fileReader1);

							String line=null;

	        while((line = bufferedReader.readLine()) != null) {

							float prob =Float.valueOf(bufferedReader1.readLine());
							//Log.d("Krazy:",line+"...prob.."+prob);
							trie.addWord(line,prob);
	        }

	            bufferedReader.close();
							bufferedReader1.close();
	        }
	        catch(FileNotFoundException ex) {
				Log.d("Krazy:", "Unable to reading file...");
	        }
	        catch(IOException ex) {
	            Log.d("Krazy:","Error reading file...");
	            // ex.printStackTrace();
	        }

		trie.addWord("barbie",0.0f);
		trie.addWord("barbiee",0.0f);
		trie.addWord("barbieee",0.0f);
		trie.addWord("barbieeee",0.0f);
		Log.d("KrazyFile:", fileName);
		Log.d("KrazyFile:",fileName1);

	}

	public void writeFile(String word){

		trie.addWord(word,1.0f);
		List<Score> matches = trie.search(word);

		Score obj=matches.get(0);

		float proab=obj.score;

		try{

			FileReader fileReader =new FileReader(fileName);
			BufferedReader bufferedReader =new BufferedReader(fileReader);

			FileReader fileReader1 =new FileReader(fileName1);
			BufferedReader bufferedReader1 =new BufferedReader(fileReader1);

			String line=null;
			int count=0;
			ArrayList<String> temp = new ArrayList<String>();

			//List<String> lines = Files.readAllLines(new File(fileName1).toPath());

			while((line = bufferedReader1.readLine()) != null) {
				//System.out.println(line);
				temp.add(line);
			}

			while((line = bufferedReader.readLine()) != null) {
				if(line.equals(word)){
					proab=proab/word.length();
					break;
				}
				count++;
			}
			System.out.println(temp.size());
			temp.set(count,""+proab);
			//	Files.write(new File(fileName1).toPath(), lines);

			bufferedReader.close();
			bufferedReader1.close();

			FileWriter fileWriter =new FileWriter(fileName1);
			BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);

			for(String str:temp){
				bufferedWriter.write(str);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();

		}catch(FileNotFoundException ex) {
			System.out.println(
					"Unable to open file '" +
							fileName + "'");
		}
		catch(IOException ex) {
			System.out.println(
					"Error reading file '"
							+ fileName + "'");
		}


	}

	public void repair(){

		try{

			FileWriter fileWriter =new FileWriter(fileName1);
			BufferedWriter bufferedWriter =new BufferedWriter(fileWriter);

			FileReader fileReader =new FileReader(fileName);
			BufferedReader bufferedReader =new BufferedReader(fileReader);

			String line=null;

			while((line = bufferedReader.readLine()) != null) {

						bufferedWriter.write(""+0.0f);
						bufferedWriter.newLine();

			}
			bufferedReader.close();
			bufferedWriter.close();

		}catch(FileNotFoundException ex) {
				System.out.println(
						"Unable to open file '" +
						fileName + "'");
		}
		catch(IOException ex) {
				System.out.println(
						"Error reading file '"
						+ fileName + "'");
				// ex.printStackTrace();
		}


	}

	public List find(String word) {


		List<Score> matches = trie.search(word);
		if(matches==null || matches.size() == 0)
		{
			System.out.println("Not found");
			return matches;
		}
		else
		{
			for(Score str:matches)
			{
				System.out.println(str);
			}
			Collections.sort(matches);
			return matches;
		}

	}

}
