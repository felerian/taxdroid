package de.rebreok.taxdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.graphics.Color;

import java.util.ArrayList;

public class GameActivity extends Activity
{
    private final static String SELECTION = "selection";
    private final static String PLAYER_MONEY = "player_money";
    private final static String TAXDROID_MONEY = "taxdroid_money";
    
    private int level;
    private int selection;
    private int player_money;
    private int taxdroid_money;
    private ArrayList<Button> buttons;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        findViewById(R.id.button_take_money).setEnabled(false);
        
        buttons = new ArrayList<Button>();
        
        level = getIntent().getIntExtra(LevelsActivity.EXTRA_LEVEL, -1);
        
        setTitle("TaxDroid - Level " + String.valueOf(level));
        
        GridLayout hbox = (GridLayout) findViewById(R.id.hbox);
        for (int i = 0; i < level; i++) {
            Button button = new Button(this);
            buttons.add(button);
            button.setText(String.valueOf(i + 1));
            button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Button b = (Button) view;
                        selection = Integer.parseInt(b.getText().toString());
                        updateSelection();
                    } 
                });
            hbox.addView(button);
        }
        
        if (savedInstanceState != null) {
            selection = savedInstanceState.getInt(SELECTION);
            player_money = savedInstanceState.getInt(PLAYER_MONEY);
            taxdroid_money = savedInstanceState.getInt(TAXDROID_MONEY);
        } else {
            selection = 0;
            player_money = 0;
            taxdroid_money = 0;
        }
        updateSelection();
    }
    
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(SELECTION, selection);
        savedInstanceState.putInt(PLAYER_MONEY, player_money);
        savedInstanceState.putInt(TAXDROID_MONEY, taxdroid_money);
        
        super.onSaveInstanceState(savedInstanceState);
    }
    
    /**
     * Check for end of game and update the Buttons' colors
     */
    private void updateSelection() {
        boolean game_over = false;
        /**
         * If no valid choice is possible give the rest to the TaxDroid
         * and set the game_over flag.
         */
        if (isGameOver()) {
            game_over = true;
            for (int i = 1; i <= level; i++) {
                if (buttons.get(i - 1).isEnabled()) {
                    taxdroid_money += i;
                }
            }
        }
        
        /** Update the UI */
        for (int i = 1; i < selection; i++) {
            if (selection % i == 0 && buttons.get(i - 1).isEnabled()) {
                buttons.get(i - 1).setTextAppearance(this, R.style.text_bad);
            } else {
                buttons.get(i - 1).setTextAppearance(this, R.style.text_default);
            }
        }
        if (selection > 0) {
            buttons.get(selection - 1).setTextAppearance(this, R.style.text_good);
        }
        for (int i = selection + 1; i <= level; i++) {
            buttons.get(i - 1).setTextAppearance(this, R.style.text_default);
        }
        if (isValidChoice(selection)) {
            findViewById(R.id.button_take_money).setEnabled(true);
        } else {
            findViewById(R.id.button_take_money).setEnabled(false);
        }
        TextView player_counter = (TextView) findViewById(R.id.text_player_score);
        player_counter.setText(String.valueOf(player_money));
        TextView taxdroid_counter = (TextView) findViewById(R.id.text_taxdroid_score);
        taxdroid_counter.setText(String.valueOf(taxdroid_money));
        
        /** If game_over, show a dialog and finish. */
        if (game_over) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.dialog_title_game_over);
            builder.setMessage(String.format(getResources().getString(R.string.dialog_message_game_over), player_money, taxdroid_money));
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.putExtra(LevelsActivity.EXTRA_LEVEL, level);
                        intent.putExtra(LevelsActivity.EXTRA_RETURN_SCORE, player_money - taxdroid_money);
                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    
    public void takeMoney(View view) {
        for (int i = 1; i < selection; i++) {
            if (selection % i == 0) {
                buttons.get(i - 1).setEnabled(false);
                taxdroid_money += i;
            }
        }
        buttons.get(selection - 1).setEnabled(false);
        player_money += selection;
        selection = 0;
        updateSelection();
    }
    
    private boolean isValidChoice(int selection) {
        for (int i = 1; i < selection; i++) {
            if (selection % i == 0 && buttons.get(i - 1).isEnabled()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isGameOver() {
        for (int i = 1; i <= level; i++) {
            if (isValidChoice(i)) {
                return false;
            }
        }
        return true;
    }
}
