package com.example.haidtracker.ui.profile;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.haidtracker.R;
import com.example.haidtracker.data.model.user.User;

public class ProfileViewBinder {
    
    private Activity activity;
    
    // Header views
    private TextView txtTitle;
    private ImageButton btnBack;
    
    // Profile views
    private ImageView ivProfilePicture;
    private TextView tvUserName;
    private TextView tvUserEmail;
    private TextView tvUserRole;
    
    // Section titles
    private TextView txtUserInfoTitle;
    private TextView txtActionsTitle;
    
    // Labels
    private TextView txtNameLabel;
    private TextView txtEmailLabel;
    private TextView txtRoleLabel;
    
    // Action buttons
    private Button btnEditProfile;
    private Button btnLogout;
    
    // Loading
    private ProgressBar progressBar;

    public ProfileViewBinder(Activity activity) {
        this.activity = activity;
        bindViews();
    }

    private void bindViews() {
        // Header views
        txtTitle = activity.findViewById(R.id.txtTitle);
        btnBack = activity.findViewById(R.id.btnBack);
        
        // Profile views
        ivProfilePicture = activity.findViewById(R.id.ivProfilePicture);
        tvUserName = activity.findViewById(R.id.tvUserName);
        tvUserEmail = activity.findViewById(R.id.tvUserEmail);
        tvUserRole = activity.findViewById(R.id.tvUserRole);
        
        // Section titles
        txtUserInfoTitle = activity.findViewById(R.id.txtUserInfoTitle);
        txtActionsTitle = activity.findViewById(R.id.txtActionsTitle);
        
        // Labels
        txtNameLabel = activity.findViewById(R.id.txtNameLabel);
        txtEmailLabel = activity.findViewById(R.id.txtEmailLabel);
        txtRoleLabel = activity.findViewById(R.id.txtRoleLabel);
        
        // Action buttons
        btnEditProfile = activity.findViewById(R.id.btnEditProfile);
        btnLogout = activity.findViewById(R.id.btnLogout);
        
        // Loading
        progressBar = activity.findViewById(R.id.progressBar);
    }

    public void setTitle(String title) {
        if (txtTitle != null) {
            txtTitle.setText(title);
        }
    }

    public void displayUserInfo(User user) {
        if (user == null) return;
        
        if (tvUserName != null) {
            tvUserName.setText(user.getName() != null ? user.getName() : "N/A");
        }
        
        if (tvUserEmail != null) {
            tvUserEmail.setText(user.getEmail() != null ? user.getEmail() : "N/A");
        }
        
        if (tvUserRole != null) {
            tvUserRole.setText(user.getRole() != null ? user.getRole() : "user");
        }
    }

    public void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setOnBackClickListener(View.OnClickListener listener) {
        if (btnBack != null) {
            btnBack.setOnClickListener(listener);
        }
    }

    public void setOnEditProfileClickListener(View.OnClickListener listener) {
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(listener);
        }
    }

    public void setOnLogoutClickListener(View.OnClickListener listener) {
        if (btnLogout != null) {
            btnLogout.setOnClickListener(listener);
        }
    }

    public void setProfilePicture(int resourceId) {
        if (ivProfilePicture != null) {
            ivProfilePicture.setImageResource(resourceId);
        }
    }

    public void enableEditButton(boolean enabled) {
        if (btnEditProfile != null) {
            btnEditProfile.setEnabled(enabled);
        }
    }

    public void enableLogoutButton(boolean enabled) {
        if (btnLogout != null) {
            btnLogout.setEnabled(enabled);
        }
    }

    // Getters for direct access if needed
    public TextView getTxtTitle() { return txtTitle; }
    public ImageButton getBtnBack() { return btnBack; }
    public ImageView getIvProfilePicture() { return ivProfilePicture; }
    public TextView getTvUserName() { return tvUserName; }
    public TextView getTvUserEmail() { return tvUserEmail; }
    public TextView getTvUserRole() { return tvUserRole; }
    public Button getBtnEditProfile() { return btnEditProfile; }
    public Button getBtnLogout() { return btnLogout; }
    public ProgressBar getProgressBar() { return progressBar; }
}
