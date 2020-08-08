package com.example.AmbersAR;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ar.sceneform.ux.ArFragment;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class VideoActivity extends AppCompatActivity
//        implements GLSurfaceView.Renderer, ImageReader.OnImageAvailableListener,
//        SurfaceTexture.OnFrameAvailableListener{
{

    private ArFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragment = (ArFragment)
                getSupportFragmentManager().findFragmentById(R.id.sceneform_fragment);

    }


//    @Override
//    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//
//    }
//
//    @Override
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//
//    }
//
//    @Override
//    public void onDrawFrame(GL10 gl) {
//
//    }
//
//    @Override
//    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
//
//    }
//
//    @Override
//    public void onImageAvailable(ImageReader reader) {
//
//    }

}