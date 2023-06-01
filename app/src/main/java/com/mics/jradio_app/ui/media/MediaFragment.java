package com.mics.jradio_app.ui.media;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mics.jradio_app.databinding.FragmentMediaBinding;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MediaViewModel mediaViewModel =
                new ViewModelProvider(this).get(MediaViewModel.class);

        binding = FragmentMediaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMedia;

        final Button btnvk = binding.buttonvk;
        // final Button btntg = binding.buttontg;

        btnvk.setOnClickListener(v -> {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/junoeradio")));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}