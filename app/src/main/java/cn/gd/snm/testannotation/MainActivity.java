package cn.gd.snm.testannotation;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.gd.snm.annotationpro.BindView;
import cn.gd.snm.testannotation.testapt02.ViewBinding;
import cn.gd.snm.testannotation.testref02.InjectUtils;
import cn.gd.snm.testannotation.testref02.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectUtils.inject(this);
        initView();
    }

    private void initView() {
        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewBinding.bind(MainActivity.this);
                textView.setText("darrenTest...");
            }
        });
    }


    @OnClick({R.id.bt2})
    public void asd(View view){
        Toast.makeText(this, "按下1", Toast.LENGTH_SHORT).show();
    }
}