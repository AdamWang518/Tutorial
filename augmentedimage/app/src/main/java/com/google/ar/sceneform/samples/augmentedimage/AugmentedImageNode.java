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

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedImage;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.FixedWidthViewSizer;
import com.google.ar.sceneform.rendering.ViewRenderable;

import com.google.ar.sceneform.samples.ModelAction.ModelOne;
import com.google.ar.sceneform.samples.ModelAction.ModelTwo;


import java.util.concurrent.CompletableFuture;

/**
 * Node for rendering an augmented image. The image is framed by placing the virtual picture frame
 * at the corners of the augmented image trackable.
 */
@SuppressWarnings({"AndroidApiChecker"})
public class AugmentedImageNode extends AnchorNode {
    private static CompletableFuture<ViewRenderable> NumberOneRenderable;
    private static CompletableFuture<ViewRenderable> NumberTwoRenderable;
    private Anchor anchor = null;
    private String Name;
    private  Context context;
    public AugmentedImageNode(Context context) {
        this.context=context;
        NumberOneRenderable=ViewRenderable.builder().setSizer(new FixedWidthViewSizer(0.5f)).setView(context,R.layout.number1_layout).build();
        NumberTwoRenderable=ViewRenderable.builder().setSizer(new FixedWidthViewSizer(0.5f)).setView(context,R.layout.number2_layout).build();
    }

    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    public void setImage(AugmentedImage image,String Name) {
        this.Name=Name;
        Log.d("imageName",Name);
        // If any of the models are not loaded, then recurse when all are loaded.
        NumberOneRenderable.thenAccept(
                (Renderable) -> {
                    View view = Renderable.getView();
                    ModelOne modelDefault = new ModelOne(context,view);
                }
        );
        NumberTwoRenderable.thenAccept(
                (Renderable) -> {
                    View view = Renderable.getView();
                    ModelTwo modelTwo=new ModelTwo(context,view);
                }
        );
        this.anchor = image.createAnchor(image.getCenterPose());
        // Set the anchor based on the center of the image.
        setAnchor(this.anchor);
        Log.d("ImageName",Name) ;
        if(Name.equals("earth.jpg"))
        {
            setVerticalRenderable(NumberOneRenderable,0f,0f, 0f);
        }
        else if(Name.equals("game.png"))
        {
            setVerticalRenderable(NumberTwoRenderable,0f,0f, 0f);
        }

    }



    private void setViewRenderable(CompletableFuture<ViewRenderable> renderable,float x,float y,float z){
        Node cornerNode;
        Vector3 localPosition = new Vector3();
        localPosition.set(x,y,z);
        cornerNode = new Node();
        cornerNode.setParent(this);
        cornerNode.setLocalPosition(localPosition);
        cornerNode.setRenderable(renderable.getNow(null));
    }
    private void setVerticalRenderable(CompletableFuture<ViewRenderable> renderable,float x,float y,float z){
        Node cornerNode;
        Vector3 localPosition = new Vector3();
        localPosition.set(x,y,z);
        cornerNode = new Node();
        cornerNode.setParent(this);
        cornerNode.setLookDirection(Vector3.forward());
        cornerNode.setLocalPosition(localPosition);
        cornerNode.setRenderable(renderable.getNow(null));
    }
    private void setHorizontalRenderable(CompletableFuture<ViewRenderable> renderable,float x,float y,float z){
        Node cornerNode;
        Vector3 localPosition = new Vector3();
        localPosition.set(x,y,z);
        cornerNode = new Node();
        cornerNode.setParent(this);
        cornerNode.setLocalPosition(localPosition);
        //cornerNode.setLookDirection(Vector3.down(),Vector3.forward());
        cornerNode.setLocalRotation(Quaternion.axisAngle(new Vector3(1, 0, 0), -90f));
        cornerNode.setRenderable(renderable.getNow(null));
    }



}
