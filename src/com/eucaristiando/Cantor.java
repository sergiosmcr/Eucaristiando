package com.eucaristiando;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView.AdapterContextMenuInfo;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class Cantor extends Activity {

	protected ArrayAdapter<CharSequence> adapterSinger;
	public List<CharSequence> singers;
	private MyApplication myApp;
	
	
	public EditText singerInput;
	public boolean displayingDialog;
    public AlertDialog.Builder alert;
	
    public int newSingers = 0;
    
    public void addSinger(String text){
    	alert = new AlertDialog.Builder(this);
        
        alert.setTitle("Nuevo Salmista");
        alert.setMessage("Ingrese el nombre del salmista que desea agregar");

        // Set an EditText view to get user singerInput 
        singerInput = new EditText(this);
        if (text != null) singerInput.setText(text);
        alert.setView(singerInput);
        
        
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
          String value =singerInput.getText().toString();
          	  if (value.length()>0) adapterSinger.add(value);
		      adapterSinger.notifyDataSetChanged();
		      
		      myApp.addNewSinger(value);
		      displayingDialog = false;
		      newSingers++;
          
          }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
        	  displayingDialog = false;
          }
        });
        displayingDialog = true;
		alert.show();
    	
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cantor);
        
        
        
        myApp = (MyApplication) getApplication();
        
        singers = new ArrayList<CharSequence>();
        
        adapterSinger = new ArrayAdapter<CharSequence>(this, 
    			android.R.layout.simple_list_item_1, singers){
        	
        	@Override
    	    public View getDropDownView(int position, View convertView, ViewGroup parent)
    	    {
    	        View v = null;
    	 
    	        // If this is the initial dummy entry, make it hidden
    	        if (position == 0) {
    	            TextView tv = new TextView(getContext());
    	            tv.setHeight(0);
    	            tv.setVisibility(View.GONE);
    	            v = tv;
    	        }
    	        else {
    	            // Pass convertView as null to prevent reuse of special case views
    	            v = super.getDropDownView(position, null, parent);
    	        }
    	 
    	        
    	        return v;
    	    }
        };
        
       ListView listView = (ListView) findViewById(R.id.mySingerlist);
       
       listView.setAdapter(adapterSinger);
       
       registerForContextMenu(listView);
       
       
       
	   /*Copy every item from the availableSinger array to the adapterSinger array
	   Note that if myApp.singerCount == 0 the following loop won't be executed
	   */
	   for (int i=0; i<myApp.singerCount; i++)
	   {
		   this.adapterSinger.add(myApp.availableSingers.get(i));
		   this.adapterSinger.notifyDataSetChanged();
	   }
	   
	   
	   if ( (savedInstanceState != null) && savedInstanceState.getBoolean("dialogDisplayed"))
   	   {
		   addSinger(savedInstanceState.getString("singerInputVal"));
   	   }
   	
    }

   
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        	Intent returnIntent;
        	returnIntent = new Intent();
			returnIntent.putExtra("newSingersCount", newSingers);
			setResult(RESULT_OK,returnIntent);     
			finish();
        }
        
		
        return super.onKeyDown(keyCode, event);
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_cantor, menu);
        return true;
    }

    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()) 
    	{
    		case R.id.menu_addSinger:
    		
    			addSinger(null);
	    		return true;
	    		
    		case R.id.menu_removeSinger:
    			return true;
    		
    		
	    	default:
	    		return super.onOptionsItemSelected(item);
    	}
    }
    
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	
    	if ((singerInput != null) && displayingDialog)
    	{
    		outState.putString("singerInputVal", singerInput.getText().toString());
    		outState.putBoolean("dialogDisplayed", true);
    	}
    	else outState.putBoolean("dialogDisplayed", false);
    	super.onSaveInstanceState(outState);
    	
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	// TODO Auto-generated method stub
    	super.onCreateContextMenu(menu, v, menuInfo);
    	
    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	// TODO Auto-generated method stub
    	
    	
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	
    	switch (item.getItemId())
    	{
    		case R.id.menu_edit:
    			
    			return true;
    			
    		case R.id.menu_remove:
    			
    			adapterSinger.remove(adapterSinger.getItem(info.position));
    			adapterSinger.notifyDataSetChanged();
    			myApp.removeSinger(info.toString());
    			
    			return true;
    			
    		default:
    			return super.onContextItemSelected(item);
    	}
    	
    }
}
