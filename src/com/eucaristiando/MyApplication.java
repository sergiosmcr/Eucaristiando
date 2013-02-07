package com.eucaristiando;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;

public class MyApplication extends Application {

	public CategoryType[] category;
	public List<CharSequence> availableSingers;
	public int singerCount;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		category = new CategoryType[11];
		for (int i=0; i<11; i++)
		{
			category[i] = new CategoryType();
		}
		singerCount = 0;
		availableSingers = new ArrayList<CharSequence>();
	}
	
	
	

	
    public void addNewSinger(String newSinger)
    {
    	availableSingers.add(newSinger);
    	singerCount++;
    }
    
    public void removeSinger(String singer)
    {
    	if (singerCount > 0)
    	{
    		availableSingers.remove(singer);
    		singerCount--;
    	}
    	
    }
    
    public void cleanup()
    {
    	availableSingers.clear();
    	singerCount = 0;
    }
	
	
}
