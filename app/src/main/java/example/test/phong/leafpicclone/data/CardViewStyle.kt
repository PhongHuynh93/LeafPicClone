package example.test.phong.leafpicclone.data

import example.test.phong.leafpicclone.R

/**
 * Created by user on 2/10/2018.
 */
enum class CardViewStyle constructor(var value: Int, var layout: Int) {
    MATERIAL(0, R.layout.card_album_material),
    FLAT(1, R.layout.card_album_flat),
    COMPACT(2, R.layout.card_album_compact);

    companion object {
        val size = CardViewStyle.values().size

        fun fromValue(value: Int): CardViewStyle {
            when (value) {
                0 -> return MATERIAL
                1 -> return FLAT
                2 -> return COMPACT
                else -> return MATERIAL
            }
        }
    }
}