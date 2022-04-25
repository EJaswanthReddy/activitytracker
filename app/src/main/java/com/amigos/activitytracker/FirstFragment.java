package com.amigos.activitytracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.amigos.activitytracker.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {

    private boolean isRecording = false;
    private ImageButton record;
    private ImageButton listButton;
    private MediaRecorder mediaRecorder;
    private String recordPath=null;
    private Chronometer timer;
    private TextView textRecord;
    private Button toText;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isRecording = false;

        listButton = view.findViewById(R.id.listButton);
        timer = view.findViewById(R.id.timer);
        textRecord = view.findViewById(R.id.textview_first);
        record = view.findViewById(R.id.record);
        toText = view.findViewById(R.id.totext);

        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording){
                    isRecording=false;
                    record.setImageResource(R.drawable.ic_baseline_keyboard_voice_24);
                    stopRecording();

                }else {
                    if(checkPermission()){
                        isRecording=true;
                        record.setImageResource(R.drawable.ic_outline_stop_circle_24);
                        startRecording();

                    }
                }
            }
        });
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRecording){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
//                            stopRecording();
                            isRecording=false;
                            NavHostFragment.findNavController(FirstFragment.this)
                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);
                        }
                    });
                    dialog.setNegativeButton("No", null);
                    dialog.setTitle("Audio Recording");
                    dialog.setMessage("Do you want to stop recording?");
                    dialog.create();
                    dialog.show();
                }else {
                    isRecording=false;
                    Log.d("TAG98", "inclick: "+isRecording);
                    NavHostFragment.findNavController(FirstFragment.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
            }
        });

        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSpeechInput(view);

            }
        });
    }

    private void startRecording() {

        Log.d("TAG_11", "startRecording: Audio started recording");
        SimpleDateFormat name = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.US);
        Date now = new Date();
        recordPath = "myrecord"+name.format(now)+".m4a";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(getActivity().getExternalFilesDir("/").getAbsolutePath()+"/"+recordPath);

        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
        textRecord.setText("Recording your activity");
        timer.setBase(SystemClock.elapsedRealtime());
        timer.start();
    }

    private void stopRecording() {

        Log.d("TAG_11", "stopRecording: Stop button/back button pressed, stopping the audio recording.");
        mediaRecorder.stop();
        timer.stop();
        textRecord.setText("Record a new activity");
        mediaRecorder.release();
        mediaRecorder = null;
    }
    

    private boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.RECORD_AUDIO}, 200);
            return false;
        }
    }

    public void getSpeechInput(View view) {

        Log.d("TAG_09", "getSpeechInput: Listening to activity to convert to speech.");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
//            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
            Log.d("TAG_09", "getSpeechInput: Your mobile may not support this feature.");
        }
    }

    @Override
    public void onStop() {
        Log.d("TAG98", "onStop: "+isRecording);
        super.onStop();

        if(isRecording){
            stopRecording();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TAG89", "onActivityResult: "+requestCode+data+resultCode);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textRecord.setText(result.get(0));
                }
                break;
        }
    }
}