package com.example.jradio_app.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jradio_app.databinding.FragmentMediaBinding;

public class MediaFragment extends Fragment {

    private FragmentMediaBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MediaViewModel mediaViewModel =
                new ViewModelProvider(this).get(MediaViewModel.class);

        binding = FragmentMediaBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMedia;
        mediaViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}