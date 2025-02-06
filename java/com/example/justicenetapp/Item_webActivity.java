package com.example.justicenetapp;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

class WebViewLogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WebActivityAdapter adapter;
    private List<WebActivityLog> webActivityLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_log);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        webActivityLogs = new ArrayList<>();
        adapter = new WebActivityAdapter(webActivityLogs);
        recyclerView.setAdapter(adapter);

        // Fetch WebView activities from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("webview_activities").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                webActivityLogs.clear();
                for (QueryDocumentSnapshot doc : value) {
                    WebActivityLog log = doc.toObject(WebActivityLog.class);
                    webActivityLogs.add(log);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
