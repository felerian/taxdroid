package de.rebreok.taxdroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.Intent;
import android.content.DialogInterface;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.View;
import android.graphics.Color;

import java.util.ArrayList;

public class GameActivity extends Activity
{
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
        
        LinearLayout hbox = (LinearLayout) findViewById(R.id.hbox);
        for (int i = 0; i < level; i++) {
            Button button = new Button(this);
            buttons.add(button);
            button.setText(String.valueOf(i + 1));
            button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Button b = (Button) view;
                        selection = Integer.parseInt(b.getText().toString());
                        selectButton();
                    } 
                });
            hbox.addView(button);
        }
    }
    
    private void selectButton() {
        boolean taxdroid_gets_its_share = false;
        for (int i = 1; i < selection; i++) {
            if (selection % i == 0 && buttons.get(i - 1).isEnabled()) {
                buttons.get(i - 1).setTextAppearance(this, R.style.text_bad);
                taxdroid_gets_its_share = true;
            } else {
                buttons.get(i - 1).setTextAppearance(this, R.style.text_default);
            }
        }
        buttons.get(selection - 1).setTextAppearance(this, R.style.text_good);
        for (int i = selection + 1; i <= level; i++) {
            buttons.get(i - 1).setTextAppearance(this, R.style.text_default);
        }
        if (taxdroid_gets_its_share) {
            findViewById(R.id.button_take_money).setEnabled(true);
        } else {
            findViewById(R.id.button_take_money).setEnabled(false);
        }
    }
    
    public void takeMoney(View view) {
        for (int i = 1; i < selection; i++) {
            buttons.get(i - 1).setTextAppearance(this, R.style.text_default);
            if (selection % i == 0) {
                buttons.get(i - 1).setEnabled(false);
                taxdroid_money += i;
            }
        }
        buttons.get(selection - 1).setEnabled(false);
        player_money += selection;
        for (int i = selection; i <= level; i++) {
            buttons.get(i - 1).setTextAppearance(this, R.style.text_default);
        }
        findViewById(R.id.button_take_money).setEnabled(false);
    }
    
    public void giveUp(View view) {
        for (int i = 1; i <= level; i++) {
            if (buttons.get(i - 1).isEnabled()) {
                taxdroid_money += i;
            }
        }
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
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
