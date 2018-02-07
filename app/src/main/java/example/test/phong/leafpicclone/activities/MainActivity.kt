package example.test.phong.leafpicclone.activities

import android.os.Bundle
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.activities.base.SharedMediaActivity

class MainActivity : SharedMediaActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUi()

        // todo pickmode

        // todo add fragment album

        // todo add fragment media


    }

    private fun initUi() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPostResume() {
        super.onPostResume()
    }
}
