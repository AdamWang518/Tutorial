package com.google.ar.sceneform.samples.ModelAction;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.ar.sceneform.samples.augmentedimage.R;

public class ModelTwo {
    Button button;
    ImageView imageView;
    Context context;
    View view;
    public ModelTwo(Context context, View view){
        this.context=context;
        this.view=view;
        button=this.view.findViewById(R.id.DogButton);
        imageView=this.view.findViewById(R.id.DogImage);
        button.setOnClickListener(buttonlistener);
    }
    View.OnClickListener buttonlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String ImageName = String.valueOf(view.getTag());
            if(ImageName.equals("dog.jpg")){
                imageView.setImageResource(R.drawable.dog2);
            }
            else if(ImageName.equals("dog2.jpg")){
                imageView.setImageResource(R.drawable.dog);
            }
        }
    };
}
