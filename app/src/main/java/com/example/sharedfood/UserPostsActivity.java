package com.example.sharedfood;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

//public class UserPostsActivity extends AppCompatActivity implements MyPostsAdapter.PostDeleteListener {
  public class UserPostsActivity extends AppCompatActivity implements MyPostsAdapter.PostDeleteListener, MyPostsAdapter.PostEditListener{

    private static final String TAG = "UserPostsActivity";
    private RecyclerView postRecyclerView;
    private MyPostsAdapter postAdapter;
    private FirebaseFirestore db;
    private String userEmail;
    private List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_posts);

        Log.d("UserPostsActivity", "📌 onCreate started!");

        // אתחול Firestore
        db = FirebaseFirestore.getInstance();

        // אתחול RecyclerView
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // אתחול רשימת הפוסטים
        postList = new ArrayList<>();

        // ✅ הגדרת האדפטר במצב ניהול, כדי שהמנהל יוכל למחוק ולערוך פוסטים
        postAdapter = new MyPostsAdapter(postList, this, this, true);
        postRecyclerView.setAdapter(postAdapter);

        Log.d("UserPostsActivity", "✅ RecyclerView & Adapter initialized!");

        // קבלת כתובת האימייל של המשתמש שנבחר
        userEmail = getIntent().getStringExtra("userEmail");
        Log.d("UserPostsActivity", "📩 Received userEmail: " + userEmail);

        // בדיקה אם המשתמש קיים
        if (userEmail == null || userEmail.isEmpty()) {
            Log.e("UserPostsActivity", "❌ Error: userEmail is missing!");
            Toast.makeText(this, "שגיאה: אימייל משתמש חסר", Toast.LENGTH_SHORT).show();
            finish(); // חזרה למסך הקודם כדי למנוע טעינה ריקה
            return;
        }

        // קריאה לטעינת הפוסטים
        loadUserPosts();
    }



    private void loadUserPosts() {
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "שגיאה: אימייל משתמש חסר", Toast.LENGTH_SHORT).show();
            finish(); // חזרה למסך הקודם כדי למנוע טעינה ריקה
            return;
        }

        Log.d(TAG, "🔍 Fetching posts for user: " + userEmail);

        db.collection("posts").whereEqualTo("userId", userEmail).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        postList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Post post = document.toObject(Post.class);

                                // 🔹 אם imageUri הוא String בפיירבייס, צריך להמיר אותו ל-Uri
                                if (document.contains("imageUri") && document.get("imageUri") instanceof String) {
                                    post.setImageUriString(document.getString("imageUri"));
                                }

                                post.setId(document.getId());
                                postList.add(post);
                            } catch (Exception e) {
                                Log.e(TAG, "❌ Error parsing document to Post", e);
                            }
                        }
                        postAdapter.notifyDataSetChanged();
                        Log.d(TAG, "✅ Loaded " + postList.size() + " posts.");
                    } else {
                        Log.e(TAG, "❌ Failed to load posts", task.getException());
                        Toast.makeText(this, "שגיאה בטעינת הפוסטים", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onDeleteClick(Post post) {
        db.collection("posts").document(post.getId()).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "הפוסט נמחק בהצלחה", Toast.LENGTH_SHORT).show();
                    loadUserPosts(); // רענון הרשימה
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error deleting post", e);
                    Toast.makeText(this, "שגיאה במחיקת הפוסט", Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    public void onEditClick(Post post) {
        new MyPostsActivity().onEditClick(post);
    }

}
