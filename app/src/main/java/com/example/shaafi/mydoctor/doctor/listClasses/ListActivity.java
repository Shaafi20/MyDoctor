package com.example.shaafi.mydoctor.doctor.listClasses;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaafi.mydoctor.R;
import com.example.shaafi.mydoctor.doctor.DoctorDetails;
import com.example.shaafi.mydoctor.doctor.DoctorHomePage;
import com.example.shaafi.mydoctor.doctor.PatientDetailsForDoctorList;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListActivity extends AppCompatActivity {

    PatientDetailsForDoctorList[] mPatientList;

    @BindView(R.id.patientListRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.emptyList)
    TextView mEmptyListMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        addSupportActionBar();
        ButterKnife.bind(this);

        Parcelable[] parcelables = getIntent().getParcelableArrayExtra(DoctorHomePage.PATIENT_LIST);
        if (parcelables != null) {
            mPatientList = Arrays.copyOf(parcelables, parcelables.length, PatientDetailsForDoctorList[].class);

            PatientListAdapter adapter = new PatientListAdapter(this, mPatientList);
            mRecyclerView.setAdapter(adapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            mRecyclerView.setNestedScrollingEnabled(true);
            mRecyclerView.setHorizontalScrollBarEnabled(true);

            Drawable dividerDrawable = ContextCompat.getDrawable(this,
                    R.drawable.devider);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(dividerDrawable));
        }
        else {
            mEmptyListMsg.setVisibility(View.VISIBLE);
            mEmptyListMsg.setText("No patient found");
        }
    }

    public void addSupportActionBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
                && getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_delete){
            //Toast.makeText(ListActivity.this, "clicked", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private Drawable mDivider;

        public DividerItemDecoration(Drawable divider) {
            mDivider = divider;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);

            if (parent.getChildAdapterPosition(view) == 0) {
                return;
            }

            outRect.top = mDivider.getIntrinsicHeight();
        }

        @Override
        public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
            int dividerLeft = parent.getPaddingLeft();
            int dividerRight = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount - 1; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int dividerTop = child.getBottom() + params.bottomMargin;
                int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

                mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
                mDivider.draw(canvas);
            }
        }
    }
}
