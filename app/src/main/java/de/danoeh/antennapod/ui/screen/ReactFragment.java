package de.danoeh.antennapod.ui.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.danoeh.antennapod.R;
import de.danoeh.antennapod.activity.MyReactActivity;

public class ReactFragment extends Fragment {
    public static final String TAG = "ReactFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Create a layout matching the app's style
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(64, 64, 64, 64);
        layout.setBackgroundColor(getResources().getColor(android.R.color.background_light, null));

        // Add a title
        TextView title = new TextView(getContext());
        title.setText("React Native Integration");
        title.setTextSize(24);
        title.setTextColor(getResources().getColor(android.R.color.black, null));
        title.setPadding(0, 0, 0, 32);
        layout.addView(title);

        // Add description
        TextView description = new TextView(getContext());
        description.setText("This tab demonstrates React Native integration in the AntennaPod app. Tap the button below to launch the React Native screen.");
        description.setTextSize(16);
        description.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        description.setPadding(0, 0, 0, 32);
        layout.addView(description);

        // Add launch button
        Button button = new Button(getContext());
        button.setText("Launch React Native Screen");
        button.setTextSize(18);
        button.setPadding(32, 16, 32, 16);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MyReactActivity.class);
            startActivity(intent);
        });

        layout.addView(button);
        return layout;
    }

    @Override
    public String toString() {
        return TAG;
    }
}
