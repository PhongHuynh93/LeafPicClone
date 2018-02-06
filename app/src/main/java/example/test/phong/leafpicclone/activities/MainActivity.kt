package example.test.phong.leafpicclone.activities

import android.os.Bundle
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.activities.base.SharedMediaActivity

class MainActivity : SharedMediaActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        initUi()

    }
}
