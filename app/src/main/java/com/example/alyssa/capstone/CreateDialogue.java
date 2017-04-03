package com.example.alyssa.capstone;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * Created by Alyssa on 4/2/2017.
 */

public class CreateDialogue extends Dialog {
    public CreateDialogue(Context context) {
       super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_create);
    }

    public void setOnSaveListener(final DialogueDismissListener listener) {
        findViewById(R.id.dialog_create_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name = (EditText) findViewById(R.id.editTextName);
                EditText length = (EditText) findViewById(R.id.editTextLegnth);
                listener.onDismiss(name.getText().toString(), Integer.parseInt(length.getText().toString()));
            }
        });
    }



    public interface DialogueDismissListener {
        void onDismiss(String s, int i);
    }
}
