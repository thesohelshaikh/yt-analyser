package com.thesohelshaikh.ytanalyser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.thesohelshaikh.ytanalyser.R
import com.thesohelshaikh.ytanalyser.UtilitiesManger.getDateAfter
import com.thesohelshaikh.ytanalyser.UtilitiesManger.getPrettyDuration
import java.util.*

class DurationsAdapter(private val ctx: Context, private val durations: List<Long>) :
    ArrayAdapter<Long?>(
        ctx, R.layout.duration_layout, durations
    ) {
    private val playbacks: MutableList<String>
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var root = convertView
        val holder: DurationViewHolder?
        if (root == null) {
            val inflater =
                ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            root = inflater.inflate(R.layout.duration_layout, parent, false)
            holder = DurationViewHolder(root)
            root.tag = holder
        } else {
            holder = root.tag as DurationViewHolder
        }
        holder.playbackTextView.text = playbacks[position]
        holder.duration.text = getPrettyDuration(durations[position])
        holder.completeByTextView.text = getDateAfter(durations[position])
        return root!!
    }

    init {
        playbacks = ArrayList()
        playbacks.add("At 1x")
        playbacks.add("At 1.25x")
        playbacks.add("At 1.5x")
        playbacks.add("At 1.75x")
        playbacks.add("At 2x")
    }

    class DurationViewHolder internal constructor(v: View) {
        var playbackTextView: TextView
        var duration: TextView
        var completeByTextView: TextView

        init {
            playbackTextView = v.findViewById(R.id.tv_playback)
            duration = v.findViewById(R.id.tv_updated_duration)
            completeByTextView = v.findViewById(R.id.tv_complete_by)
        }
    }

}