package com.eucaristiando;



import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;



public class Categoria extends Activity {

	
	
	
	public List<CharSequence> singers;
	public String[] songs;
	private int index;
	
	private MyApplication myApp;
	
	public EditText singerInput;
	public boolean displayingDialog;
    public AlertDialog.Builder alert;
    
    protected ArrayAdapter<CharSequence> adapterSong;
	protected ArrayAdapter<CharSequence> adapterSinger;
    public  Spinner spinnerSong;
    public 	Spinner spinnerSinger;
    
    
    public  Spinner spinnerSong2;
    public 	Spinner spinnerSinger2;
    
    
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
          	  adapterSinger.add(value);
		      adapterSinger.notifyDataSetChanged();
		      
		      myApp.addNewSinger(value);
		      displayingDialog = false;
          
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
    
    public void setSpinnerSelection(){
    	
    	//int numSongs = adapterSong.getCount();
    	int numSongs = songs.length;
    	String currentSong1 = myApp.category[index].songName;
    	String currentSinger1 = myApp.category[index].singerName;
    	String currentSong2 = myApp.category[index].songName2;
    	String currentSinger2 = myApp.category[index].singerName2;
    	
    	
    	/*See if the current songs stored in myApp.category for each spinner
    	matches any valid song
    	*/
    	//spinnerSong1
    	boolean validSong = false;
    	int j=0;  	
    	while((j < numSongs) && (validSong==false))
    	{
    		if (songs[j].equals(currentSong1))
    		{
    			spinnerSong.setSelection(j);
    			validSong = true;
    		}
    		j++;
    	}
    	
    	//spinnerSong2
    	validSong = false;
    	j=0; 	
    	while((j < numSongs) && (validSong==false))
    	{
    		if (songs[j].equals(currentSong2))
    		{
    			spinnerSong2.setSelection(j);
    			validSong = true;
    		}
    		j++;
    	}
    	
    	
    	adapterSinger.add(currentSinger1);
    	adapterSinger.notifyDataSetChanged();
    	spinnerSinger.setSelection(0);
    	
    	if (!currentSinger1.equals(currentSinger2))
    	{
    		adapterSinger.add(currentSinger2);
    		adapterSinger.notifyDataSetChanged();
    		spinnerSinger2.setSelection(1);
    	}
    	else spinnerSinger2.setSelection(0);
    	
    	
    	/*Copy every item from the availableSinger array to the adapterSinger array
    	At the same time, check that the singer that is currently in the category
    	matches any of the elements in the availableSinger array, in that case set
    	the selection in the spinnerSinger to that element
    	Note that if myApp.singerCount == 0 the following loop won't be executed
    	*/
    	for (int i=0; i<myApp.singerCount; i++)
    	{
    		this.adapterSinger.add(myApp.availableSingers.get(i));
        	this.adapterSinger.notifyDataSetChanged();
        	
        	if (myApp.availableSingers.get(i).equals(currentSinger1))
        	{
        		spinnerSinger.setSelection(i);
        		spinnerSinger2.setSelection(i);
        	}
    	}
    	
    	
    	
    	
    	
    }
    
    
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria);
        
        displayingDialog = false;
        Bundle bundle = getIntent().getExtras();
        index = bundle.getInt("categoryIndex");
        setTitle(bundle.getString("categoryName"));
        
        myApp = (MyApplication) getApplication();
        
        
        
        singers = new ArrayList<CharSequence>();
        songs = getResources().getStringArray(R.array.songs);
        
        
        spinnerSong = (Spinner) findViewById(R.id.spinner_canto);
    	spinnerSinger = (Spinner) findViewById(R.id.spinner_cantor);
    	
    	spinnerSong2 = (Spinner) findViewById(R.id.spinner_canto2);
    	spinnerSinger2 = (Spinner) findViewById(R.id.spinner_cantor2);
    	
    	
    	
    	if(bundle.getInt("categoryType") == R.string.ZERO_ONE )
    	{
    		LinearLayout lay=(LinearLayout)this.findViewById(R.id.sublayoutCanto);
    		lay.setVisibility(View.GONE);
    	}
    	
    	if(bundle.getInt("categoryType") == R.string.TWO_TWO )
    	{
    		LinearLayout laySong=(LinearLayout)this.findViewById(R.id.sublayoutCanto2);
    		laySong.setVisibility(View.VISIBLE);
    		
    		LinearLayout laySinger=(LinearLayout)this.findViewById(R.id.sublayoutCantor2);
    		laySinger.setVisibility(View.VISIBLE);
    	}
    	
    	this.adapterSong = new ArrayAdapter<CharSequence>(this, 
    			android.R.layout.simple_spinner_dropdown_item, songs){
    	    		
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
    	

    	
    	this.adapterSinger = new ArrayAdapter<CharSequence>(this, 
    			android.R.layout.simple_spinner_dropdown_item, singers){
    		
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
    	
    	
    	
    	
    	
    	
    	spinnerSong.setAdapter(this.adapterSong);
    	spinnerSinger.setAdapter(this.adapterSinger);
    	
    	spinnerSong2.setAdapter(this.adapterSong);
    	spinnerSinger2.setAdapter(this.adapterSinger);
    	
    	
    	setSpinnerSelection();
        
    	
        
        spinnerSong.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String item = (String) parent.getItemAtPosition(pos);
		        myApp.category[index].songName = item;
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
		});
        
        spinnerSinger.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				
				String item = (String) parent.getItemAtPosition(pos);
		        myApp.category[index].singerName = item;
			
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
		        
			}
        	
		});
        
        
        spinnerSong2.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String item = (String) parent.getItemAtPosition(pos);
		        myApp.category[index].songName2 = item;
				
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
		});
        
        spinnerSinger2.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				
				String item = (String) parent.getItemAtPosition(pos);
		        myApp.category[index].singerName2 = item;
			
			}

			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
		        
			}
        	
		});
        
    	
    	
    	
    	Button okButton = (Button) findViewById(R.id.boton_canto_ok);
    	okButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				finish();
			}
		});
    	
    	
    	if ( (savedInstanceState != null) && savedInstanceState.getBoolean("dialogDisplayed"))
    	   {
 		   addSinger(savedInstanceState.getString("singerInputVal"));
    	   }
    	

    }//OnCreate
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	int newSingersCount = 0;
    	
    	if (resultCode == RESULT_OK)
    	{
    		newSingersCount= data.getExtras().getInt("newSingersCount");
        	int singerCount = myApp.singerCount;
	    	for (int i=newSingersCount; i>0; i--)
	    	{
	    		adapterSinger.add(myApp.availableSingers.get(singerCount - i));
	    		adapterSinger.notifyDataSetChanged();
	    		
	    	}
    	}
    	
    	super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_categoria, menu);
        return true;
        
        
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	
    	switch (item.getItemId()){
    		case R.id.menu_goToSingers:
    			Intent intent = new Intent(Categoria.this, Cantor.class);
    			startActivityForResult(intent, 1);
    			return true;
    		case R.id.menu_addSinger:
    			addSinger(null);
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
    
   
    
   
    
}

