package de.rebreok.taxdroid;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class GameActivity extends Activity
{
    private int level;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        level = getIntent().getIntExtra(LevelsActivity.EXTRA_LEVEL, -1);
        
        Intent intent = new Intent();
        intent.putExtra(LevelsActivity.EXTRA_LEVEL, level);
        intent.putExtra(LevelsActivity.EXTRA_RETURN_SCORE, 42);
        setResult(Activity.RESULT_OK, intent);
    }
    
}
