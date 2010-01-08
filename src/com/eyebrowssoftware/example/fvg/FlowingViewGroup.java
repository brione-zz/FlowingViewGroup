package com.eyebrowssoftware.example.fvg;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class FlowingViewGroup extends ListActivity {

    private static final String[] mTitles1 = 
    {
        "Henry IV (1)",   
        "Henry V",
        "Henry VIII",       
        "Richard II",
        "Richard III",
        "Merchant of Venice",  
        "Othello",
        "King Lear",
        "A Path with Heart",
        "Transmetropolitan",
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[] mTitles2 = 
    {
        "Henry V",
        "Henry VIII",       
        "Richard II",
        "Richard III",
        "Merchant of Venice",  
        "Othello",
        "King Lear",
        "A Path with Heart",
        "Transmetropolitan",
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[] mTitles3 = 
    {
        "Henry VIII",       
        "Richard II",
        "Richard III",
        "Merchant of Venice",  
        "Othello",
        "King Lear",
        "A Path with Heart",
        "Transmetropolitan",
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[] mTitles4 = 
    {
        "Richard III",
        "Merchant of Venice",  
        "Othello",
        "King Lear",
        "A Path with Heart",
        "Transmetropolitan",
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[] mTitles5 = 
    {
        "Othello",
        "King Lear",
        "A Path with Heart",
        "Transmetropolitan",
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[] mTitles6 = 
    {
        "A Path with Heart",
        "Transmetropolitan",
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[] mTitles7 = 
    {
        "Fables",
        "Jack of Fables",
        "Strangers in Paradise",
    };
    
    private static final String[][] mLibraries = {
    	mTitles1, mTitles2, mTitles3, mTitles4, mTitles5, mTitles6, mTitles7,
    };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TagsViewGroup tvg = (TagsViewGroup) findViewById(R.id.tags);
        ArrayAdapter<String> saa = new ArrayAdapter<String>(this, R.layout.text_view, mTitles1);
        tvg.setAdapter(saa);
        ArrayAdapter<String[]> saaa = new ArrayAdapter<String[]>(this, R.layout.tags_view_group, mLibraries) {
        	
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		TagsViewGroup tgv;
        		if(convertView == null) {
        			tgv = (TagsViewGroup) FlowingViewGroup.this.getLayoutInflater().inflate(R.layout.tags_view_group, null);
        		} else
        			tgv = (TagsViewGroup) convertView;
        		ArrayAdapter<String> aa = new ArrayAdapter<String>(FlowingViewGroup.this, 
        				R.layout.text_view, this.getItem(position));
        		tgv.setAdapter(aa);
        		return tgv;
        	}
        };
        this.setListAdapter(saaa);
    }
}