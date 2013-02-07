package com.eucaristiando;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class MainActivity extends ListActivity {

	
	public String[] categories;
	private MyApplication myApp;
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        
        // First paramenter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
        		R.array.categories, android.R.layout.simple_list_item_1);

        setListAdapter(adapter);
        
        myApp = (MyApplication) getApplication();
        
        String FILENAME = "file";
        FileInputStream fis = null;
        String singer;
        try {
			fis = openFileInput(FILENAME);
			int theByte = fis.read();
			while(theByte != -1)
			{
				singer = "";
				while((theByte != 10) && (theByte != -1))
				{
					singer += (char) theByte;
					theByte = fis.read();
				}
				
				myApp.addNewSinger(singer);
				theByte = fis.read();
				
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
        
        categories = this.getResources().getStringArray(R.array.categories);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	// TODO Auto-generated method stub
    	super.onListItemClick(l, v, position, id);
    	
        Intent intent;
        intent = new Intent(MainActivity.this,Categoria.class);
        if ( ((position >= 1) && (position <= 3)) || (position == 7) )
        {
        	intent.putExtra("categoryType", R.string.ZERO_ONE);
        }
        else if ((position == 8) || (position == 9))
        {
        	intent.putExtra("categoryType", R.string.TWO_TWO);
        }
        
        else
        {
        	
        	intent.putExtra("categoryType", R.string.ONE_ONE);
        }
        
        
        
        
        intent.putExtra("categoryIndex", position);
        intent.putExtra("categoryName", l.getItemAtPosition(position).toString());
		startActivity(intent);
    }
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	
    	switch (item.getItemId())
    	{
    		case R.id.menu_share:
    			String msg = "LISTA DE CANTOS:\n";
    	    	
    	    	for (int i=0; i<11; i++)
    	    	{
    	    		msg += categories[i] + ":\n";
    	    		
    	    		if ((i==0)||((i>4) && (i!=7)))
    	    		{
    	    			msg += "\tCanto: " + myApp.category[i].songName + "\n";
    	    		}
    	    		msg += "\tSalmista: " + myApp.category[i].singerName + "\n";
    	    		
    	    		
    	    	}
    	    	try {
    				
    				
    	    		 Intent sendIntent = new Intent(Intent.ACTION_SEND);
	   			     sendIntent.putExtra(Intent.EXTRA_TEXT, msg);
	   			     sendIntent.setType("text/plain");
	   			     sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Lista de Cantos para " +
	   			     		"la eucaristía");
	   			     startActivity(Intent.createChooser(sendIntent, "Compartir"));

    			} catch (Exception e) {
    				Toast.makeText(getApplicationContext(),
    					"SMS faild, please try again later!",
    					Toast.LENGTH_LONG).show();
    				e.printStackTrace();
    			}
    	    	return true;
    			
    		case R.id.menu_about:
    			
    			 
    			AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(item.getTitle());
                alert.setMessage("Descripción: Aplicación para preparar los cantos " +
                				 "de la eucarsitía\n\n" +
                				 "Versión: 1.0.1 \n" +
                				 "Fecha: Enero, 2013\n" +
                				 "Desarrollador: Sergio S.M.");

                alert.setNeutralButton("Seguir eucaristiando", new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				});
                
        		alert.show();
    			return true;
    			
    		case R.id.menu_goToSingers:
				
				Intent intent = new Intent(MainActivity.this, Cantor.class);
    			startActivity(intent);
    			return true;
    			
    			
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    	
    	
    	
    	
    }

    
    @Override
    protected void onDestroy() {
    	
    	String string = "";
    	String FILENAME = "file";
		
		for (int i=0; i<myApp.singerCount; i++)
		{
			string += myApp.availableSingers.get(i) + "\n";
		}
		
		FileOutputStream fos = null;
		
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			fos.write(string.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	myApp.cleanup();
    	super.onDestroy();
    }
}
