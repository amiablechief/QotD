package com.seriousplay.qotd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.os.Bundle;
import android.app.Activity;
import android.content.res.AssetManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		EditText textToScroll = (EditText) findViewById(R.id.qotdview);
		textToScroll.setMovementMethod(ScrollingMovementMethod.getInstance());
	}

	private AssetManager getApplicationAssets() {
		// open random quotes file
		AssetManager assetmanager = getAssets();
		return assetmanager;
	}

	private String getAssetPath(AssetManager assetmanager) {
		String[] dirs = null;
		String[] files = null;
		String path = null;
		
		try {
			dirs = assetmanager.list("");	//get list of files / dirs from the project 'assets' directory
			files = assetmanager.list(dirs[2]);	//Directories are listed in alphabetical order so fetch the 'txt' directory
			path = dirs[2].toString() + "/" + files[0].toString();	//construct the path (there is only 1 file in the dir)
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}

	// Get the path for the random quote file
	private InputStreamReader getQuoteReader() throws IOException {
		// open random quotes file
		AssetManager assets = getApplicationAssets();
		String path = null;
		path = getAssetPath(assets);
		InputStream inputStream = null;

		try {
			inputStream = assets.open(path);
			Log.v("QotD path", path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStreamReader textReader = new InputStreamReader(inputStream);
		return textReader;
	}

	// Get the total number of lines in the file
	private int getFileLineCount(InputStreamReader textReader) {
		BufferedReader br = new BufferedReader(textReader);
		int lineCount = 0;
		try {
			while ((br.readLine()) != null) {
				lineCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lineCount; // total number of lines in the text file
	}

	// Return a random line number from where to get the corresponding quote
	// string
	private int getRandomLineNumber(int totalLines) {
		Random rand = new Random();
		return rand.nextInt(totalLines);
	}

	private String getRandomQuote(int lineToFetch)
			throws IOException {
		//1. get path
		AssetManager assets = getApplicationAssets();
		String path = null;
		path = getAssetPath(assets);
		
		//2. open assets
		InputStream stream = assets.open(path);
		InputStreamReader randomQuote = new InputStreamReader(stream);
		
		//3. Get BufferedReader object
		BufferedReader buf = new BufferedReader(randomQuote);

		String quote = null; 
		String line = null;
		int currLine = 0;

		//4. Loop through using the new InputStreamReader until a match is found
		while ((line = buf.readLine()) != null) {
			// Get a random line number
			if (currLine == lineToFetch) {
				quote = line;
				Log.v("LINE", line);
				randomQuote.close();
				buf.close();
				return quote;
			} else
				currLine++;
		}
		randomQuote.close();
		buf.close();
		return quote;
	}

	// Set the EditText widget to display the new random quote
	private void displayQuote(String quote) {
		EditText quoteDisplay = (EditText) findViewById(R.id.qotdview);
		quoteDisplay.setText(quote);
	}

	// onClick handler for the button click
	public void fetchQotD(View view) throws IOException {
		// open random quotes file
		InputStreamReader textReader = getQuoteReader();

		final int totalLines = getFileLineCount(textReader);
		int lineToFetch = 0;
		String quote = null;
		//String path = null;

		// We want to get the quote at the following line number
		lineToFetch = getRandomLineNumber(totalLines);
		
		//Fine until this point in the program//
		
		quote = getRandomQuote(lineToFetch);

		displayQuote(quote);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}