package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity {
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> arrayAdapter;
    private String followedUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);
        FancyToast.makeText(this,"Welcome " +ParseUser.getCurrentUser().getUsername(),Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
        listView = findViewById(R.id.myListView);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(TwitterUsers.this,android.R.layout.simple_list_item_checked, arrayList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView checkedTextView = (CheckedTextView)view;
                if(checkedTextView.isChecked()){
                    FancyToast.makeText(TwitterUsers.this, "You are now following " +arrayList.get(position) +"!", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                    ParseUser.getCurrentUser().add("following",arrayList.get(position));
                }
                else{
                    FancyToast.makeText(TwitterUsers.this, "You unfollowed " +arrayList.get(position) +"!", Toast.LENGTH_SHORT, FancyToast.INFO, true).show();
                    ParseUser.getCurrentUser().getList("following").remove(arrayList.get(position));
                    List currentUserFollowingList = ParseUser.getCurrentUser().getList("following");
                    ParseUser.getCurrentUser().remove("following");
                    ParseUser.getCurrentUser().put("following",currentUserFollowingList);
                }
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            FancyToast.makeText(TwitterUsers.this,"Saved",Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        }
                    }
                });
            }
        });
        try {
            ParseQuery<ParseUser> getUsers = ParseUser.getQuery();
            getUsers.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            getUsers.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                            for (ParseUser twitterUsers : objects) {
                                arrayList.add(twitterUsers.getUsername());
                            }
                            listView.setAdapter(arrayAdapter);
                            for(String twitterUser: arrayList){
                                if(ParseUser.getCurrentUser().getList("following") !=null){
                                if(ParseUser.getCurrentUser().getList("following").contains(twitterUser)){
                                    followedUser = followedUser + twitterUser +"\n";
                                    listView.setItemChecked(arrayList.indexOf(twitterUser),true);
                                    FancyToast.makeText(TwitterUsers.this,"You are following \n" +followedUser,Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
                                }
                            }
                        }
                    } else {
                        FancyToast.makeText(TwitterUsers.this, e.getMessage(), Toast.LENGTH_SHORT, FancyToast.ERROR, true).show();
                    }

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.logout){
            ParseUser.getCurrentUser().logOut();
            finish();
            startActivity(new Intent(TwitterUsers.this,SignUpActivity.class));
        }else if(item.getItemId()==R.id.sendTweet){
            Intent intent = new Intent(TwitterUsers.this,SendingTweetActivity.class);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);
    }

}
