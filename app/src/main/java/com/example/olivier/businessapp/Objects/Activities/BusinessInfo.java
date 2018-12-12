package com.example.olivier.businessapp.Objects.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.olivier.businessapp.R;
import com.example.olivier.businessapp.views.SquareImageView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class BusinessInfo extends AppCompatActivity {

    ViewPager viewPager;
    private DrawerLayout mdrawer;
    LinearLayout slider;
    String pic,pic2,pic3,pic4, id;
    StorageReference storageRef;
    FirebaseStorage storage;
    File ratings;
    Context context;
    LinearLayout sliderpanel;
    private static final String TAG="FireLog";
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mdrawer = (DrawerLayout) findViewById(R.id.dre_drawer);
        NavigationView navigationView = findViewById(R.id.nav_view);
////////////////////////////////////////////////setting up the navigation drawer////////////////////
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);



////////////////////////////////////////////////////////////////////////////////////////////////////

        setContentView(R.layout.activity_business_info);
        storage= FirebaseStorage.getInstance();
        sliderpanel = (LinearLayout) findViewById(R.id.slider);
        Bundle extras = getIntent().getExtras();
        if(extras !=null)
        {
            pic = extras.getString("pic");
            pic2 = extras.getString("pic2");
            pic3 = extras.getString("pic3");
            pic4 = extras.getString("pic4");
            id=extras.getString("id");
        }

        dots = new ImageView[4];
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        viewPager.setAdapter(adapter);

        for(int i = 0; i < 4; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderpanel.addView(dots[i], params);

        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for(int i = 0; i< 4; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class ImagePagerAdapter extends PagerAdapter {

        private String[] mImages = new String[] {
                pic,
                pic2,
                pic3,
                pic4
        };
        @Override
        public int getCount() {
            return mImages.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final long ONE_MEGABYTE = 2048 * 2048;
            storageRef = storage.getReferenceFromUrl("gs://business-5c307.appspot.com").child(mImages[position]);
            Context context = BusinessInfo.this;
            //final SquareImageView imageView = new SquareImageView(context);
            final ImageView imageView = new ImageView(context);
            int padding = context.getResources().getDimensionPixelSize(
                    R.dimen.padding_small);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed

                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            ((ViewPager) container).addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((ImageView) object);
        }

    }

    public void rate(View view)
    {
        Context ctx = getApplicationContext();
        BufferedReader reader;
        String filename = "rating.txt";
        String fileContents = "";
        String yourFilePath = ctx.getFilesDir() + "/" + "rating.txt";
        FileOutputStream outputStream;
        byte b='0';
        boolean rated=false;
        try{
            outputStream = openFileOutput(yourFilePath, ctx.MODE_PRIVATE);
            //outputStream.write(b);
            outputStream.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            reader= new BufferedReader(new FileReader(yourFilePath));
            String line = reader.readLine();
            while (line != null) {
                if(line == id)
                {
                    rated=true;
                }
                reader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(!rated)
        {

            try (PrintWriter p = new PrintWriter(new FileOutputStream(yourFilePath, true))) {
                p.println(id);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
        }

        if (rated)
        {
            Log.d(TAG, "Rated");
        }
    }
}


