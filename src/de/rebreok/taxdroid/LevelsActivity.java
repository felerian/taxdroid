package de.rebreok.taxdroid;

import android.app.Activity;
import android.app.ListActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class LevelsActivity extends ListActivity
{
    private final static String ITEM_TITLE = "item_title";
    private final static String ITEM_DESCRIPTION = "item_description";
    public final static String EXTRA_LEVEL = "level";
    private final static String LEVELS = "levels";
    private final static String SCORES = "scores";
    private final static int MIN_LEVEL = 5;
    private final static int MAX_LEVEL = 15;
    public final static int RETURN_GAME_RESULT_REQUEST = 23;
    public final static String EXTRA_RETURN_SCORE = "return_score";
    
    private ArrayList<Integer> levels;
    private ArrayList<Integer> scores;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        if (savedInstanceState != null)
        {
            levels = savedInstanceState.getIntegerArrayList(LEVELS);
            scores = savedInstanceState.getIntegerArrayList(SCORES);
        } else {
            levels = new ArrayList<Integer>(MAX_LEVEL - MIN_LEVEL + 1);
            scores = new ArrayList<Integer>(MAX_LEVEL - MIN_LEVEL + 1);
            for (int i = MIN_LEVEL; i <= MAX_LEVEL; i++)
            {
                levels.add(i);
                scores.add(null);
            }
        }
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        List<HashMap<String, String> > listData = new ArrayList<HashMap<String, String> >();
        
        for (int i = 0; i < scores.size(); i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ITEM_TITLE, getResources().getText(R.string.text_level) + " " + String.valueOf(levels.get(i)));
            if (scores.get(i) == null)
            {
                map.put(ITEM_DESCRIPTION, "" + getResources().getText(R.string.text_unplayed));
            } else {
                map.put(ITEM_DESCRIPTION, getResources().getText(R.string.text_highscore) + " " + String.valueOf(scores.get(i)));
            }
            listData.add(map);
        }
        
        SimpleAdapter adapter = new SimpleAdapter(this, listData, android.R.layout.two_line_list_item, new String[] {ITEM_TITLE, ITEM_DESCRIPTION}, new int[] {android.R.id.text1, android.R.id.text2});
        
        setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id)
    {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(EXTRA_LEVEL, levels.get(position));
        startActivityForResult(intent, RETURN_GAME_RESULT_REQUEST);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == RETURN_GAME_RESULT_REQUEST && resultCode == Activity.RESULT_OK)
        {
            int level = data.getIntExtra(EXTRA_LEVEL, -1);
            int score = data.getIntExtra(EXTRA_RETURN_SCORE, -999999);
            int level_index = levels.indexOf(level);
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title_level_completed);
            //~ builder.setTitle("Level " + String.valueOf(level) + ": score = " + String.valueOf(score));
            if (scores.get(level_index) == null || score > scores.get(level_index))
            {
                builder.setMessage(R.string.dialog_text_score_improved);
                scores.set(level_index, score);
            } else {
                builder.setMessage(R.string.dialog_text_score_not_improved);
            }
            builder.setPositiveButton(R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(LEVELS, levels);
        outState.putIntegerArrayList(SCORES, scores);
    }
}
