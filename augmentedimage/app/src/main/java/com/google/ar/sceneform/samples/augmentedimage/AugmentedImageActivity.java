/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.ar.sceneform.samples.augmentedimage;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentOnAttachListener;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.Session;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.samples.common.helpers.SnackbarHelper;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseArFragment;

import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This application demonstrates using augmented images to place anchor nodes. app to include image
 * tracking functionality.
 *
 * <p>In this example, we assume all images are static or moving slowly with a large occupation of
 * the screen. If the target is actively moving, we recommend to check
 * ArAugmentedImage_getTrackingMethod() and render only when the tracking method equals to
 * AR_AUGMENTED_IMAGE_TRACKING_METHOD_FULL_TRACKING. See details in <a
 * href="https://developers.google.com/ar/develop/c/augmented-images/">Recognize and Augment
 * Images</a>.
 */
public  class AugmentedImageActivity extends AppCompatActivity {


  private ArFragment arFragment;
  private boolean isImageDetected=false;
  // Augmented image and its associated center pose anchor, keyed by the augmented image in
  // the database.
  private final HashMap<AugmentedImage, AugmentedImageNode> augmentedImageMap = new HashMap<>();
  Scene scene = null;
  AugmentedImageNode node = null;
  AugmentedImage image = null;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
    scene = arFragment.getArSceneView().getScene();
    node = new AugmentedImageNode(this);

    scene.addOnUpdateListener(this::onUpdateFrame);
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (augmentedImageMap.isEmpty()) {

      Log.d("leolog","OnResume");
    }
  }

  /**
   * Registered with the Sceneform Scene object, this method is called at the start of each frame.
   *
   * @param frameTime - time since last frame.
   */
  private void onUpdateFrame(FrameTime frameTime) {//反覆被呼叫
    Log.d("leolog","Detecting");
    if(isImageDetected) {
      return;
    }
    Frame frame = arFragment.getArSceneView().getArFrame();
    Log.d("leolog","OnUpdateFrame");
    // If there is no frame, just return.
    if (frame == null) {
      return;
    }

    Collection<AugmentedImage> updatedAugmentedImages =
            frame.getUpdatedTrackables(AugmentedImage.class);
    for (AugmentedImage augmentedImage : updatedAugmentedImages) {
      SnackbarHelper.getInstance().showMessage(this, augmentedImage.getTrackingState().toString());
      if(augmentedImage.getTrackingState().toString().equals("PAUSED")){
        Toast toast=Toast.makeText(this, "掃描中", Toast.LENGTH_LONG);
//        showMyToast(toast, 500);
//        //Toast.makeText(this, "掃描中", Toast.LENGTH_SHORT).show();
      }
      else if(augmentedImage.getTrackingState().toString().equals("TRACKING")){
        Toast.makeText(this, "掃描完成", Toast.LENGTH_SHORT).show();
//        Toast toast=Toast.makeText(this, "掃描完成", Toast.LENGTH_LONG);
//        showMyToast(toast, 500);
      }
      //Log.d("leolog",augmentedImage.getTrackingState().toString());
      //SnackbarHelper.getInstance().showMessage(this, augmentedImage.getTrackingState().toString());
      switch (augmentedImage.getTrackingState()) {
        case PAUSED:
          // When an image is in PAUSED state, but the camera is not PAUSED, it has been detected,
          // but not yet tracked.
          String text = "Detected Image: " + augmentedImage.getName();

          SnackbarHelper.getInstance().showMessage(this, text);
          //需要修復
          break;

        case TRACKING:
          // Have to switch to UI Thread to update View.

          //搞懂如何反覆投放
          if (!augmentedImageMap.containsKey(augmentedImage)) {
            //node = new AugmentedImageNode(this);
            this.image = augmentedImage;

            node.setImage(this.image,augmentedImage.getName());
            augmentedImageMap.put(this.image, node);
            arFragment.getArSceneView().getScene().addChild(node);
            isImageDetected = true;
          }
          break;

        case STOPPED:
          augmentedImageMap.remove(this.image);
          break;
      }
    }
  }
  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);


  }

  public void restart(View view) {
    Intent intent = getIntent();
    finish();
    startActivity(intent);
  }



  public void showMyToast(final Toast toast, final int cnt) {
    final Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        toast.show();
      }
    }, 0, 3000);
    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        toast.cancel();
        timer.cancel();
      }
    }, cnt );
  }


}
