package com.example.sharedfood;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminActionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_actions);

        // כפתור לניהול פוסטים
        Button managePostsButton = findViewById(R.id.managePostsButton);
        managePostsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActionsActivity.this, ManagePostsActivity.class);
            startActivity(intent);
        });

        // Michael, 26/01/2025 - START: הוספת כפתור "רשימת מנהלים"
        Button adminListButton = findViewById(R.id.adminListButton);
        adminListButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActionsActivity.this, AdminListActivity.class);
            startActivity(intent);
        });
        // Michael, 26/01/2025 - END: הוספת כפתור "רשימת מנהלים"

        // Michael, 26/01/2025 - START: הוספת כפתור "רשימת משתמשים"
        Button userListButton = findViewById(R.id.userListButton);
        userListButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActionsActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        // Michael, 26/01/2025 - END: הוספת כפתור "רשימת משתמשים"

        // Michael, 17/02/2025 - START: הוספת כפתור "רשימת פוסטים לכל משתמש"
        Button userPostsListButton = findViewById(R.id.userPostsListButton);
        userPostsListButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActionsActivity.this, UserListActivity.class);
            startActivity(intent);
        });
        // Michael, 17/02/2025 - END: הוספת כפתור "רשימת פוסטים לכל משתמש"
    }
}
