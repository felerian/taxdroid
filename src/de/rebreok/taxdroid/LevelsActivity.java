package de.rebreok.taxdroid;

import android.app.Activity;
import android.app.ListActivity;
import android.app.AlertDialog;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.widget.SimpleAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class LevelsActivity extends ListActivity
{
    private final static String ITEM_TITLE = "item_title";
    private final static String ITEM_DESCRIPTION = "item_description";
    public final static String EXTRA_LEVEL = "level";
    private final static String PREF_LEVEL_SCORE_FMT = "level_%1$d_score";
    private final static int MIN_LEVEL = 1;
    private final static int MAX_LEVEL = 25;
    public final static int RETURN_GAME_RESULT_REQUEST = 23;
    public final static String EXTRA_RETURN_SCORE = "return_score";
    private final static int DEF_INT = 232323;
    
    private ArrayList<Integer> levels;
    private ArrayList<Integer> scores;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void onResume()
    {
        super.onResume();
        
        SharedPreferences preferences = getPreferences(0);
        
        List<HashMap<String, String> > listData = new ArrayList<HashMap<String, String> >();
        
        for (int level = MIN_LEVEL; level <= MAX_LEVEL; level++)
        {
            String level_score_key = String.format(PREF_LEVEL_SCORE_FMT, level);
            
            HashMap<String, String> map = new HashMap<String, String>();
            map.put(ITEM_TITLE, getResources().getText(R.string.text_level) + " " + String.valueOf(level));
            if (preferences.contains(level_score_key))
            {
                map.put(ITEM_DESCRIPTION, getResources().getText(R.string.text_highscore) + " " + String.valueOf(preferences.getInt(level_score_key, DEF_INT)));
            } else {
                map.put(ITEM_DESCRIPTION, "" + getResources().getText(R.string.text_unplayed));
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
        intent.putExtra(EXTRA_LEVEL, position + MIN_LEVEL);
        startActivityForResult(intent, RETURN_GAME_RESULT_REQUEST);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SharedPreferences preferences = getPreferences(0);
        
        if (requestCode == RETURN_GAME_RESULT_REQUEST && resultCode == Activity.RESULT_OK)
        {
            int level = data.getIntExtra(EXTRA_LEVEL, -1);
            int new_score = data.getIntExtra(EXTRA_RETURN_SCORE, -999999);
            String level_score_key = String.format(PREF_LEVEL_SCORE_FMT, level);
            int old_score = preferences.getInt(level_score_key, DEF_INT);
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title_level_completed);
            String message;
            if (preferences.contains(level_score_key))
            {
                if (new_score > old_score)
                {
                    message = getResources().getString(R.string.dialog_fmt_score_improved, new_score, old_score);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(level_score_key, new_score);
                    editor.commit();
                } else {
                    message = getResources().getString(R.string.dialog_fmt_score_not_improved, new_score, old_score);
                }
            } else {
                message = getResources().getString(R.string.dialog_fmt_score_new, new_score);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(level_score_key, new_score);
                editor.commit();
            }
            builder.setMessage(message);
            builder.setPositiveButton(R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.levels, menu);
        return true;
    }
    
    public void resetScore(MenuItem menuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_reset_score);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setMessage(R.string.dialog_message_reset_score);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    SharedPreferences preferences = getPreferences(0);
                    for (int level = MIN_LEVEL; level <= MAX_LEVEL; level++) {
                        String level_score_key = String.format(PREF_LEVEL_SCORE_FMT, level);
                        if (preferences.contains(level_score_key)) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove(level_score_key);
                            editor.commit();
                        }
                    }
                    onResume();
                }
            });
        builder.setNegativeButton(R.string.cancel, null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
