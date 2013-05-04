package de.rebreok.taxdroid;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ButtonAdapter extends BaseAdapter {
    private GameActivity parent;
    private ArrayList<Button> buttons;
    
    public ButtonAdapter(GameActivity ga) {
        parent = ga;
        buttons = new ArrayList<Button>();
        for (int i = 0; i < getCount(); i++) {
            Button button = new Button(parent);
            buttons.add(button);
            button.setText(String.valueOf(i + 1));
            button.setPadding(20, 20, 20, 20);
            button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Button b = (Button) view;
                        parent.selectButton(Integer.parseInt(b.getText().toString()));
                    }
                });
        }
    }
    
    public int getCount() {
        return parent.getLevel();
    }
    
    public Object getItem(int position) {
        return buttons.get(position);
    }
    
    public long getItemId(int position) {
        return 0;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        return buttons.get(position);
    }
}
