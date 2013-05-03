package de.rebreok.taxdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;


public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void displayLevels(View view)
    {
        Intent intent = new Intent(this, LevelsActivity.class);
        startActivity(intent);
    }
    
    public void displayHelp(View view)
    {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }
}
