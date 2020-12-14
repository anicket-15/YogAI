package com.anicket.yogai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseDetection;
import com.google.mlkit.vision.pose.PoseDetector;
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity {


    PreviewView cameraPreviewView;
    Preview preview;
    ImageAnalysis imageAnalysis;
    GraphicOverlay graphicOverlay;
    int cameraSide;
    Button switchCamera;

    public CameraActivity(){
        cameraSide = CameraSelector.LENS_FACING_FRONT; // Front Camera
    }

    @Override
    @ExperimentalGetImage
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);



        // Reference to XML Items
        cameraPreviewView = findViewById(R.id.cameraPreviewView);
        switchCamera = findViewById(R.id.switchCamera);
        startCamera();
        switchCamera.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(cameraSide==CameraSelector.LENS_FACING_BACK){
                            cameraSide = CameraSelector.LENS_FACING_FRONT;
                        }else{
                            cameraSide = CameraSelector.LENS_FACING_BACK;
                        }
                        startCamera();
                    }
                }
        );

    }

    @ExperimentalGetImage
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> processCameraProviderListenableFuture =ProcessCameraProvider.getInstance(this);
        processCameraProviderListenableFuture.addListener(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ProcessCameraProvider cameraProvider = processCameraProviderListenableFuture.get();
                            bindToLifeCycle(cameraProvider);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, ContextCompat.getMainExecutor(this));

    }

    @ExperimentalGetImage
    private void bindToLifeCycle(ProcessCameraProvider cameraProvider) {
        preview = new Preview.Builder().build();
        imageAnalysis = new ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(cameraSide).build();
        preview.setSurfaceProvider(cameraPreviewView.getSurfaceProvider());
        ExecutorService cameraAnalysisExecutor = Executors.newSingleThreadExecutor();
        graphicOverlay = findViewById(R.id.graphicOverlay);
        imageAnalysis.setAnalyzer(cameraAnalysisExecutor, new ImageAnalysis.Analyzer() {
            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {
                PoseDetectorOptions poseDetectorOptions = new PoseDetectorOptions.Builder().setDetectorMode(PoseDetectorOptions.STREAM_MODE).build();
                PoseDetector poseDetector = PoseDetection.getClient(poseDetectorOptions);
                Image mediaImage =imageProxy.getImage();
                if(mediaImage!=null){
                    int rotationDegrees =imageProxy.getImageInfo().getRotationDegrees();
                    try{
                        if (rotationDegrees == 0 || rotationDegrees == 180) {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getWidth(),
                                    imageProxy.getHeight(),
                                    cameraSide==CameraSelector.LENS_FACING_FRONT);
                        } else {
                            graphicOverlay.setImageSourceInfo(
                                    imageProxy.getHeight(),
                                    imageProxy.getWidth(),
                                    cameraSide==CameraSelector.LENS_FACING_FRONT);
                        }

                    }catch(Exception e){
                        Log.d("graphicOverlayException",e.toString());
                    }

                    InputImage inputImage = InputImage.fromMediaImage(mediaImage,imageProxy.getImageInfo().getRotationDegrees());
                    Task<Pose>result =poseDetector.process(inputImage)
                            .addOnSuccessListener(
                                    new OnSuccessListener<Pose>() {
                                        @Override
                                        public void onSuccess(Pose pose) {
                                            try {
                                                graphicOverlay.post(
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                graphicOverlay.add(new PoseGraphic(graphicOverlay,pose,false));
                                                            }
                                                        }
                                                );
                                            }catch (Exception e){
                                                Log.d("GraphicOverlayError",e.toString());
                                            }
                                        }
                                    }
                            ).addOnCompleteListener(
                                    new OnCompleteListener<Pose>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Pose> task) {
                                            imageProxy.close();
                                            graphicOverlay.clear();
                                        }
                                    }
                            ).addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("failure",e.toString());
                                        }
                                    }
                            );
                }
            }
        });
        cameraProvider.unbindAll();
        cameraProvider.bindToLifecycle(this,cameraSelector,preview,imageAnalysis);
    }
}