package com.google.ar.sceneform.samples.ModelAction;
import com.google.ar.sceneform.samples.augmentedimage.R;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ModelOne {
    Button button;
    ImageView imageView;
    Context context;
    View view;
    public ModelOne(Context context, View view){
        this.context=context;
        this.view=view;
        button=this.view.findViewById(R.id.CatButton);
        imageView=this.view.findViewById(R.id.CatImage);
        button.setOnClickListener(buttonlistener);
    }
    View.OnClickListener buttonlistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String ImageName = String.valueOf(view.getTag());
            Log.d("getImage",ImageName);
            if(ImageName.equals("cat.jpg")){
                imageView.setImageResource(R.drawable.cat2);
            }
            else if(ImageName.equals("cat2.jpg")){
                imageView.setImageResource(R.drawable.cat);
            }
        }
    };
}
