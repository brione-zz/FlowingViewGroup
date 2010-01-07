package com.eyebrowssoftware.example.fvg;

import com.example.fvg.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class FlowingViewGroup extends Activity {

    private static final String[] mTitles = 
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
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TagsViewGroup tvg = (TagsViewGroup) findViewById(R.id.tags);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, R.layout.text_view, android.R.id.text1, mTitles);
        tvg.setAdapter(aa);
    }
}