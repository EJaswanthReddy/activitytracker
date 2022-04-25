package com.amigos.activitytracker;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;

public class SecondFragment extends Fragment implements RecordedListAdapter.onItemClick{

    private RecyclerView recordList;
    private File[] recordedFiles;
    private RecordedListAdapter adapter;
    private MediaPlayer playAudio = null;
    private boolean isAudioPlaying=false;
    private File recordedAudio;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        recordList = view.findViewById(R.id.recordingsList);
        String filePath = getActivity().getExternalFilesDir("/").getAbsolutePath();
        File dir = new File(filePath);
        recordedFiles = dir.listFiles();

        adapter = new RecordedListAdapter(recordedFiles, this);
        recordList.setHasFixedSize(true);
        recordList.setLayoutManager(new LinearLayoutManager(getContext()));
        recordList.setAdapter(adapter);
    }

    @Override
    public void onClickListner(File file, int position) {
        if (isAudioPlaying){
            stopAudio();

            if(recordedAudio.getAbsoluteFile()!=file.getAbsoluteFile()){
                recordedAudio = file;
                playRecorded(recordedAudio);

            }

        }else{
            recordedAudio = file;
            playRecorded(recordedAudio);

        }
    }

    private void stopAudio() {
        Log.d("TAG_12", "stopAudio: Audio Stopped");
        isAudioPlaying = false;
        playAudio.stop();
        playAudio.release();
        playAudio = null;
    }

    private void playRecorded(File recordedAudio) {
        Log.d("TAG_12", "stopAudio: Start playing audio");
        isAudioPlaying = true;
        playAudio = new MediaPlayer();

        try {
            playAudio.setDataSource(recordedAudio.getAbsolutePath());
            playAudio.prepare();
            playAudio.start();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if(isAudioPlaying)
            stopAudio();
    }
}