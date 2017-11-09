package andrew.zach.eyeTableP1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Zach on 25/10/2017.
 */

public class cellTables extends AppCompatActivity {

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference newChildDataRef=database.getReference();
        DatabaseReference dbChild1=database.getReference().child("DataTest");


        String s,s2,s3,s4,s5,temp="Default";


        @Override
        protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_cell_tables);

            //Buttons
            final Button sendButton= (Button) findViewById(R.id.sendButton);
            final Button clearFromDB=(Button) findViewById(R.id.removeButton);//remove actual data
            final Button addChildToDB=(Button) findViewById(R.id.button_addChildNode);// add child node
            final Button removeChildFromDB=(Button) findViewById(R.id.removeChild);//remove child form DB
            final Button backButton=(Button) findViewById(R.id.buttonBack);//a back button


            //EditText
            final EditText text=(EditText)findViewById(R.id.textBox1);
            final EditText textForDataRemoval=(EditText) findViewById(R.id.editText_removeData);
            final EditText textForAddingChild= (EditText) findViewById(R.id.editText_addChildNode);
            final EditText textForRemovingChild=(EditText) findViewById(R.id.editText_removeChildNode);
            final EditText childTextBox=(EditText) findViewById(R.id.editText_sendChildBox);


            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s=text.getText().toString(); //get text to string before sending to the db
                    s5=childTextBox.getText().toString();//get child node info to send data to

                    if(!s.isEmpty()&&!s5.isEmpty()) //if s & s5 have data and location, then send
                    {
                        newChildDataRef.child(s5).setValue(s);
                    }
                    text.setText("");
                }
            });

            clearFromDB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s2=textForDataRemoval.getText().toString();
                    if(!s2.isEmpty()&&!s2.equals("")){
                    newChildDataRef.child(s2).setValue("***CLEARED***");//remove data
                    }
                    textForDataRemoval.setText("");
                }
            });

            addChildToDB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s3=textForAddingChild.getText().toString();
                    if(!s3.isEmpty()) {
                        newChildDataRef.child(s3).setValue("Empty"); //create a new child/update existing, according to text referenced
                    }
                    textForAddingChild.setText("");
                }
            });

            removeChildFromDB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (textForRemovingChild.getText() != null) {
                        s4 = textForRemovingChild.getText().toString();


                        newChildDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(s4).exists()) {
                                    temp = dataSnapshot.child(s4).getKey();
                                    // Object obj=dataSnapshot.getKey();
                                    // temp=obj.toString();
                                    System.out.println("******VALUE OF dataSnapshot******" + dataSnapshot.child(s4).getKey());
                                    System.out.println("******VALUE OF temp******" + temp);
                                    System.out.println("******VALUE OF s4******" + s4);
                                    if(s4!=null&&temp!=null) {
                                        if (temp.equals(s4)) {
                                            newChildDataRef.child(temp).removeValue();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //do nothing
                            }
                        });

                        textForRemovingChild.setText("");//clear text for easier user
                    }
                }

            });

            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(cellTables.this,LogInActivity.class);
                    startActivity(intent);
                }
            });

    }//outside onCreate

}
