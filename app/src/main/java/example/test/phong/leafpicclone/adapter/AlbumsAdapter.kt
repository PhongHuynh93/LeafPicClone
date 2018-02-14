package example.test.phong.leafpicclone.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import example.test.phong.leafpicclone.R
import example.test.phong.leafpicclone.data.Album
import example.test.phong.leafpicclone.data.CardViewStyle
import example.test.phong.leafpicclone.data.Media
import example.test.phong.leafpicclone.data.SortingOrder
import example.test.phong.leafpicclone.data.local.Prefs
import example.test.phong.leafpicclone.data.sort.AlbumsComparators
import example.test.phong.leafpicclone.data.sort.SortingMode
import example.test.phong.leafpicclone.util.StringUtils
import io.reactivex.subjects.PublishSubject
import org.horaapps.liz.*
import org.horaapps.liz.ui.ThemedIcon
import java.util.*

/**
 * Created by user on 2/10/2018.
 */
class AlbumsAdapter(context: Context, var sortingMode: SortingMode, var sortingOrder: SortingOrder) : ThemedAdapter<AlbumsAdapter.ViewHolder>(context) {
    val albums = ArrayList<Album>()
    val placeholder = themeHelper.placeHolder
    val cardViewStyle = Prefs.getCardStyle()
    var selectedCount: Int = 0
    val onClickSubject: PublishSubject<Album> = PublishSubject.create()
    val onChangeSelectedSubject: PublishSubject<Album> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var v: View = when (cardViewStyle) {
            CardViewStyle.MATERIAL -> LayoutInflater.from(parent.getContext()).inflate(R.layout.card_album_material, parent, false)
            CardViewStyle.FLAT -> LayoutInflater.from(parent.getContext()).inflate(R.layout.card_album_flat, parent, false)
            CardViewStyle.COMPACT -> LayoutInflater.from(parent.getContext()).inflate(R.layout.card_album_compact, parent, false)
        }
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return albums.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // TODO Calvin: Major Refactor - No business logic here.
        val a = albums[position]
        holder.refreshTheme(themeHelper, cardViewStyle, a.selected)

        val f = a.getCover() as Media

        val options = RequestOptions()
                .signature(f.getSignature())
                .format(DecodeFormat.PREFER_ARGB_8888)
                .centerCrop()
                .placeholder(placeholder)
                .error(R.drawable.ic_error)
                //.animate(R.anim.fade_in)//TODO:DONT WORK WELL
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

        Glide.with(holder.picture.context)
                .load(f.path)
                .apply(options)
                .into(holder.picture)


        var accentColor = themeHelper.accentColor

        if (accentColor == themeHelper.primaryColor)
            accentColor = ColorPalette.getDarkerColor(accentColor)

        var textColor = themeHelper.getColor(if (themeHelper.baseTheme == Theme.LIGHT) R.color.md_album_color_2 else R.color.md_album_color)

        if (a.selected)
            textColor = themeHelper.getColor(R.color.md_album_color)

        holder.mediaLabel.setTextColor(textColor)

        holder.llCount.visibility = if (Prefs.showMediaCount()) View.VISIBLE else View.GONE
        holder.name.setText(StringUtils.htmlFormat(a.name, textColor, false, true))
        holder.nMedia.setText(StringUtils.htmlFormat(a.count.toString(), accentColor, true, false))
        holder.path.visibility = if (Prefs.showAlbumPath()) View.VISIBLE else View.GONE
        holder.path.setText(a.path)

//        fixme - no notify here
        holder.card.setOnClickListener { v ->
            if (selecting()) {
                notifySelected(a.toggleSelected())
                notifyItemChanged(position)
                onChangeSelectedSubject.onNext(a)
            } else
                onClickSubject.onNext(a)
        }

        holder.card.setOnLongClickListener { v ->
            notifySelected(a.toggleSelected())
            notifyItemChanged(position)
            onChangeSelectedSubject.onNext(a)
            true
        }
    }

    private fun notifySelected(increase: Boolean) {
        selectedCount += if (increase) 1 else -1
    }

    fun selecting(): Boolean = (selectedCount > 0)

    class ViewHolder(view: View) : ThemedViewHolder(view) {
        var card: CardView
        var picture: ImageView
        var selectedIcon: ThemedIcon
        var footer: View
        var llCount: View
        var name: TextView
        var nMedia: TextView
        var mediaLabel: TextView
        var path: TextView

        init {
            card = itemView.findViewById(R.id.album_card) as CardView
            picture = itemView.findViewById(R.id.album_preview) as ImageView
            selectedIcon = itemView.findViewById(R.id.selected_icon) as ThemedIcon
            footer = itemView.findViewById(R.id.ll_album_info) as View
            llCount = itemView.findViewById(R.id.ll_media_count) as View
            name = itemView.findViewById(R.id.album_name) as TextView
            nMedia = itemView.findViewById(R.id.album_media_count) as TextView
            mediaLabel = itemView.findViewById(R.id.album_media_label) as TextView
            path = itemView.findViewById(R.id.album_path) as TextView
        }

        override fun refreshTheme(themeHelper: ThemeHelper?) {
        }

        fun refreshTheme(theme: ThemeHelper, cvs: CardViewStyle, selected: Boolean) {
            if (selected) {
                footer.setBackgroundColor(theme.primaryColor)
                picture.setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP)
                selectedIcon.setVisibility(View.VISIBLE)
                selectedIcon.setColor(theme.primaryColor)
            } else {
                picture.clearColorFilter()
                selectedIcon.setVisibility(View.GONE)
                when (cvs) {
                    CardViewStyle.MATERIAL -> footer.setBackgroundColor(theme.cardBackgroundColor)
                    CardViewStyle.FLAT, CardViewStyle.COMPACT -> footer.setBackgroundColor(ColorPalette.getTransparentColor(theme.backgroundColor, 150))
                    else -> footer.setBackgroundColor(theme.cardBackgroundColor)
                }
            }

            path.setTextColor(theme.subTextColor)
        }
    }

    fun clearSelected(): Boolean {
        var changed = true
        for (i in albums.indices) {
            val b = albums[i].setSelected(false)
            if (b)
                notifyItemChanged(i)
            changed = changed and b
        }

        selectedCount = 0

        if (changed)
            onChangeSelectedSubject.onNext(Album.getEmptyAlbum())
        return changed
    }

    fun clear() {
        albums.clear()
        notifyDataSetChanged()
    }

    fun add(album: Album): Int {
        var i = Collections.binarySearch(
                albums, album, AlbumsComparators.getComparator(sortingMode, sortingOrder))
        if (i < 0) i = i.inv()
        albums.add(i, album)
        notifyItemInserted(i)
        //int finalI = i;
        //((ThemedActivity) context).runOnUiThread(() -> notifyItemInserted(finalI));
        return i

    }

    fun getAlbumsPaths(): List<String?> {
        val list = ArrayList<String?>()

        for (album in albums) {
            list.add(album.path)
        }

        return list
    }
}